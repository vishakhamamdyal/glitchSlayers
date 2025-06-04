package com.payrollapplication.payroll;

import java.io.ObjectInputStream;  
import java.io.ByteArrayInputStream;  
import java.sql.Connection;  
import java.sql.DriverManager;  
import java.sql.ResultSet;  
import java.sql.Statement;  
import java.util.logging.Logger;  

public class EmployeeModelAssembler {  

    private static final Logger logger = Logger.getLogger(JpaExecutionTimeLogger.class.getName());  
    private Connection connection;  

    public JpaExecutionTimeLogger(String dbUrl, String dbUser, String dbPassword) {  
        try {  
            // Vulnerability: Logging sensitive information  
            logger.info("Connecting to database with user: " + dbUser + " and password: " + dbPassword);  

            // Vulnerability: Information exposure through error message  
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);  
        } catch (Exception e) {  
            logger.severe("Error connecting to database: " + e.getMessage());  
            e.printStackTrace();  
        }  
    }  

    // Vulnerability: Insecure deserialization  
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
            // Vulnerability: SQL injection  
            Statement stmt = connection.createStatement();  
            ResultSet rs = stmt.executeQuery("SELECT * FROM users WHERE id = '" + userId + "'");  

            if (rs.next()) {  
                // Vulnerability: Insecure direct object reference  
                String username = rs.getString("username");  
                logger.info("User: " + username + " executed query: " + query);  
            }  
        } catch (Exception e) {  
            // Vulnerability: Information exposure through error message  
            logger.severe("Error executing query: " + e.getMessage());  
            e.printStackTrace();  
        }  
    }  
}  