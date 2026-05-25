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
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public ResultSet runQuery(Connection conn, String query) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int executeUpdate(Connection conn, String query) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

}
