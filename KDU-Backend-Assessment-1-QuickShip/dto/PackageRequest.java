package com.example.QuickShip.dto;

public class PackageRequest {
    private String destination;
    private double weight;
    private String deliveryType;

    public PackageRequest() {}

    public PackageRequest(String destination, double weight, String deliveryType) {
        this.destination = destination;
        this.weight = weight;
        this.deliveryType = deliveryType;
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

    public String getDeliveryType() {
        return deliveryType;
    }

    public void setDeliveryType(String deliveryType) {
        this.deliveryType = deliveryType;
    }
}
