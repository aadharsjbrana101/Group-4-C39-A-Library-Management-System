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
               try{
            String username = "root";
            String password = "Aman@107";
            String database = "lms";
            Connection conn;
            conn = DriverManager.getConnection(
            "jdbc:mysql://localhost:3306/" + database,username,password
            );
            if(conn != null){
                System.out.print("Connection successfull");
            }else{
                System.out.print("Connetion not successfull");
            }return conn;
        }catch (SQLException e){
            System.out.print(e);
            return null;
        }
    }
    

    @Override
    public void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.print("Connection closed successfully");
            }
        } catch (SQLException e) {
            System.out.print(e);
        }
    }

    @Override
    public ResultSet runQuery(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(query);
        } catch (SQLException e) {
            System.out.print(e);
            return null;
        }
    }

    @Override
    public int executeUpdate(Connection conn, String query) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeUpdate(query);
        } catch (SQLException e) {
            System.out.print(e);
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
