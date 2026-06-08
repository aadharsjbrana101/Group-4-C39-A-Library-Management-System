package dao;

import Database.mysqlconnection;
import Model.Fine;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FineDao {
    private final mysqlconnection mysql = new mysqlconnection();

    public FineDao() {
        try {
            initTable();
        } catch (SQLException e) {
            System.err.println("Error initializing fines table: " + e.getMessage());
        }
    }

    private void initTable() throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            return;
        }

        String createTableSQL = "CREATE TABLE IF NOT EXISTS fines (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "borrow_id INT NOT NULL," +
                "user_id INT NOT NULL," +
                "amount DECIMAL(10,2) DEFAULT 0.0," +
                "status VARCHAR(50) DEFAULT 'unpaid'," +
                "payment_date DATE," +
                "FOREIGN KEY (borrow_id) REFERENCES borrows(id) ON DELETE CASCADE," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE" +
                ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // Calculate fines dynamically for all active, overdue borrows
    public void calculateFines() throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            return;
        }
        
        // Find all active borrows that are overdue
        String query = "SELECT id, user_id, due_date FROM borrows WHERE status = 'borrowed' AND due_date < CURDATE()";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                int borrowId = rs.getInt("id");
                int userId = rs.getInt("user_id");
                Date dueDate = rs.getDate("due_date");
                
                long timeDiff = System.currentTimeMillis() - dueDate.getTime();
                long daysDiff = timeDiff / (1000 * 60 * 60 * 24);
                if (daysDiff > 0) {
                    double fineAmount = daysDiff * 10.0; // Rs. 10 per day
                    
                    // Check if fine record already exists
                    String checkSQL = "SELECT id, amount, status FROM fines WHERE borrow_id = ?";
                    try (PreparedStatement checkPs = conn.prepareStatement(checkSQL)) {
                        checkPs.setInt(1, borrowId);
                        try (ResultSet checkRs = checkPs.executeQuery()) {
                            if (checkRs.next()) {
                                int fineId = checkRs.getInt("id");
                                String status = checkRs.getString("status");
                                if ("unpaid".equalsIgnoreCase(status)) {
                                    // Update amount if still unpaid
                                    String updateSQL = "UPDATE fines SET amount = ? WHERE id = ?";
                                    try (PreparedStatement updatePs = conn.prepareStatement(updateSQL)) {
                                        updatePs.setDouble(1, fineAmount);
                                        updatePs.setInt(2, fineId);
                                        updatePs.executeUpdate();
                                    }
                                }
                            } else {
                                // Insert new unpaid fine
                                String insertSQL = "INSERT INTO fines (borrow_id, user_id, amount, status) VALUES (?, ?, ?, 'unpaid')";
                                try (PreparedStatement insertPs = conn.prepareStatement(insertSQL)) {
                                    insertPs.setInt(1, borrowId);
                                    insertPs.setInt(2, userId);
                                    insertPs.setDouble(3, fineAmount);
                                    insertPs.executeUpdate();
                                }
                            }
                        }
                    }
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // Sprint 2: Fine status updates and transactions
    public boolean payFine(int fineId) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "UPDATE fines SET status = 'paid', payment_date = CURDATE() WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, fineId);
            return ps.executeUpdate() > 0;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // Sprint 3: Fines query by user id
    public int getUnpaidFinesCountForUser(int userId) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT COUNT(*) FROM fines WHERE user_id = ? AND status = 'unpaid'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return 0;
    }

    // Get unpaid fines sum for user
    public double getUnpaidFinesSumForUser(int userId) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT SUM(amount) FROM fines WHERE user_id = ? AND status = 'unpaid'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return 0.0;
    }

    // Get total fines paid for a user
    public double getPaidFinesSumForUser(int userId) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT SUM(amount) FROM fines WHERE user_id = ? AND status = 'paid'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble(1);
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return 0.0;
    }

    // Get total fines received (for Admin dashboard stats)
    public double getTotalFinesReceived() throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT SUM(amount) FROM fines WHERE status = 'paid'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return 0.0;
    }

    // Sprint 4: Outstanding overdue accounts calculations
    public double getTotalOutstandingFines() throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT SUM(amount) FROM fines WHERE status = 'unpaid'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return 0.0;
    }

    // Get Fines for user
    public List<Fine> getFinesForUser(int userId) throws SQLException {
        List<Fine> list = new ArrayList<>();
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT * FROM fines WHERE user_id = ? ORDER BY id DESC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Fine(
                        rs.getInt("id"),
                        rs.getInt("borrow_id"),
                        rs.getInt("user_id"),
                        rs.getDouble("amount"),
                        rs.getString("status"),
                        rs.getDate("payment_date")
                    ));
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return list;
    }

    // Get All Transactions / Fines History (for Admin Payments)
    public List<Fine> getAllFines() throws SQLException {
        List<Fine> list = new ArrayList<>();
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT * FROM fines ORDER BY id DESC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Fine(
                    rs.getInt("id"),
                    rs.getInt("borrow_id"),
                    rs.getInt("user_id"),
                    rs.getDouble("amount"),
                    rs.getString("status"),
                    rs.getDate("payment_date")
                ));
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return list;
    }
}
