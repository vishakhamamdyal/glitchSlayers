package com.payrollapplication.payroll;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class EmployeeModelAssembler {

    private static final Logger logger = Logger.getLogger(EmployeeModelAssembler.class.getName());
    private Connection connection;

    public EmployeeModelAssembler(String dbUrl, String dbUser, String dbPassword) {
        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        } catch (SQLException e) {
            logger.severe("Error connecting to database: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Object deserializeObject(byte[] serializedObject) {
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(serializedObject))) {
            return ois.readObject();
        } catch (Exception e) {
            logger.severe("Deserialization error: " + e.getMessage());
        }
        return null;
    }

    public void logExecutionTime(String query, String userId) {
        try {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                logger.info("User: " + username + " executed query: " + query);
            }
        } catch (SQLException e) {
            logger.severe("Error executing query: " + e.getMessage());
            e.printStackTrace();
        }
    }
}