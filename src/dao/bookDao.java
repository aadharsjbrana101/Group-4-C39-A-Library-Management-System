package dao;

import Model.bookdata;
import Database.mysqlconnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class bookDao {

    mysqlconnection mysql = new mysqlconnection();

    // ── ADD a new book ──────────────────────────────────────────────
    public boolean addBook(bookdata book) {
        Connection conn = mysql.openconnection();
        String sql = "INSERT INTO books (title, author, genre, total_copies, available_copies, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, book.getTitle());
            pstm.setString(2, book.getAuthor());
            pstm.setString(3, book.getGenre());
            pstm.setInt(4, book.getTotalCopies());
            pstm.setInt(5, book.getAvailableCopies());
            pstm.setString(6, book.getStatus());
            pstm.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // ── UPDATE a book ───────────────────────────────────────────────
    public boolean updateBook(bookdata book) {
        Connection conn = mysql.openconnection();
        String sql = "UPDATE books SET title=?, author=?, genre=?, "
                   + "total_copies=?, available_copies=?, status=? WHERE book_id=?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, book.getTitle());
            pstm.setString(2, book.getAuthor());
            pstm.setString(3, book.getGenre());
            pstm.setInt(4, book.getTotalCopies());
            pstm.setInt(5, book.getAvailableCopies());
            pstm.setString(6, book.getStatus());
            pstm.setInt(7, book.getBookId());
            pstm.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // ── DELETE a book ───────────────────────────────────────────────
    public boolean deleteBook(int bookId) {
        Connection conn = mysql.openconnection();
        String sql = "DELETE FROM books WHERE book_id=?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setInt(1, bookId);
            pstm.executeUpdate();
            return true;
        } catch (Exception e) {
            System.out.println(e);
            return false;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // ── GET ALL books ───────────────────────────────────────────────
    public List<bookdata> getAllBooks() {
        Connection conn = mysql.openconnection();
        List<bookdata> bookList = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                bookdata book = new bookdata(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("total_copies"),
                    rs.getInt("available_copies"),
                    rs.getString("status")
                );
                bookList.add(book);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeconnection(conn);
        }
        return bookList;
    }

    // ── GET TOTAL book count (for dashboard card) ───────────────────
    public int getTotalBookCount() {
        Connection conn = mysql.openconnection();
        String sql = "SELECT COUNT(*) FROM books";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeconnection(conn);
        }
        return 0;
    }

    // ── GET ISSUED book count (for dashboard card) ──────────────────
    public int getIssuedBookCount() {
        Connection conn = mysql.openconnection();
        String sql = "SELECT COUNT(*) FROM books WHERE status='Issued'";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeconnection(conn);
        }
        return 0;
    }

    // ── SEARCH books by title or author ────────────────────────────
    public List<bookdata> searchBooks(String keyword) {
        Connection conn = mysql.openconnection();
        List<bookdata> bookList = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ?";
        try (PreparedStatement pstm = conn.prepareStatement(sql)) {
            pstm.setString(1, "%" + keyword + "%");
            pstm.setString(2, "%" + keyword + "%");
            ResultSet rs = pstm.executeQuery();
            while (rs.next()) {
                bookdata book = new bookdata(
                    rs.getInt("book_id"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("total_copies"),
                    rs.getInt("available_copies"),
                    rs.getString("status")
                );
                bookList.add(book);
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            mysql.closeconnection(conn);
        }
        return bookList;
    }
}