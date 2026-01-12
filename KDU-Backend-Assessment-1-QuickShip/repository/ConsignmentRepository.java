package com.example.QuickShip.repository;

import com.example.QuickShip.model.Consignment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsignmentRepository extends JpaRepository<Consignment, String> {
    
}
