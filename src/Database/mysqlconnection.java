/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;
/**
 *
 * @author aadha
 */
public class mysqlconnection implements Db{

    @Override
    public Connection openConnection() {
        try {
            String username = "root";
            String password = "1234@";
            String database = "lms";
            
            // Ensure the JDBC driver is loaded
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                } catch (ClassNotFoundException ex) {
                    System.out.println("MySQL Driver not found in classpath.");
                }
            }

            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + database, username, password
            );
            if (conn != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection not successful.");
            }
            return conn;
        } catch (SQLException e) {
            System.out.println("Database Connection Failed: " + e.getMessage());
            return null;
        }
    }
    

    @Override
    public void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed successfully.");
            }
        } catch (SQLException e) {
            System.out.println("Error closing connection: " + e.getMessage());
        }
    }

    @Override
    public ResultSet runQuery(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.println("Error running query: " + e.getMessage());
            return null;
        }
    }

    @Override
    public int executeUpdate(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.println("Error executing update: " + e.getMessage());
            return -1;
        }
    }

    public Connection openconnection() {
        return openConnection();
    }

    public void closeconnection(Connection conn) {
        closeConnection(conn);
    }

}
