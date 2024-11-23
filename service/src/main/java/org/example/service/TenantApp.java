package org.example.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Logger;

public class TenantApp implements TenantDao{

    private Logger logger = Logger.getLogger(TenantApp.class.getName());
    private Connection conn  = null;
    private int currentId = 0;

    static {
        try {
            String url = "jdbc:sqlite:HeatSystemDB";
            var temp = DriverManager.getConnection(url);

            String[] tableCreationQueries = {
                    "CREATE TABLE IF NOT EXISTS Administrators (" +
                            "    admin_id INTEGER PRIMARY KEY," +
                            "    name TEXT," +
                            "    surname TEXT" +
                            ");",
                    "CREATE TABLE IF NOT EXISTS Admins_Accounts (" +
                            "    account_id INTEGER PRIMARY KEY," +
                            "    login TEXT," +
                            "    password TEXT," +
                            "    status TEXT" +
                            ");",
                    "CREATE TABLE IF NOT EXISTS ControllersTasks (" +
                            "    executor_id INTEGER," +
                            "    task TEXT," +
                            "    task_description TEXT," +
                            "    task_status TEXT," +
                            "    due_date DATE," +
                            "    assignedDate DATETIME DEFAULT CURRENT_TIMESTAMP," +
                            "    FOREIGN KEY (executor_id) REFERENCES ControllersAccounts(Account_Id)" +
                            ");"
            };

            try (Statement stmt = temp.createStatement()) {
                for (String query : tableCreationQueries) {
                    stmt.execute(query);
                }
            }

            temp.close();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public TenantApp(int accountId) {
        this.currentId = accountId;
    }

    @Override
    public void login(String login, String password) {
        if(isLoggedIn()) {
            System.out.println("Already logged in.");
            return;
        }
        try {
            if(!doesExits(login)) {
                System.out.println("User does not exist.");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
                System.out.println("Logged in as " + login + " successfully.");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
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
                result.add(new Bill(billId, tenantId, amount, pricePerKwh, price, status, date, buildingNumber, apartmentNumber));

            }
            result.sort(Comparator.comparing(Bill::getDate));
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return  null;
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    @Override
    public void payBill(int id) {
        if(!isLoggedIn()) {
            System.out.println("You are not logged in.");
            return;
        }
        String sql = "UPDATE Bills SET status = 'PAID' WHERE tenant_id = ? AND status = 'UNPAID' AND id = ?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentId);
            pstmt.setInt(2, id);
            pstmt.executeUpdate();
            System.out.println("Bill paid successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void connect() throws SQLException {
        if (conn != null) {
            return;
        }
        String url = "jdbc:sqlite:HeatSystemDB";
        conn = DriverManager.getConnection(url);
        System.out.println("Connection established to HeatSystemDB.");
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
            System.out.println("Logged out successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
