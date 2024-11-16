package org.example.service;

public class Apartment {
    private final int buildingNumber;
    private final int apartmentNumber;
    private final int tenantId;
    private final double area;
    private String tenant;

    public Apartment(int buildingNumber, int apartmentNumber, int tenantId, double area, String tenant) {
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
        this.tenantId = tenantId;
        this.area = area;
        this.tenant = tenant;
    }

    public Apartment(int buildingNumber, int apartmentNumber, double area) {
        this(buildingNumber, apartmentNumber, 0, area,null);
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }

    public int getTenantId() {
        return tenantId;
    }

    public double getArea() {
        return area;
    }

    public String getTenant() {
        return tenantId == 0 ? "Not occupied" : "Occupied";
    }
}
