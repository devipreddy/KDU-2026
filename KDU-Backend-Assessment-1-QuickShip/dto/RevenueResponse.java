package com.example.QuickShip.dto;

public class RevenueResponse {
    private double totalRevenue;
    private int sortedPackagesCount;
    private double ratePerKg;

    public RevenueResponse() {}

    public RevenueResponse(double totalRevenue, int sortedPackagesCount, double ratePerKg) {
        this.totalRevenue = totalRevenue;
        this.sortedPackagesCount = sortedPackagesCount;
        this.ratePerKg = ratePerKg;
    }

    public double getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(double totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getSortedPackagesCount() {
        return sortedPackagesCount;
    }

    public void setSortedPackagesCount(int sortedPackagesCount) {
        this.sortedPackagesCount = sortedPackagesCount;
    }

    public double getRatePerKg() {
        return ratePerKg;
    }

    public void setRatePerKg(double ratePerKg) {
        this.ratePerKg = ratePerKg;
    }
}
