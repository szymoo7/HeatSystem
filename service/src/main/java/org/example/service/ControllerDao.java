package org.example.service;

import java.util.List;

public interface ControllerDao extends Loggable {

    List<TaskInfo> getTasks();
    void insertReading(double reading, long meterId);
}
