package com.example.QuickShip.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.QuickShip.dto.LoginRequest;
import com.example.QuickShip.dto.LoginResponse;
import com.example.QuickShip.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
public class AuthController {

    private final AuthService authService;
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "User login", description = "Authenticate user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        String token = authService.login(request.getUsername(), request.getPassword());
        logger.info("Generated token for user {}", request.getUsername());

        return ResponseEntity.ok(new LoginResponse(token));


    }
}
