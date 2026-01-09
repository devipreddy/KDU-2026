package com.example.DeviPrasadSecurityCorporatePortal.config;

import com.example.DeviPrasadSecurityCorporatePortal.service.JWTService;
import com.example.DeviPrasadSecurityCorporatePortal.service.MyUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class CustomSecurityFilterChain {

    @Bean
    public InMemoryUserDetailsManager userDetailsService(PasswordEncoder encoder) {

        UserDetails user1 = User.withUsername("user1")
                .password(encoder.encode("password1"))
                .roles("BASIC")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("adminpass"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user1, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtFilter jwtFilter(JWTService jwtService, MyUserDetailsService uds) {
        return new JwtFilter(jwtService, uds);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtFilter jwtFilter) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/users/register", "/api/users/login").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationManager(authenticationManager(http))   // ← THIS LINE IS CRITICAL
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder builder =
                http.getSharedObject(AuthenticationManagerBuilder.class);

        builder
            .userDetailsService(userDetailsService(passwordEncoder()))
            .passwordEncoder(passwordEncoder());

        return builder.build();
    }


}
