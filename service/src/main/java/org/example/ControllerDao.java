package org.example;

import java.util.List;

public interface ControllerDao extends Loggable {

    List<Task> getTasks();
    void insertReading(double reading, long meterId);
}
