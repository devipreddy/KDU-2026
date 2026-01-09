package com.example.DeviPrasadSecurityCorporatePortal.service;


import com.example.DeviPrasadSecurityCorporatePortal.model.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class UserService{

    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    public UserService(InMemoryUserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder, JWTService jwtService) {
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }




    public boolean registerUser(AppUser user) {
        if (userDetailsManager.userExists(user.getUserName())) {
            return false;
        }

        UserDetails newUser = User.withUsername(user.getUserName())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(user.getRoles().toArray(new String[0]))
                .build();

        userDetailsManager.createUser(newUser);
        return true;
    }


    public AppUser getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return null; // No authenticated user
        }

        String username = authentication.getName();
        UserDetails userDetails = userDetailsManager.loadUserByUsername(username);

        // Convert UserDetails to your User model
        List<String> roles = new ArrayList<>();
        for (GrantedAuthority authority : userDetails.getAuthorities()) {
            roles.add(authority.getAuthority());
        }

        return new AppUser(userDetails.getUsername(), userDetails.getPassword(),"", roles);
    }
}