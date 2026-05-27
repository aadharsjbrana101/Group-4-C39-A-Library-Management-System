package dao;

import Database.mysqlconnection;
import Model.FineModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class FineDao {

    mysqlconnection mysql = new mysqlconnection();

    // Get all fines for a specific user
    public List<FineModel> getFinesByUserId(int userId) {
        List<FineModel> fines = new ArrayList<>();
        Connection conn = mysql.openConnection();
        String sql = "SELECT * FROM fines WHERE user_id = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                FineModel fine = new FineModel();
                fine.setFineId(rs.getInt("fine_id"));
                fine.setUserId(rs.getInt("user_id"));
                fine.setBookTitle(rs.getString("book_title"));
                fine.setFineType(rs.getString("fine_type"));
                fine.setAmount(rs.getDouble("amount"));
                fine.setStatus(rs.getString("status"));
                fine.setDueDate(rs.getString("due_date"));
                fines.add(fine);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
        return fines;
    }

    // Get total fine amount for a user
    public double getTotalFines(int userId) {
        double total = 0;
        Connection conn = mysql.openConnection();
        String sql = "SELECT SUM(amount) AS total FROM fines WHERE user_id = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
        return total;
    }

    // Get total PENDING fine amount for a user
    public double getPendingFines(int userId) {
        double pending = 0;
        Connection conn = mysql.openConnection();
        String sql = "SELECT SUM(amount) AS pending FROM fines WHERE user_id = ? AND status = 'Pending'";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                pending = rs.getDouble("pending");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
        return pending;
    }

    // Get total PAID fine amount for a user
    public double getPaidFines(int userId) {
        double paid = 0;
        Connection conn = mysql.openConnection();
        String sql = "SELECT SUM(amount) AS paid FROM fines WHERE user_id = ? AND status = 'Paid'";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, userId);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                paid = rs.getDouble("paid");
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
        return paid;
    }

    // Mark a fine as Paid
    public void markFineAsPaid(int fineId) {
        Connection conn = mysql.openConnection();
        String sql = "UPDATE fines SET status = 'Paid' WHERE fine_id = ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, fineId);
            pstm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
    }

    // Insert a new fine record
    public void createFine(FineModel fine) {
        Connection conn = mysql.openConnection();
        String sql = "INSERT INTO fines (user_id, book_title, fine_type, amount, status, due_date) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, fine.getUserId());
            pstm.setString(2, fine.getBookTitle());
            pstm.setString(3, fine.getFineType());
            pstm.setDouble(4, fine.getAmount());
            pstm.setString(5, fine.getStatus());
            pstm.setString(6, fine.getDueDate());
            pstm.executeUpdate();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeConnection(conn);
        }
    }
}