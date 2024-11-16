package org.example.service;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DirectorApp implements DirectorDao{

    private Logger logger = Logger.getLogger(DirectorApp.class.getName());
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
                            "    assigned_date DATETIME DEFAULT CURRENT_TIMESTAMP," +
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


    public DirectorApp() {
    }
    public DirectorApp(int currentId) {
        this.currentId = currentId;
    }


    @Override
    public void login(String login, String password) {
        if(isLoggedIn()) {
            System.out.println("You are already logged in.");
            return;
        }
        if(!doesExist(login)) {
            System.out.println("User does not exist.");
            return;
        }
        String sql1 = "SELECT account_id, password FROM AdminsAccounts WHERE login = ?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            int id = rs.getInt("account_id");
            String validPassword = rs.getString("password");

            if(validPassword.equals(password)) {
                String update = "UPDATE AdminsAccounts SET status = 'online' WHERE account_id = ?";
                PreparedStatement pstmt2 = conn.prepareStatement(update);
                pstmt2.setInt(1, id);
                pstmt2.executeUpdate();
                this.currentId = id;
                System.out.println("Logged in as " + login + " successfully.");
            } else {
                System.out.println("Invalid login or password.");
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
    public void registerController(Controller c) {
        if(!isLoggedIn()) {
            System.out.println("You must be logged in to register a controller.");
            return;
        }
        if(doesControllerExist(c)) {
            System.out.println("Controller already exists.");
            return;
        }
        try {
            connect();
            String sql = "INSERT INTO ControllersAccounts(login, password, status) VALUES(?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, c.login());
            pstmt.setString(2, c.password());
            pstmt.setString(3, "offline");
            pstmt.executeUpdate();
            String sql2 = "SELECT account_id FROM ControllersAccounts WHERE login = ?";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setString(1, c.login());
            ResultSet rs = pstmt2.executeQuery();
            int id = rs.getInt("account_id");
            String sql3 = "INSERT INTO Controllers(name, surname, controller_id) VALUES(?, ?, ?)";
            PreparedStatement pstmt3 = conn.prepareStatement(sql3);
            pstmt3.setString(1, c.name());
            pstmt3.setString(2, c.surname());
            pstmt3.setInt(3, id);
            pstmt3.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void delegateTask(Task t, Controller c, Tenant tnt, String description, LocalDate dueDate) {
        if(!isLoggedIn()) {
            System.out.println("You must be logged in to delegate a task.");
            return;
        }
        try {
            connect();
            String sql = "INSERT INTO ControllersTasks(executor_id, task, task_description," +
                    " task_status, due_date) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, c.id());
            pstmt.setString(2, t.toString());
            pstmt.setString(3, description);
            pstmt.setString(4, "TODO");
            pstmt.setString(5, dueDate.toString());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setPricePerKwh(double price, Building b) {
        if(!isLoggedIn()) {
            System.out.println("You must be logged in to set a price.");
            return;
        }
        if(!doesExist(b)) {
            System.out.println("Building does not exist in the database.");
            return;
        }
        try {
            connect();
            String sql = "UPDATE Buildings SET price_per_kwh = ? WHERE building_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setDouble(1, price);
            pstmt.setInt(2, b.buildingNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setBill(Bill b) {
        if(!isLoggedIn()) {
            System.out.println("You must be logged in to set a bill.");
            return;
        }
        try {
            connect();
            String sql = "INSERT INTO Bills(tenant_id, amount, price, status," +
                    " price_per_kwh, date, building_number, apartment_number) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, b.tenantId());
            pstmt.setDouble(2, b.amount());
            pstmt.setDouble(3, b.price());
            pstmt.setString(4, b.status().toString());
            pstmt.setDouble(5, b.pricePerKwh());
            pstmt.setString(6, b.date().toString());
            pstmt.setInt(7, b.buildingNumber());
            pstmt.setInt(8, b.apartmentNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void registerTenant(Tenant t) {
        if(!isLoggedIn()) {
            System.out.println("You must be logged in to register a tenant.");
            return;
        }
        if(doesTenantExist(t)) {
            System.out.println("Tenant already exists.");
            return;
        }
        try {
            connect();
            String sql = "INSERT INTO TenantsAccounts(login, password, status) VALUES(?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, t.login());
            pstmt.setString(2, t.password());
            pstmt.setString(3, "offline");
            pstmt.executeUpdate();
            String sql2 = "SELECT account_id FROM TenantsAccounts WHERE login = ?";
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setString(1, t.login());
            ResultSet rs = pstmt2.executeQuery();
            int id = rs.getInt("account_id");
            String sql3 = "INSERT INTO Tenants(name, surname, tenant_id) VALUES(?, ?, ?)";
            PreparedStatement pstmt3 = conn.prepareStatement(sql3);
            pstmt3.setString(1, t.name());
            pstmt3.setString(2, t.surname());
            pstmt3.setInt(3, id);
            pstmt3.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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

    private boolean doesExist(String login) {
        try {
            connect();
            String sql = "SELECT account_id FROM AdminsAccounts WHERE login = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            int id = rs.getInt("account_id");
            return id != 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private boolean doesTenantExist(Tenant t) {
        try {
            connect();
            String sql = "SELECT account_id FROM TenantsAccounts WHERE login = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, t.login());
            ResultSet rs = pstmt.executeQuery();
            int id = rs.getInt("account_id");
            return id != 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean doesControllerExist(Controller c) {
        try {
            connect();
            String sql = "SELECT account_id FROM ControllersAccounts WHERE login = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, c.login());
            ResultSet rs = pstmt.executeQuery();
            int id = rs.getInt("account_id");
            return id != 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isLoggedIn() {
        return currentId != 0;
    }

    private boolean doesExist(Building b) {
        try {
            connect();
            String sql = "SELECT building_number FROM Buildings WHERE building_number = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, b.buildingNumber());
            ResultSet rs = pstmt.executeQuery();
            int id = rs.getInt("building_number");
            return id != 0;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public Bill calculateBill(Apartment a, double pricePerKwh, LocalDate date, BillStatus status) {
        double area = a.getArea();
        int buildingNumber = a.getBuildingNumber();
        String sql = "SELECT m.measure AS meter_measure FROM Meters m WHERE m.apartment_number = ? AND m.building_number = ?" +
                "UNION SELECT mm.measure AS main_meter_measure FROM Main_Meters mm WHERE mm.building_number = ?"
                + "UNION SELECT SUM(area) FROM Apartments AS total_area WHERE building_number = ?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, a.getApartmentNumber());
            pstmt.setInt(2, buildingNumber);
            pstmt.setInt(3, buildingNumber);
            ResultSet rs = pstmt.executeQuery();
            double apartmentUsage = rs.getDouble("meter_measure");
            double buildingUsage = rs.getDouble("main_meter_measure");
            double totalArea = rs.getDouble("total_area");
            double weight = area / totalArea;
            double toPay = apartmentUsage * pricePerKwh + (weight * buildingUsage * pricePerKwh);

            return new Bill(a.getTenantId(), apartmentUsage,toPay, pricePerKwh,
                    status, date, buildingNumber, a.getApartmentNumber());

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<Apartment> getApartments() {
        String sql = "SELECT a.building_number, a.apartment_number, a.tenant_id, a.area, t.name, t.surname " +
             "FROM Apartments a JOIN Tenants t ON a.tenant_id = t.tenant_id";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            List<Apartment> result = new ArrayList<>();
            while(rs.next()) {
                int buildingNumber = rs.getInt("building_number");
                int apartmentNumber = rs.getInt("apartment_number");
                int tenantId = rs.getInt("tenant_id");
                double area = rs.getDouble("area");
                String tenant = rs.getString("name") + " " + rs.getString("surname");
                result.add(new Apartment(buildingNumber, apartmentNumber, tenantId, area, tenant));
            }
            return result;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
