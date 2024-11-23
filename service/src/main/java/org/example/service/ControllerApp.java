package org.example.service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class ControllerApp implements ControllerDao{

    private Logger logger = Logger.getLogger(ControllerApp.class.getName());
    private Connection conn  = null;
    private int currentId = 0;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public ControllerApp(int currentId) {
        this.currentId = currentId;
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

    public int getCurrentId() {
        return currentId;
    }

    @Override
    public void login(String login, String password) {
        String sql1 = "SELECT account_id, password FROM ControllersAccounts WHERE login = ?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql1);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            int id = rs.getInt("account_id");
            String validPassword = rs.getString("password");

            if(!validPassword.equals(password)) {
                logger.warning("Invalid login or password.");
            } else {
                String update = "UPDATE ControllersAccounts SET status = 'online' WHERE account_id = ?";
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
    public List<TaskInfo> getTasks() {
        if(currentId ==  0) {
            logger.warning("You are not logged in.");
            return null;
        }
        List<TaskInfo> result = new ArrayList<>();
        String sql = "SELECT * FROM ControllersTasks WHERE executor_id = ?";
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.currentId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                String task = rs.getString("task");
                if(task != null) {
                    String sql1 = "SELECT name, surname FROM Controllers WHERE controller_id = ?";
                    PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                    pstmt1.setInt(1, this.currentId);
                    ResultSet rs1 = pstmt1.executeQuery();
                    String name = rs1.getString("name");
                    String surname = rs1.getString("surname");
                    String executor = name + " " + surname;
                    String description = rs.getString("task_description");
                    String status = rs.getString("task_status");
                    String dueDate = rs.getString("due_date");
                    String assignedDate = rs.getString("assigned_date");
                    int buildingNumber = rs.getInt("building_number");
                    int apartmentNumber = rs.getInt("apartment_number");
                    result.add(new TaskInfo(executor, task, description, status, dueDate, assignedDate, buildingNumber, apartmentNumber));
                }
            }
            return result;
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return null;
        } finally {
            try {
                disconnect();
            } catch (SQLException e) {
                logger.severe(e.getMessage());
            }
        }
    }

    @Override
    public void insertReading(int buildingNumber, int apartmentNumber,
                              double mainMeterReading, double apartmentMeterReading, int controllerId) {
        if(currentId == 0) {
            logger.warning("You are not logged in.");
            return;
        }
        String sql = "SELECT apartment_id FROM Apartments WHERE building_number = ? AND apartment_number = ?";
        String sql1 = "INSERT INTO Measures (apartment_id, controller_id, measure) VALUES (?, ?, ?)";
        String sql2 = "UPDATE ControllersTasks SET task_status = 'DONE' WHERE executor_id = ? AND task_status = 'TODO' AND building_number = ? AND apartment_number = ?";
        String sql3 = "DELETE FROM Meters WHERE building_number = ? AND apartment_number = ?";
        String sql4 = "SELECT main_meter_id FROM Buildings WHERE building_number = ?";
        String sql5 = "DELETE FROM Main_Meters WHERE main_meter_id = ?";
        String sql6 = "INSERT INTO Meters (measure, building_number, apartment_number) VALUES (?, ?, ?)";
        String sql7 = "INSERT INTO Main_Meters (measure, building_number, main_meter_id) VALUES (?, ?, ?)";


        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, buildingNumber);
            pstmt.setInt(2, apartmentNumber);
            ResultSet rs = pstmt.executeQuery();
            int apartmentId = rs.getInt("apartment_id");
            PreparedStatement pstmt1 = conn.prepareStatement(sql1);
            pstmt1.setInt(1, apartmentId);
            pstmt1.setInt(2, controllerId);
            pstmt1.setDouble(3, apartmentMeterReading);
            pstmt1.executeUpdate();
            PreparedStatement pstmt2 = conn.prepareStatement(sql2);
            pstmt2.setInt(1, controllerId);
            pstmt2.setInt(2, buildingNumber);
            pstmt2.setInt(3, apartmentNumber);
            pstmt2.executeUpdate();
            PreparedStatement pstmt3 = conn.prepareStatement(sql3);
            pstmt3.setInt(1, buildingNumber);
            pstmt3.setInt(2, apartmentNumber);
            pstmt3.executeUpdate();
            PreparedStatement pstmt4 = conn.prepareStatement(sql4);
            pstmt4.setInt(1, buildingNumber);
            ResultSet rs1 = pstmt4.executeQuery();
            int mainMeterId = rs1.getInt("main_meter_id");
            PreparedStatement pstmt5 = conn.prepareStatement(sql5);
            pstmt5.setInt(1, mainMeterId);
            pstmt5.executeUpdate();
            PreparedStatement pstmt6 = conn.prepareStatement(sql6);
            pstmt6.setDouble(1, apartmentMeterReading);
            pstmt6.setInt(2, buildingNumber);
            pstmt6.setInt(3, apartmentNumber);
            pstmt6.executeUpdate();
            PreparedStatement pstmt7 = conn.prepareStatement(sql7);
            pstmt7.setDouble(1, mainMeterReading);
            pstmt7.setInt(2, buildingNumber);
            pstmt7.setInt(3, mainMeterId);
            pstmt7.executeUpdate();
            logger.info("Reading inserted successfully.");
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

    private boolean doesExits(String login) throws SQLException {
        connect();
        String sql = "SELECT account_id FROM ControllersAccounts WHERE login = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, login);
        ResultSet rs = pstmt.executeQuery();
        int id = rs.getInt("account_id");
        return id != 0;
    }

    private boolean isLogged(String login) {
        String sql = "SELECT status FROM ControllersAccounts WHERE login = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            return rs.getString("status").equals("online");
        } catch (SQLException e) {
            logger.severe(e.getMessage());
            return false;
        }
    }

    @Override
    public void logout() {
        try {
            connect();
            String update = "UPDATE ControllersAccounts SET status = 'offline' WHERE account_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(update);
            pstmt.setInt(1, this.currentId);
            pstmt.executeUpdate();
            this.currentId = 0;
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
