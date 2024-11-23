package org.example.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class TenantApp implements TenantDao{

    private Logger logger = Logger.getLogger(TenantApp.class.getName());
    private Connection conn  = null;
    private int currentId = 0;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public TenantApp(int accountId) {
        this.currentId = accountId;
    }

    @Override
    public void login(String login, String password) {
        String sql1 = "SELECT account_id, password FROM TenantsAccounts WHERE login = ?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            int id = rs.getInt("account_id");
            String validPassword = rs.getString("password");

            if(!validPassword.equals(password)) {
                System.out.println("Invalid login or password.");
            } else {
                String update = "UPDATE TenantsAccounts SET status = 'online' WHERE account_id = ?";
                PreparedStatement pstmt2 = conn.prepareStatement(update);
                pstmt2.setInt(1, id);
                pstmt2.executeUpdate();
                this.currentId = id;
                logger.info("Logged in as " + login + " successfully.");
            }

        } catch (SQLException e) {
            logger.severe(e.getMessage());
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                logger.severe(e.getMessage());
            }
        }
    }

    @Override
    public List<Bill> getBills() {
        String sql = "SELECT * FROM Bills WHERE tenant_id = ?";
        List<Bill> result = new ArrayList<>();
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int billId = rs.getInt("id");
                int tenantId = rs.getInt("tenant_id");
                double amount = rs.getDouble("amount");
                double pricePerKwh = rs.getDouble("price_per_kwh");
                double price = rs.getDouble("price");
                String status = rs.getString("status");
                String date = rs.getString("date");
                int buildingNumber = rs.getInt("building_number");
                int apartmentNumber = rs.getInt("apartment_number");
                result.add(new Bill(billId, tenantId, amount, price, pricePerKwh, status, date, buildingNumber, apartmentNumber));
            }
            return result;
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return  null;
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                logger.severe(e.getMessage());
            }
        }
    }

    @Override
    public void payBill(int id) {
        if(!isLoggedIn()) {
            logger.warning("You are not logged in.");
            return;
        }
        String sql = "UPDATE Bills SET status = 'PAID' WHERE tenant_id = ? AND status = 'UNPAID' AND id = ?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentId);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            logger.info("Bill paid successfully.");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                logger.severe(e.getMessage());
            }
        }
    }

    private void connect() throws SQLException {
        if (conn != null) {
            return;
        }
        String url = "jdbc:sqlite:HeatSystemDB";
        conn = DriverManager.getConnection(url);
    }

    private void disconnect() throws SQLException {
        if (conn == null) {
            return;
        }
        conn.close();
        conn = null;
    }

    private boolean isLoggedIn() {
        return currentId != 0;
    }

    private boolean doesExits(String login) throws SQLException {
        connect();
        String sql = "SELECT account_id FROM TenantsAccounts WHERE login = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, login);
        ResultSet rs = pstmt.executeQuery();
        int id = rs.getInt("account_id");
        return id != 0;
    }

    @Override
    public void logout() {
        try {
            connect();
            String update = "UPDATE TenantsAccounts SET status = 'offline' WHERE account_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setInt(1, currentId);
            pstmt.executeUpdate();
            currentId = 0;
            logger.info("Logged out successfully.");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                logger.severe(e.getMessage());
            }
        }
    }
}
