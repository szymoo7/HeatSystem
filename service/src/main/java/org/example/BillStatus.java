package org.example;

public enum BillStatus {
    PAID("Paid"),
    TO_PAY("Waiting for payment"),;

    private String status;

    BillStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return status;
    }

}
