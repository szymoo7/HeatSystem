package org.example.service;

public record Building(int buildingNumber, int adminId, String address, int mainMeterId, double pricePerKwh) {

    public Building(int buildingNumber,double pricePerKwh) {
        this(buildingNumber, 0, null, 0, pricePerKwh);
    }
}
