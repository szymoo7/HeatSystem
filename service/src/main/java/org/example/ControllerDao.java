package org.example;

import java.util.List;

public interface ControllerDao {

    void login(String login, String password);
    List<Task> getTasks();
    void insertReading(double reading, long meterId);
}
