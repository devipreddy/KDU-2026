package com.example.QuickShip.controller;

import com.example.QuickShip.dto.PackageRequest;
import com.example.QuickShip.dto.PackageResponse;
import com.example.QuickShip.dto.RevenueResponse;
import com.example.QuickShip.model.Consignment;
import com.example.QuickShip.service.ConsignmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.net.URI;
import com.example.QuickShip.error.ErrorResponse;

@RestController
@Tag(name = "Consignment Management", description = "APIs for managing packages and logistics")
public class ConsignmentController {
    private final ConsignmentService consignmentService;
    private final Logger logger = LoggerFactory.getLogger(ConsignmentController.class);

    public ConsignmentController(ConsignmentService consignmentService) {
        this.consignmentService = consignmentService;
    }


    @Operation(
            summary = "Create a new package",
            description = "Accept a new package from the warehouse manager ASYNCHRONOUSLY. Only MANAGER role can access.",
            security = {@SecurityRequirement(name = "Bearer Token")}
    )
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/packages")
    public ResponseEntity<?> createPackage(@RequestBody PackageRequest request) {
        try {
            logger.info("Creating new package for destination: {}", request.getDestination());
            PackageResponse response = consignmentService.createPackageAsync(request);
            // return 202 Accepted since processing continues in background
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating package: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error creating package: " + e.getMessage()));
        }
    }


    @Operation(
            summary = "Create a new package",
            description = "Accept a new package from the warehouse manager. Only MANAGER role can access.",
            security = {@SecurityRequirement(name = "Bearer Token")}
    )
    @PreAuthorize("hasRole('MANAGER')")
    @PostMapping("/packages/sync")
    public ResponseEntity<?> createPackageSync(@RequestBody PackageRequest request) {
        try {
            logger.info("Creating new package for destination: {}", request.getDestination());
            PackageResponse response = consignmentService.createPackageSync(request);
            // return 201 Created with Location header
            URI location = URI.create("/packages/" + response.getId());
            return ResponseEntity.created(location).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating package: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error creating package: " + e.getMessage()));
        }
    }


    @Operation(
            summary = "Get revenue analytics",
            description = "Calculate total projected revenue from sorted packages. Both MANAGER and DRIVER can access.",
            security = {@SecurityRequirement(name = "Bearer Token")}
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'DRIVER')")
    @GetMapping("/analytics/revenue")
    public ResponseEntity<?> getRevenueAnalytics() {
        try {
            logger.info("Fetching revenue analytics");
            RevenueResponse response = consignmentService.calculateRevenue();
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Error fetching revenue analytics: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error fetching revenue analytics: " + e.getMessage()));
        }
    }


    @Operation(
            summary = "Get All Packages in the Warehouse",
            description = "Get all the packages in the Warehouse. Both MANAGER and DRIVER can access.",
            security = {@SecurityRequirement(name = "Bearer Token")}
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'DRIVER')")
    @GetMapping("/packages")
    public ResponseEntity<List<Consignment>> getPackages() {
        logger.info("Returning all requested packages");
        return ResponseEntity.ok(consignmentService.getAllPackages());  
    }


    @Operation(
            summary = "Get Warehouse Details",
            description = "View warehouse statistics including total PENDING and SORTED packages. Only MANAGER role can access.",
            security = {@SecurityRequirement(name = "Bearer Token")}
    )
    @PreAuthorize("hasAnyRole('MANAGER', 'DRIVER')")
    @GetMapping("/warehouse/details")
    public ResponseEntity<?> getWarehouseDetails() {
        try {
            logger.info("Fetching warehouse details");
            return ResponseEntity.ok(consignmentService.getWarehouseStats());
        } catch (Exception e) {
            logger.error("Error fetching warehouse details: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error fetching warehouse details: " + e.getMessage()));
        }
    }
}
