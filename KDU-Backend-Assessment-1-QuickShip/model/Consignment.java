package com.example.QuickShip.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import java.util.UUID;


@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})

public class Consignment {
    @Id
    private String id;
    private String destination;
    private double weight;
    
    @Enumerated(EnumType.STRING)
    private ConsignmentStatus status;

    public enum ConsignmentStatus{
        PENDING,
        SORTED
    }

    @Enumerated(EnumType.STRING)
    private DeliveryType deliveryType;

    public enum DeliveryType{
        STANDARD,
        EXPRESS
    }

    // Getters and Setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getDestination() {
        return destination;
    }
    public void setDestination(String destination) {
        this.destination = destination;
    }
    public double getWeight() {
        return weight;
    }
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public ConsignmentStatus getStatus() {
        return status;
    }
    public void setStatus(ConsignmentStatus status) {
        this.status = status;
    }
    public DeliveryType getDeliveryType() {
        return deliveryType;
    }
    public void setDeliveryType(DeliveryType deliveryType) {
        this.deliveryType = deliveryType;
    }

}

/**
 



{
  "destination": "ksjfhjf",
  "weight": "23.00",
  "deliveryType": "STANDARD"
}


{
  "destination": "ksjfhjf",
  "weight": "23.00",
  "deliveryType": "STANDARD"
}
{
    "username": "manager",
    "password": "managerpassword"
}

{
    "username": "driver",
    "password": "driverpassword"
}
[
  {
    "destination": "New Delhi, India",
    "weight": "12.50",
    "deliveryType": "STANDARD"
  },
  {
    "destination": "Mumbai, India",
    "weight": "5.75",
    "deliveryType": "EXPRESS"
  },
  {
    "destination": "Bengaluru, India",
    "weight": "8.00",
    "deliveryType": "STANDARD"
  },
  {
    "destination": "Hyderabad, India",
    "weight": "15.20",
    "deliveryType": "EXPRESS"
  },
  {
    "destination": "Chennai, India",
    "weight": "3.60",
    "deliveryType": "STANDARD"
  },
  {
    "destination": "Kolkata, India",
    "weight": "18.45",
    "deliveryType": "EXPRESS"
  },
  {
    "destination": "Pune, India",
    "weight": "6.30",
    "deliveryType": "STANDARD"
  },
  {
    "destination": "Ahmedabad, India",
    "weight": "10.90",
    "deliveryType": "EXPRESS"
  },
  {
    "destination": "Jaipur, India",
    "weight": "4.25",
    "deliveryType": "STANDARD"
  },
  {
    "destination": "Kochi, India",
    "weight": "7.80",
    "deliveryType": "EXPRESS"
  }
]

 */