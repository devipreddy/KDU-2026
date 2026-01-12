package com.example.QuickShip.service;

import com.example.QuickShip.dto.PackageRequest;
import com.example.QuickShip.dto.PackageResponse;
import com.example.QuickShip.dto.RevenueResponse;
import com.example.QuickShip.dto.WarehouseStats;
import com.example.QuickShip.model.Consignment;
import com.example.QuickShip.repository.ConsignmentRepository;
import org.springframework.stereotype.Service;
import org.springframework.scheduling.annotation.Async;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class ConsignmentService {
    private final ConsignmentRepository consignmentRepository;
    private final Logger logger = LoggerFactory.getLogger(ConsignmentService.class);
    private static final double REVENUE_RATE_PER_KG = 2.50;
    private final AtomicLong idCounter = new AtomicLong(2);
    private final ExecutorService service = Executors.newSingleThreadExecutor();

    public ConsignmentService(ConsignmentRepository consignmentRepository) {
        this.consignmentRepository = consignmentRepository;
    }


    // Create a new package and store it in H2 Database - Asynchrounsly with delay of 3s

    public PackageResponse createPackageAsync(PackageRequest request) {
        // Validation: destination
        if (request.getDestination() == null || request.getDestination().trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be empty");
        }

        // Validation: weight
        if (request.getWeight() <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }

        // Validation: delivery type
        if (request.getDeliveryType() == null || request.getDeliveryType().trim().isEmpty()) {
            throw new IllegalArgumentException("DeliveryType cannot be empty");
        }

        String deliveryTypeUpper = request.getDeliveryType().toUpperCase();
        if (!deliveryTypeUpper.equals("STANDARD") && !deliveryTypeUpper.equals("EXPRESS")) {
            throw new IllegalArgumentException("DeliveryType must be STANDARD or EXPRESS");
        }

        // Create and save the package
        Consignment consignment = new Consignment();
        consignment.setId(String.valueOf(idCounter.getAndIncrement()));
        consignment.setDestination(request.getDestination());
        consignment.setWeight(request.getWeight());
        consignment.setDeliveryType(Consignment.DeliveryType.valueOf(deliveryTypeUpper));
        consignment.setStatus(Consignment.ConsignmentStatus.PENDING);

        Consignment savedConsignment = consignmentRepository.save(consignment);
        logger.info("Package created with ID: {} at destination: {}", savedConsignment.getId(), request.getDestination());

        // Kick off background thread for async-processing and return immediately
        String packageId = savedConsignment.getId();

        service.submit(() ->{

            logger.info("Background scan started for package {}", packageId);
            try {
                Thread.sleep(3000); // simulate 3-second scanning
                Consignment c = consignmentRepository.findById(packageId).orElse(null);
                if (c != null) {
                    c.setStatus(Consignment.ConsignmentStatus.SORTED);
                    consignmentRepository.save(c);
                    logger.info("Background scan completed for package {}, status set to SORTED", packageId);
                } else {
                    logger.warn("Package {} not found during background scan", packageId);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.error("Background scan interrupted for package {}", packageId);
            } catch (Exception e) {
                logger.error("Error during background scan for package {}: {}", packageId, e.getMessage());
            }
        });

        return new PackageResponse(
                savedConsignment.getId(),
                savedConsignment.getDestination(),
                savedConsignment.getWeight(),
                savedConsignment.getStatus().toString(),
                savedConsignment.getDeliveryType().toString()
        );
    }

    // Create a new package and store it in H2 Database - Synchronously
    public PackageResponse createPackageSync(PackageRequest request) {
        // Validation: destination
        if (request.getDestination() == null || request.getDestination().trim().isEmpty()) {
            throw new IllegalArgumentException("Destination cannot be empty");
        }

        // Validation: weight
        if (request.getWeight() <= 0) {
            throw new IllegalArgumentException("Weight must be positive");
        }

        // Validation: delivery type
        if (request.getDeliveryType() == null || request.getDeliveryType().trim().isEmpty()) {
            throw new IllegalArgumentException("DeliveryType cannot be empty");
        }

        String deliveryTypeUpper = request.getDeliveryType().toUpperCase();
        if (!deliveryTypeUpper.equals("STANDARD") && !deliveryTypeUpper.equals("EXPRESS")) {
            throw new IllegalArgumentException("DeliveryType must be STANDARD or EXPRESS");
        }

        // Create and save the package
        Consignment consignment = new Consignment();
        consignment.setId(String.valueOf(idCounter.getAndIncrement()));
        consignment.setDestination(request.getDestination());
        consignment.setWeight(request.getWeight());
        consignment.setDeliveryType(Consignment.DeliveryType.valueOf(deliveryTypeUpper));
        consignment.setStatus(Consignment.ConsignmentStatus.PENDING);

        Consignment savedConsignment = consignmentRepository.save(consignment);
        logger.info("Package created with ID: {} at destination: {}", savedConsignment.getId(), request.getDestination());

        return new PackageResponse(
                savedConsignment.getId(),
                savedConsignment.getDestination(),
                savedConsignment.getWeight(),
                savedConsignment.getStatus().toString(),
                savedConsignment.getDeliveryType().toString()
        );
    }


    //Calculate total projected revenue from sorted packages

    public RevenueResponse calculateRevenue() {
        List<Consignment> allConsignments = consignmentRepository.findAll();
        List<Consignment> sortedPackages = allConsignments.stream()
                .filter(p -> p.getStatus() == Consignment.ConsignmentStatus.SORTED)
                .toList();

        double totalRevenue = sortedPackages.stream()
                .mapToDouble(p -> p.getWeight() * REVENUE_RATE_PER_KG)
                .sum();

        logger.info("Revenue calculated: ${}, Total sorted packages: {}", 
                String.format("%.2f", totalRevenue), sortedPackages.size());

        return new RevenueResponse(
                Math.round(totalRevenue * 100.0) / 100.0,
                sortedPackages.size(),
                REVENUE_RATE_PER_KG
        );
    }

    // Get the details of Warehourse
    public WarehouseStats getWarehouseStats() {
        List<Consignment> allConsignments = consignmentRepository.findAll();
        long pendingCount = allConsignments.stream()
                .filter(p -> p.getStatus() == Consignment.ConsignmentStatus.PENDING)
                .count();
        long sortedCount = allConsignments.stream()
                .filter(p -> p.getStatus() == Consignment.ConsignmentStatus.SORTED)
                .count();
        
        return new WarehouseStats((int) pendingCount, (int) sortedCount);
    }



    public List<Consignment> getAllPackages() {
        return consignmentRepository.findAll();
    }

    public void updatePackageStatus(String packageId, String newStatus) {
        Consignment consignment = consignmentRepository.findById(packageId)
                .orElseThrow(() -> new IllegalArgumentException("Package not found"));

        try {
            consignment.setStatus(Consignment.ConsignmentStatus.valueOf(newStatus.toUpperCase()));
            consignmentRepository.save(consignment);
            logger.info("Package {} status updated to {}", packageId, newStatus);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid status. Must be PENDING or SORTED");
        }
    }
}