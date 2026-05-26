/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class userdata {
    // Sprint 2: User Login Session properties
    // Sprint 3: Forgot Password security fields
    private int user_id;
    private String username;
    private String email;
    private String password;
    private String role;     // "admin" or "user"
    private String status;   // "active" or "blocked"

    // Constructors
    public userdata(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = "user";
        this.status = "active";
    }

    public userdata(int user_id, String username, String email, String password, String role, String status) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    // Getters
    public int getId() {
        return user_id;
    }
    public String getUsername() {
        return username;
    }
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(int user_id) {
        this.user_id = user_id;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setRole(String role) {
        this.role = role;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}