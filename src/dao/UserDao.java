package dao;

import Database.mysqlconnection;
import Model.userdata;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
    private final mysqlconnection mysql = new mysqlconnection();

    public UserDao() {
        try {
            initTable();
        } catch (SQLException e) {
            System.err.println("Error initializing users table: " + e.getMessage());
        }
    }

    // Dynamic schema migration and seeding
    private void initTable() throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            return;
        }

        String createTableSQL = "CREATE TABLE IF NOT EXISTS users (" +
                "user_id INT AUTO_INCREMENT PRIMARY KEY," +
                "username VARCHAR(255) NOT NULL UNIQUE," +
                "email VARCHAR(255) NOT NULL UNIQUE," +
                "password VARCHAR(255) NOT NULL," +
                "role VARCHAR(50) DEFAULT 'user'," +
                "status VARCHAR(50) DEFAULT 'active'," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);

            // Alter columns if migrating existing table
            try {
                stmt.executeUpdate("ALTER TABLE users ADD COLUMN role VARCHAR(50) DEFAULT 'user'");
            } catch (SQLException ignored) {}

            try {
                stmt.executeUpdate("ALTER TABLE users ADD COLUMN status VARCHAR(50) DEFAULT 'active'");
            } catch (SQLException ignored) {}

            // Seed default admin and user if table is empty or has no admin
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM users WHERE role = 'admin'");
            if (rs.next() && rs.getInt(1) == 0) {
                // Seed Admin
                String seedAdmin = "INSERT INTO users (username, email, password, role, status) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(seedAdmin)) {
                    ps.setString(1, "admin");
                    ps.setString(2, "admin@lms.com");
                    ps.setString(3, "admin123");
                    ps.setString(4, "admin");
                    ps.setString(5, "active");
                    ps.executeUpdate();
                }
                
                // Seed a normal user
                String seedUser = "INSERT INTO users (username, email, password, role, status) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(seedUser)) {
                    ps.setString(1, "user");
                    ps.setString(2, "user@lms.com");
                    ps.setString(3, "user123");
                    ps.setString(4, "user");
                    ps.setString(5, "active");
                    ps.executeUpdate();
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // Sprint 2: Login validation query methods
    public userdata authenticate(String usernameOrEmail, String password) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT * FROM users WHERE (username = ? OR email = ?) AND password = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, usernameOrEmail);
            pstm.setString(2, usernameOrEmail);
            pstm.setString(3, password);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    return new userdata(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("status")
                    );
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return null;
    }

    // Keep original loginUser method for backward compatibility
    public boolean loginUser(userdata user) throws SQLException {
        userdata authUser = authenticate(user.getUsername(), user.getPassword());
        return authUser != null && "active".equalsIgnoreCase(authUser.getStatus());
    }

    // SIGNUP
    public boolean signUp(userdata user) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "INSERT INTO users (username, email, password, role, status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, user.getUsername());
            pstm.setString(2, user.getEmail());
            pstm.setString(3, user.getPassword());
            pstm.setString(4, "user");
            pstm.setString(5, "active");
            int rows = pstm.executeUpdate();
            return rows > 0;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // CHECK if user exists
    public boolean checkUser(userdata user) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT * FROM users WHERE email = ? OR username = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, user.getEmail());
            pstm.setString(2, user.getUsername());
            try (ResultSet result = pstm.executeQuery()) {
                return result.next();
            }
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // CHECK if email exists
    public boolean checkEmail(String email) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT * FROM users WHERE email = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, email);
            try (ResultSet result = pstm.executeQuery()) {
                return result.next();
            }
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // UPDATE password
    public boolean updatePassword(String email, String newPassword) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, newPassword);
            pstm.setString(2, email);
            int rows = pstm.executeUpdate();
            return rows > 0;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // GET all users for admin
    public List<userdata> getAllUsers() throws SQLException {
        List<userdata> list = new ArrayList<>();
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT * FROM users ORDER BY user_id DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new userdata(
                    rs.getInt("user_id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("role"),
                    rs.getString("status")
                ));
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return list;
    }

    // UPDATE user status (block/unblock)
    public boolean updateUserStatus(int userId, String status) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "UPDATE users SET status = ? WHERE user_id = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, status);
            pstm.setInt(2, userId);
            int rows = pstm.executeUpdate();
            return rows > 0;
        } finally {
            mysql.closeconnection(conn);
        }
    }
}