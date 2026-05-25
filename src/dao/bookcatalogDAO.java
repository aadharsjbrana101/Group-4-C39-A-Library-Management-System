package dao;

import Database.mysqlconnection;
import Model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Data Access Object for Book Catalog and Borrowing operations
 * @author Amanm
 */
public class bookcatalogDAO {
    private static final Logger logger = Logger.getLogger(bookcatalogDAO.class.getName());
    private final mysqlconnection dbConnection = new mysqlconnection();

    /**
     * Retrieves all books from the database
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching all books", e);
        }
        return books;
    }

    /**
     * Retrieves books flagged as future releases
     */
    public List<Book> getFutureReleases() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE is_future = TRUE";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching future releases", e);
        }
        return books;
    }

    /**
     * Retrieves books in the regular catalog (not future releases)
     */
    public List<Book> getRegularCatalog() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books WHERE is_future = FALSE";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                books.add(mapResultSetToBook(rs));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching regular catalog books", e);
        }
        return books;
    }

    /**
     * Retrieves books borrowed by a specific user
     */
    public List<Book> getBorrowedBooks(int userId) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT b.* FROM books b " +
                     "JOIN borrowed_books bb ON b.id = bb.book_id " +
                     "WHERE bb.user_id = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    books.add(mapResultSetToBook(rs));
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching borrowed books for user: " + userId, e);
        }
        return books;
    }

    /**
     * Borrows a book for a user
     */
    public boolean borrowBook(int userId, int bookId) {
        if (isBorrowed(userId, bookId)) {
            return false; // Already borrowed
        }
        
        String sql = "INSERT INTO borrowed_books (user_id, book_id) VALUES (?, ?)";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error borrowing book", e);
            return false;
        }
    }

    /**
     * Checks if a book is currently borrowed by a specific user
     */
    public boolean isBorrowed(int userId, int bookId) {
        String sql = "SELECT COUNT(*) FROM borrowed_books WHERE user_id = ? AND book_id = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking if book is borrowed", e);
        }
        return false;
    }

    /**
     * Returns a book borrowed by a user
     */
    public boolean returnBook(int userId, int bookId) {
        String sql = "DELETE FROM borrowed_books WHERE user_id = ? AND book_id = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error returning book", e);
            return false;
        }
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setGenre(rs.getString("genre"));
        book.setPublishedYear(rs.getInt("published_year"));
        book.setDescription(rs.getString("description"));
        book.setImagePath(rs.getString("image_path"));
        book.setFuture(rs.getBoolean("is_future"));
        return book;
    }
}
