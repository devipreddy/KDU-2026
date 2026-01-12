package com.example.QuickShip.dto;

public class WarehouseStats {
    private int totalPending;
    private int totalSorted;

    public WarehouseStats() {}

    public WarehouseStats(int totalPending, int totalSorted) {
        this.totalPending = totalPending;
        this.totalSorted = totalSorted;
    }

    public int getTotalPending() {
        return totalPending;
    }

    public void setTotalPending(int totalPending) {
        this.totalPending = totalPending;
    }

    public int getTotalSorted() {
        return totalSorted;
    }

    public void setTotalSorted(int totalSorted) {
        this.totalSorted = totalSorted;
    }
}
