package dao;

import Database.mysqlconnection;
import Model.Borrow;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowDao {
    private final mysqlconnection mysql = new mysqlconnection();

    public BorrowDao() {
        try {
            initTable();
        } catch (SQLException e) {
            System.err.println("Error initializing borrows table: " + e.getMessage());
        }
    }

    private void initTable() throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            return;
        }

        String createTableSQL = "CREATE TABLE IF NOT EXISTS borrows (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "user_id INT NOT NULL," +
                "book_id INT NOT NULL," +
                "borrow_date DATE NOT NULL," +
                "due_date DATE NOT NULL," +
                "return_date DATE," +
                "status VARCHAR(50) DEFAULT 'borrowed'," +
                "renew_count INT DEFAULT 0," +
                "FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE," +
                "FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE" +
                ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // Check if user already has an active borrow of this specific book
    public boolean hasActiveBorrow(int userId, int bookId) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT COUNT(*) FROM borrows WHERE user_id = ? AND book_id = ? AND status = 'borrowed'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, bookId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return false;
    }

    // Borrow Book
    public boolean borrowBook(int userId, int bookId) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        conn.setAutoCommit(false);
        try {
            // Check availability
            String checkQty = "SELECT available_quantity FROM books WHERE id = ?";
            int qty = 0;
            try (PreparedStatement ps = conn.prepareStatement(checkQty)) {
                ps.setInt(1, bookId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        qty = rs.getInt("available_quantity");
                    }
                }
            }

            if (qty <= 0) {
                conn.rollback();
                return false;
            }

            // Decrement quantity
            String decQty = "UPDATE books SET available_quantity = available_quantity - 1 WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(decQty)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            // Insert borrow record
            String insertSQL = "INSERT INTO borrows (user_id, book_id, borrow_date, due_date, status, renew_count) VALUES (?, ?, CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 'borrowed', 0)";
            try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                ps.setInt(1, userId);
                ps.setInt(2, bookId);
                ps.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            mysql.closeconnection(conn);
        }
    }

    // Sprint 2: Return updates and query functions
    public boolean returnBook(int borrowId) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        conn.setAutoCommit(false);
        try {
            // Fetch borrow details
            int bookId = -1;
            int userId = -1;
            Date dueDate = null;
            String status = "";

            String selectSQL = "SELECT book_id, user_id, due_date, status FROM borrows WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
                ps.setInt(1, borrowId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        bookId = rs.getInt("book_id");
                        userId = rs.getInt("user_id");
                        dueDate = rs.getDate("due_date");
                        status = rs.getString("status");
                    }
                }
            }

            if (bookId == -1 || !"borrowed".equals(status)) {
                conn.rollback();
                return false;
            }

            // Increment quantity
            String incQty = "UPDATE books SET available_quantity = available_quantity + 1 WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(incQty)) {
                ps.setInt(1, bookId);
                ps.executeUpdate();
            }

            // Update borrow record
            String updateBorrow = "UPDATE borrows SET return_date = CURDATE(), status = 'returned' WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateBorrow)) {
                ps.setInt(1, borrowId);
                ps.executeUpdate();
            }

            // Calculate overdue fine
            long timeDiff = System.currentTimeMillis() - dueDate.getTime();
            long daysDiff = timeDiff / (1000 * 60 * 60 * 24);
            if (daysDiff > 0) {
                double fineAmount = daysDiff * 10.0; // Rs. 10 per day
                String checkFine = "SELECT id FROM fines WHERE borrow_id = ?";
                int fineId = -1;
                try (PreparedStatement ps = conn.prepareStatement(checkFine)) {
                    ps.setInt(1, borrowId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            fineId = rs.getInt("id");
                        }
                    }
                }

                if (fineId == -1) {
                    String insertFine = "INSERT INTO fines (borrow_id, user_id, amount, status) VALUES (?, ?, ?, 'unpaid')";
                    try (PreparedStatement ps = conn.prepareStatement(insertFine)) {
                        ps.setInt(1, borrowId);
                        ps.setInt(2, userId);
                        ps.setDouble(3, fineAmount);
                        ps.executeUpdate();
                    }
                } else {
                    String updateFine = "UPDATE fines SET amount = ? WHERE id = ?";
                    try (PreparedStatement ps = conn.prepareStatement(updateFine)) {
                        ps.setDouble(1, fineAmount);
                        ps.setInt(2, fineId);
                        ps.executeUpdate();
                    }
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            throw e;
        } finally {
            conn.setAutoCommit(true);
            mysql.closeconnection(conn);
        }
    }

    // Renew Book
    public boolean renewBook(int borrowId) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        
        // Fetch borrow details
        int renewCount = 0;
        Date dueDate = null;
        String status = "";
        
        String selectSQL = "SELECT renew_count, due_date, status FROM borrows WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(selectSQL)) {
            ps.setInt(1, borrowId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    renewCount = rs.getInt("renew_count");
                    dueDate = rs.getDate("due_date");
                    status = rs.getString("status");
                }
            }
        }

        if (dueDate == null || !"borrowed".equals(status)) {
            return false;
        }

        if (renewCount >= 2) {
            // Limit of 2 renewals reached
            return false;
        }

        // Check if book is already overdue
        if (dueDate.before(new java.util.Date())) {
            // Overdue books cannot be renewed
            return false;
        }

        String updateSQL = "UPDATE borrows SET due_date = DATE_ADD(due_date, INTERVAL 14 DAY), renew_count = renew_count + 1 WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateSQL)) {
            ps.setInt(1, borrowId);
            return ps.executeUpdate() > 0;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // Get Active Borrows for a User
    public List<Borrow> getActiveBorrowsForUser(int userId) throws SQLException {
        return getBorrowsByQuery("SELECT * FROM borrows WHERE user_id = ? AND status = 'borrowed' ORDER BY due_date ASC", userId);
    }

    // Get Returned Borrows for a User
    public List<Borrow> getReturnedBorrowsForUser(int userId) throws SQLException {
        return getBorrowsByQuery("SELECT * FROM borrows WHERE user_id = ? AND status = 'returned' ORDER BY return_date DESC", userId);
    }

    // Get All Borrow History for a User
    public List<Borrow> getUserBorrowHistory(int userId) throws SQLException {
        return getBorrowsByQuery("SELECT * FROM borrows WHERE user_id = ? ORDER BY borrow_date DESC", userId);
    }

    // Get All Active Borrows across all users (for statistics)
    public int getActiveBorrowsCount() throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT COUNT(*) FROM borrows WHERE status = 'borrowed'";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return 0;
    }

    // Get Active Borrow count for a single user
    public int getActiveBorrowsCountForUser(int userId) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT COUNT(*) FROM borrows WHERE user_id = ? AND status = 'borrowed'";
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

    // Get Total Borrow count for a single user
    public int getTotalBorrowsCountForUser(int userId) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT COUNT(*) FROM borrows WHERE user_id = ?";
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

    // Helper method
    private List<Borrow> getBorrowsByQuery(String sql, int userId) throws SQLException {
        List<Borrow> list = new ArrayList<>();
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(new Borrow(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getInt("book_id"),
                        rs.getDate("borrow_date"),
                        rs.getDate("due_date"),
                        rs.getDate("return_date"),
                        rs.getString("status"),
                        rs.getInt("renew_count")
                    ));
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return list;
    }
}
