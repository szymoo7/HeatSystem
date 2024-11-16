package org.example.service;

import java.time.LocalDate;

public record Bill(int id, int tenantId, double amount, double price, double pricePerKwh, BillStatus status, LocalDate date, int buildingNumber, int apartmentNumber) {

    public Bill(int amount, BillStatus status, LocalDate date, int buildingNumber, int apartmentNumber) {
        this(0, 0, amount, 0, 0, status, date, buildingNumber, apartmentNumber);
    }

    public Bill(int tenantId, double amount, double price, double pricePerKwh, BillStatus status, LocalDate date,
                int buildingNumber, int apartmentNumber) {
        this(0, tenantId, amount, price, pricePerKwh, status, date, buildingNumber, apartmentNumber);
    }
}
