/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import Database.mysqlconnection;
import Model.userdata;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class UserDao {
    mysqlconnection mysql = new mysqlconnection();

    public void createUser(userdata user) { 
        Connection conn = mysql.openconnection();
        String sql = "INSERT INTO users (username, email, password) VALUES (?,?,?)";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {  
            pstm.setString(1, user.getUsername());   
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getPassword());
            pstm.executeUpdate();                    
        } catch (Exception e) {
            System.out.print(e);                     
        } finally {
            mysql.closeconnection(conn);
        }
    }  
}    