package dao;

import Database.mysqlconnection;
import Model.AdminLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminLogDao {
    private final mysqlconnection mysql = new mysqlconnection();

    public AdminLogDao() {
        try {
            initTable();
        } catch (SQLException e) {
            System.err.println("Error initializing admin_logs table: " + e.getMessage());
        }
    }

    private void initTable() throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            return;
        }

        String createTableSQL = "CREATE TABLE IF NOT EXISTS admin_logs (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "admin_id INT NOT NULL," +
                "action VARCHAR(255) NOT NULL," +
                "action_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                "FOREIGN KEY (admin_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // Log administrative action
    public boolean logAction(int adminId, String action) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "INSERT INTO admin_logs (admin_id, action) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, adminId);
            ps.setString(2, action);
            return ps.executeUpdate() > 0;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // Retrieve all logs (most recent first)
    public List<AdminLog> getAllLogs() throws SQLException {
        List<AdminLog> list = new ArrayList<>();
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT * FROM admin_logs ORDER BY action_date DESC LIMIT 50";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new AdminLog(
                    rs.getInt("id"),
                    rs.getInt("admin_id"),
                    rs.getString("action"),
                    rs.getTimestamp("action_date")
                ));
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return list;
    }
}
