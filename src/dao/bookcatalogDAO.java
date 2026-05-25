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

    /**
     * Authenticates a user.
     * Returns the user ID if successful, -999 for programmatically authenticated admin, and -1 if failed.
     */
    public int authenticateUser(String username, String password) {
        if ("admin".equalsIgnoreCase(username) && "admin".equals(password)) {
            return -999; // Special Admin ID
        }
        String sql = "SELECT id FROM users WHERE username = ? AND password = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error authenticating user", e);
        }
        return -1; // Authentication failed
    }

    /**
     * Registers a new user.
     * Returns true if registration succeeded, false if user already exists or error.
     */
    public boolean registerUser(String username, String password, String email) {
        // First check if user exists
        String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(checkSql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return false; // User already exists
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error checking user existence", e);
            return false;
        }

        String sql = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, email);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error registering user", e);
            return false;
        }
    }

    /**
     * Adds a new book to the database.
     */
    public boolean addBook(Book book) {
        String sql = "INSERT INTO books (title, author, genre, published_year, description, image_path, is_future) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getPublishedYear());
            stmt.setString(5, book.getDescription());
            stmt.setString(6, book.getImagePath());
            stmt.setBoolean(7, book.isFuture());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error adding book", e);
            return false;
        }
    }

    /**
     * Updates an existing book in the database.
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, genre = ?, published_year = ?, description = ?, image_path = ?, is_future = ? WHERE id = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, book.getTitle());
            stmt.setString(2, book.getAuthor());
            stmt.setString(3, book.getGenre());
            stmt.setInt(4, book.getPublishedYear());
            stmt.setString(5, book.getDescription());
            stmt.setString(6, book.getImagePath());
            stmt.setBoolean(7, book.isFuture());
            stmt.setInt(8, book.getId());
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error updating book", e);
            return false;
        }
    }

    /**
     * Deletes a book from the database.
     */
    public boolean deleteBook(int bookId) {
        String sql = "DELETE FROM books WHERE id = ?";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, bookId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error deleting book", e);
            return false;
        }
    }

    /**
     * Fetches all borrowed books with user information for administrative monitoring.
     */
    public List<String[]> getAllBorrowedBooksAdmin() {
        List<String[]> borrowedList = new ArrayList<>();
        String sql = "SELECT u.username, u.email, b.title, bb.borrow_date " +
                     "FROM borrowed_books bb " +
                     "JOIN users u ON bb.user_id = u.id " +
                     "JOIN books b ON bb.book_id = b.id " +
                     "ORDER BY bb.borrow_date DESC";
        try (Connection conn = dbConnection.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                borrowedList.add(new String[]{
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("title"),
                    rs.getString("borrow_date")
                });
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Error fetching borrowed books for admin", e);
        }
        return borrowedList;
    }
}
