package com.example.QuickShip.dto;

public class PackageResponse {
    private String id;
    private String destination;
    private double weight;
    private String status;
    private String deliveryType;

    public PackageResponse() {}

    public PackageResponse(String id, String destination, double weight, String status, String deliveryType) {
        this.id = id;
        this.destination = destination;
        this.weight = weight;
        this.status = status;
        this.deliveryType = deliveryType;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}
