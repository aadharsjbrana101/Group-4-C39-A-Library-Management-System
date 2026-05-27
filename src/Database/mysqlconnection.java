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
            String username = "newuser";
            String password = "1234";
            String database = "lms";
            Connection conn;
            conn = DriverManager.getConnection(
           "jdbc:mysql://127.0.0.1:3306/" + database,username,password
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
        try{
            if(conn !=null && !conn.isClosed());
            conn.close();
            System.out.println("connection is closed");  
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    @Override
    public ResultSet runQuery(Connection conn, String query) {
        try{
            Statement stmp= conn.createStatement();
            ResultSet result=stmp.executeQuery(query);
            return result;
        }catch(SQLException e){
            System.out.println(e);
        return null;
        }
    }

    @Override
    public int executeUpdate(Connection conn, String query) {
     try{
             Statement stmp= conn.createStatement();
            int result=stmp.executeUpdate(query);
            return result;
        }catch(SQLException e){
            System.out.println(e);
        return -1;
        }
    }
}
