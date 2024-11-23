package org.example.service;

public record Controller(int id, String name, String surname, String login, String password) {
    public Controller(String name, String surname, String login, String password) {
        this(0, name, surname, login, password);
    }

    public Controller(int id, String name, String surname) {
        this(id, name, surname, null, null);
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}
