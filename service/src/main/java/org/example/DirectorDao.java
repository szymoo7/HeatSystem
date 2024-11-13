package org.example;

import java.time.LocalDate;

public interface DirectorDao {

    void login(String login, String password);
    void registerController(Controller c);
    void registerTenant(Tenant t);
    void delegateTask(Task t, Controller c, Tenant tnt, String description, LocalDate dueDate);
    void setPricePerKwh(double price, Building b);
    void setBill(Bill b, Apartment a);
}
