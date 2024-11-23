package org.example.service;

import java.util.List;

public interface DirectorDao extends Loggable {

    void registerController(Controller c);
    void registerTenant(Tenant t);
    void delegateTask(String task, Controller c, int buildingNumber, int apartmentNumber, String description, String dueDate);
    void setPricePerKwh(double price, Building b);
    void setBill(Bill b);
    Bill calculateBill(Apartment a, double pricePerKwh, String date, String status);
    List<Apartment> getApartments();
    List<TaskInfo> getTasks();
    List<Building> getBuildings();
    List<Controller> getControllers();
}
