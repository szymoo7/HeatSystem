package org.example.service;

public record Tenant(int id, String name, String surname, String login, String password) {

    public Tenant(String name, String surname, String login, String password) {
        this(0, name, surname, login, password);
    }
}
