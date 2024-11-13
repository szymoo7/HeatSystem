package org.example;

import java.util.List;

public interface TenantDao {

        void login(String login, String password);
        List<Bill> getBills();
        void payBill();
}
