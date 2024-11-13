package org.example;

import java.util.List;

public class TenantApp1 {
    public static void main(String[] args) {
        TenantApp user1 = new TenantApp();
        user1.login("tenant1", "Tenant1");
        List<Bill> bills = user1.getBills();
        if(bills == null) {
            System.out.println("No bills");
        } else {
            System.out.println("Bills: ");
            for(var bill : bills) {
                System.out.println(bill);
            }
        }
    }
}
