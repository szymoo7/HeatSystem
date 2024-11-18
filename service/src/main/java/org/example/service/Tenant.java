package org.example.service;

public record Tenant(int id, String name, String surname, String login,
                     String password, int buildingNumber, int apartmentNumber, double area) {

    public Tenant(String name, String surname, String login, String password, int buildingNumber, int apartmentNumber, double area) {
        this(0, name, surname, login, password, buildingNumber, apartmentNumber, area);
    }
}
