package org.example.service;

public class Bill {
    private final int id;
    private final int tenantId;
    private final double amount;
    private final double price;
    private final double pricePerKwh;
    private final String status;
    private final String date;
    private final int buildingNumber;
    private final int apartmentNumber;

    public Bill(int id, int tenantId, double amount, double price, double pricePerKwh, String status, String date, int buildingNumber, int apartmentNumber) {
        this.id = id;
        this.tenantId = tenantId;
        this.amount = amount;
        this.price = price;
        this.pricePerKwh = pricePerKwh;
        this.status = status;
        this.date = date;
        this.buildingNumber = buildingNumber;
        this.apartmentNumber = apartmentNumber;
    }

    public Bill(int amount, String status, String date, int buildingNumber, int apartmentNumber) {
        this(0, 0, amount, 0, 0, status, date, buildingNumber, apartmentNumber);
    }

    public Bill(int tenantId, double amount, double price, double pricePerKwh, String status, String date, int buildingNumber, int apartmentNumber) {
        this(0, tenantId, amount, price, pricePerKwh, status, date, buildingNumber, apartmentNumber);
    }

    public int getId() {
        return id;
    }

    public int getTenantId() {
        return tenantId;
    }

    public double getAmount() {
        return amount;
    }

    public double getPrice() {
        return price;
    }

    public double getPricePerKwh() {
        return pricePerKwh;
    }

    public String getStatus() {
        return status;
    }

    public String getDate() {
        return date;
    }

    public int getBuildingNumber() {
        return buildingNumber;
    }

    public int getApartmentNumber() {
        return apartmentNumber;
    }
}
