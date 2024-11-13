package org.example;

public record Controller(int id, String name, String surname, String login, String password) {
    public Controller(String name, String surname, String login, String password) {
        this(0, name, surname, login, password);
    }
}
