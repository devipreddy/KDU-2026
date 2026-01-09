package com.example.DeviPrasadSecurityCorporatePortal.controller;

import com.example.DeviPrasadSecurityCorporatePortal.model.AppUser;
import com.example.DeviPrasadSecurityCorporatePortal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import com.example.DeviPrasadSecurityCorporatePortal.service.JWTService;
import org.springframework.security.access.prepost.PreAuthorize;


import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/users")
public class UserController {


    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;



    public UserController(InMemoryUserDetailsManager userDetailsManager,
                        PasswordEncoder passwordEncoder,
                        UserService userService,
                        AuthenticationManager authenticationManager,
                        JWTService jwtService) {

        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }



    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody AppUser user) {

        boolean created = userService.registerUser(user);

        if (created) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("User registered successfully");
        }

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body("Username already exists");
    }

    @PreAuthorize("hasAnyRole('BASIC', 'ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<AppUser> getCurrentUser(Authentication authentication) {

        AppUser appUser = userService.getCurrentUser(authentication);

        if (appUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(appUser);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AppUser user) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword())
        );

        if (auth.isAuthenticated()) {
            return ResponseEntity.ok(jwtService.generateToken(user.getUserName()));
        }

        return ResponseEntity.status(401).build();
    }




}