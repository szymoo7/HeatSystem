package org.example.service;

import java.util.List;

public interface ControllerDao extends Loggable {

    List<TaskInfo> getTasks();
    void insertReading(int buildingNumber, int apartmentNumber,
                  double mainMeterReading, double apartmentMeterReading, int controllerId);
}
