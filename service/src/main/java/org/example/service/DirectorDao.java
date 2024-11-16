package org.example.service;

import java.time.LocalDate;
import java.util.List;

public interface DirectorDao extends Loggable {

    void registerController(Controller c);
    void registerTenant(Tenant t);
    void delegateTask(Task t, Controller c, Tenant tnt, String description, LocalDate dueDate);
    void setPricePerKwh(double price, Building b);
    void setBill(Bill b);
    Bill calculateBill(Apartment a, double pricePerKwh, LocalDate date, BillStatus status);
    List<Apartment> getApartments();
}
