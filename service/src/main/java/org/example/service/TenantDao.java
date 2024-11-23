package org.example.service;

import java.util.List;

public interface TenantDao extends Loggable {

        List<Bill> getBills();
        void payBill(int id);
}
