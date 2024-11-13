package org.example;

import java.time.LocalDate;

public class DirectorApp1 {
    public static void main(String[] args) {
        DirectorApp admin1 = new DirectorApp();
        admin1.login("admin1", "ADMIN");
        Tenant testTenant = new Tenant("Mateusz", "Głuchowski", "mati", "mefedron");
        LocalDate dueDate = LocalDate.parse("2024-01-01");
        Bill testBill = new Bill(1, 7, 1020, 123456, 18340, BillStatus.TO_PAY, dueDate, 3, 2);
        admin1.registerTenant(testTenant);
        Apartment testApartment = new Apartment(15, 607, 1);
        admin1.setBill(testBill, testApartment);
        Controller testController = new Controller( "Jan", "Kowalski", "jan2", "jan123");
        admin1.registerController(testController);
        Task ttest = Task.CHECK_TEMPERATURE;
        admin1.delegateTask(ttest, testController, testTenant, ttest.toString(), dueDate);

    }
}
