package org.example;

public record Apartment(int buildingNumber, int apartmentNumber, int tenantId, double area) {

    public Apartment(int buildingNumber, int apartmentNumber, double area) {
        this(buildingNumber, apartmentNumber, 0, area);
    }
}
