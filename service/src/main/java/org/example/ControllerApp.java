package org.example;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        try {
            String url = "jdbc:sqlite:HeatSystemDB";
            var temp = DriverManager.getConnection(url);

            String[] tableCreationQueries = {
                    "CREATE TABLE IF NOT EXISTS ControllerAccounts (" +
                            "    Account_Id INTEGER PRIMARY KEY AUTOINCREMENT," +
                            "    Login TEXT UNIQUE," +
                            "    Password TEXT," +
                            "    Status TEXT" +
                            ");",
                    "CREATE TABLE IF NOT EXISTS Controllers (" +
                            "    Controller_Id INTEGER," +
                            "    Name TEXT," +
                            "    Surname TEXT," +
                            "    FOREIGN KEY (Controller_Id) REFERENCES ControllerAccounts(Account_Id)" +
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

    @Override
    public void login(String login, String password) {
        if(this.currentId != 0) {
            System.out.println("You are already logged in.");
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
        String sql1 = "SELECT account_id, password FROM ControllersAccounts WHERE login = ?";
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
                String update = "UPDATE ControllersAccounts SET status = 'online' WHERE account_id = ?";
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
    public List<Task> getTasks() {
        return null;
    }

    public Map<Task, String> getTaskMap() {
        if(currentId ==  0) {
            System.out.println("You are not logged in.");
            return null;
        }
        String sql = "SELECT task, task_description FROM ControllersTasks WHERE executor_id = ?";
        Map<Task, String> results = new HashMap<>();
        try {
            connect();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, this.currentId);
            ResultSet rs = pstmt.executeQuery();
            while(rs.next()) {
                String task = rs.getString("task");
                String description = rs.getString("task_description");
                if(task == null) {
                    continue;
                }
                Task todo = Task.valueOf(task.toUpperCase());
                results.put(todo, description);
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
        return results.isEmpty() ? null : results;
    }

    @Override
    public void insertReading(double reading, long meterId) {
        if(currentId == 0) {
            System.out.println("You are not logged in.");
            return;
        }

        String sql = "INSERT INTO MetersReadings (meter_id, reading, reading_date) VALUES (?, ?, CURRENT_TIMESTAMP)";




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
}
