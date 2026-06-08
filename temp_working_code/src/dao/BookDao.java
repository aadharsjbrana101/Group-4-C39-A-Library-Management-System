package dao;

import Database.mysqlconnection;
import Model.Book;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDao {
    private final mysqlconnection mysql = new mysqlconnection();

    public BookDao() {
        try {
            initTable();
        } catch (SQLException e) {
            System.err.println("Error initializing books table: " + e.getMessage());
        }
    }

    // Auto-create table and seed if empty
    private void initTable() throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            return;
        }

        String createTableSQL = "CREATE TABLE IF NOT EXISTS books (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "title VARCHAR(255) NOT NULL," +
                "author VARCHAR(255)," +
                "genre VARCHAR(100)," +
                "year INT," +
                "image_path VARCHAR(255)," +
                "is_future_release BOOLEAN," +
                "isbn VARCHAR(50) DEFAULT 'N/A'," +
                "description TEXT," +
                "quantity INT DEFAULT 5," +
                "available_quantity INT DEFAULT 5" +
                ")";

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);

            // Dynamically alter schema if needed
            try {
                stmt.executeUpdate("ALTER TABLE books ADD COLUMN isbn VARCHAR(50) DEFAULT 'N/A'");
            } catch (SQLException ignored) {}

            try {
                stmt.executeUpdate("ALTER TABLE books ADD COLUMN description TEXT");
            } catch (SQLException ignored) {}

            try {
                stmt.executeUpdate("ALTER TABLE books ADD COLUMN quantity INT DEFAULT 5");
            } catch (SQLException ignored) {}

            try {
                stmt.executeUpdate("ALTER TABLE books ADD COLUMN available_quantity INT DEFAULT 5");
            } catch (SQLException ignored) {}

            // Seed missing books dynamically
            seedData(conn);
        } finally {
            mysql.closeconnection(conn);
        }
    }

    private void seedData(Connection conn) throws SQLException {
        String insertSQL = "INSERT INTO books (title, author, genre, year, image_path, is_future_release, isbn, description, quantity, available_quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
            // Future releases
            addSeedItem(ps, conn, "Elantris 2", "Brandon Sanderson", "Fantasy", 2029, "elantris2.png", true, "978-0765350371", "The highly anticipated sequel to Sanderson's classic debut novel.", 5, 5);
            addSeedItem(ps, conn, "Ghostbloods 2", "Brandon Sanderson", "Fantasy", 2029, "ghostbloods2.png", true, "978-1250868268", "Delve deeper into the mysteries of the Cosmere and the secretive Ghostbloods organization.", 5, 5);
            addSeedItem(ps, conn, "Diary of a Wimpy Kid", "Jeff Kinney", "Fiction", 2029, "wimpykid.png", true, "978-1419711329", "Follow Greg Heffley's hilarious high school misadventures in this upcoming entry.", 5, 5);
            addSeedItem(ps, conn, "Percy Jackson and the Olympians", "Rick Riordan", "Fantasy", 2029, "percyjackson.png", true, "978-1423101481", "A fresh new story about Percy Jackson and the modern demigods of Camp Half-Blood.", 5, 5);

            // Catalog items
            addSeedItem(ps, conn, "Dune", "Frank Herbert", "Sci-Fi", 1965, "dune.png", false, "978-0441172719", "Set on the desert planet Arrakis, Dune is the story of the boy Paul Atreides, heir to a noble family.", 8, 8);
            addSeedItem(ps, conn, "To Kill a Mockingbird", "Harper Lee", "Classic", 1960, "mockingbird.png", false, "978-0446310789", "The classic story of lawyer Atticus Finch and his daughter Scout in a racially divided Alabama town.", 6, 6);
            addSeedItem(ps, conn, "Harry Potter", "J.K. Rowling", "Fantasy", 1997, "harrypotter.png", false, "978-0439708180", "The first novel in the Harry Potter series, following a young wizard's training at Hogwarts school.", 10, 10);
            addSeedItem(ps, conn, "Sherlock Holmes", "Arthur Conan Doyle", "Detective", 1892, "sherlock.png", false, "978-1508474319", "A collection of short detective cases starring Sherlock Holmes and Dr. John H. Watson.", 4, 4);

            // 10 new high-fidelity books
            addSeedItem(ps, conn, "The Great Gatsby", "F. Scott Fitzgerald", "Classic", 1925, "gatsby.png", false, "978-0743273565", "A portrait of the Jazz Age in all its decadence and excess, Gatsby captures the cynicism and hopes of a generation.", 5, 5);
            addSeedItem(ps, conn, "1984", "George Orwell", "Sci-Fi", 1949, "nineteen_eighty_four.png", false, "978-0451524935", "A dystopian social science fiction novel and cautionary tale about totalitarianism, surveillance, and control.", 5, 5);
            addSeedItem(ps, conn, "The Hobbit", "J.R.R. Tolkien", "Fantasy", 1937, "hobbit.png", false, "978-0345339683", "Bilbo Baggins, a quiet hobbit, is swept into a quest to reclaim the lost Dwarf Kingdom of Erebor from the dragon Smaug.", 5, 5);
            addSeedItem(ps, conn, "The Catcher in the Rye", "J.D. Salinger", "Classic", 1951, "catcher_rye.png", false, "978-0316769174", "A novel detailing two days in the life of Holden Caulfield after his expulsion from prep school.", 5, 5);
            addSeedItem(ps, conn, "The Da Vinci Code", "Dan Brown", "Detective", 2003, "davinci_code.png", false, "978-0307474278", "Harvard symbologist Robert Langdon investigates a murder in the Louvre, uncovering a massive religious conspiracy.", 5, 5);
            addSeedItem(ps, conn, "Pride and Prejudice", "Jane Austen", "Classic", 1813, "pride_prejudice.png", false, "978-0486284736", "A classic romantic novel about the emotional development of protagonist Elizabeth Bennet.", 5, 5);
            addSeedItem(ps, conn, "Fahrenheit 451", "Ray Bradbury", "Sci-Fi", 1953, "fahrenheit.png", false, "978-1451673319", "A dystopian future where books are outlawed and firemen burn any that are found.", 5, 5);
            addSeedItem(ps, conn, "The Alchemist", "Paulo Coelho", "Fiction", 1988, "alchemist.png", false, "978-0062315007", "An allegorical novel about a young Andalusian shepherd journeying to Egypt in search of worldly treasure.", 5, 5);
            addSeedItem(ps, conn, "Frankenstein", "Mary Shelley", "Classic", 1818, "frankenstein.png", false, "978-0486282114", "Victor Frankenstein creates a sapient creature in an unorthodox scientific experiment, with tragic results.", 5, 5);
            addSeedItem(ps, conn, "The Murder of Roger Ackroyd", "Agatha Christie", "Detective", 1926, "roger_ackroyd.png", false, "978-0062073563", "Renowned detective Hercule Poirot investigates the murder of a wealthy friend in a quiet English village.", 5, 5);
        }
    }

    private void addSeedItem(PreparedStatement ps, Connection conn, String title, String author, String genre, int year, String imagePath, boolean isFuture, String isbn, String desc, int qty, int availQty) throws SQLException {
        // Check if book already exists by title
        String checkSQL = "SELECT COUNT(*) FROM books WHERE title = ?";
        try (PreparedStatement checkPs = conn.prepareStatement(checkSQL)) {
            checkPs.setString(1, title);
            try (ResultSet rs = checkPs.executeQuery()) {
                if (rs.next() && rs.getInt(1) > 0) {
                    return; // Already exists, skip
                }
            }
        }

        ps.setString(1, title);
        ps.setString(2, author);
        ps.setString(3, genre);
        ps.setInt(4, year);
        ps.setString(5, imagePath);
        ps.setBoolean(6, isFuture);
        ps.setString(7, isbn);
        ps.setString(8, desc);
        ps.setInt(9, qty);
        ps.setInt(10, availQty);
        ps.executeUpdate();
    }

    // Get all future releases
    public List<Book> getFutureReleases() throws SQLException {
        List<Book> list = new ArrayList<>();
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }

        String sql = "SELECT * FROM books WHERE is_future_release = true ORDER BY year ASC";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapBook(rs));
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return list;
    }

    // Get catalog books with search and genre filters
    public List<Book> getCatalogBooks(String searchTitle, String genre) throws SQLException {
        List<Book> list = new ArrayList<>();
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }

        StringBuilder sql = new StringBuilder("SELECT * FROM books WHERE is_future_release = false");
        if (searchTitle != null && !searchTitle.trim().isEmpty()) {
            sql.append(" AND (title LIKE ? OR author LIKE ?)");
        }
        if (genre != null && !genre.trim().isEmpty() && !genre.equalsIgnoreCase("All Genres")) {
            sql.append(" AND genre = ?");
        }
        sql.append(" ORDER BY title ASC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            int paramIndex = 1;
            if (searchTitle != null && !searchTitle.trim().isEmpty()) {
                String likePattern = "%" + searchTitle.trim() + "%";
                ps.setString(paramIndex++, likePattern);
                ps.setString(paramIndex++, likePattern);
            }
            if (genre != null && !genre.trim().isEmpty() && !genre.equalsIgnoreCase("All Genres")) {
                ps.setString(paramIndex, genre.trim());
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapBook(rs));
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return list;
    }

    // CRUD: Get single book
    public Book getBookById(int id) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT * FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapBook(rs);
                }
            }
        } finally {
            mysql.closeconnection(conn);
        }
        return null;
    }

    // CRUD: Add Book
    public boolean addBook(Book book) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "INSERT INTO books (title, author, genre, year, image_path, is_future_release, isbn, description, quantity, available_quantity) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getGenre());
            ps.setInt(4, book.getYear());
            ps.setString(5, book.getImagePath());
            ps.setBoolean(6, book.isFutureRelease());
            ps.setString(7, book.getIsbn());
            ps.setString(8, book.getDescription());
            ps.setInt(9, book.getQuantity());
            ps.setInt(10, book.getAvailableQuantity());
            return ps.executeUpdate() > 0;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // CRUD: Update Book
    public boolean updateBook(Book book) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "UPDATE books SET title = ?, author = ?, genre = ?, year = ?, image_path = ?, is_future_release = ?, isbn = ?, description = ?, quantity = ?, available_quantity = ? WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getGenre());
            ps.setInt(4, book.getYear());
            ps.setString(5, book.getImagePath());
            ps.setBoolean(6, book.isFutureRelease());
            ps.setString(7, book.getIsbn());
            ps.setString(8, book.getDescription());
            ps.setInt(9, book.getQuantity());
            ps.setInt(10, book.getAvailableQuantity());
            ps.setInt(11, book.getId());
            return ps.executeUpdate() > 0;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // CRUD: Delete Book
    public boolean deleteBook(int id) throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // Dynamic stats: Get total books count
    public int getTotalBooksCount() throws SQLException {
        Connection conn = mysql.openconnection();
        if (conn == null) {
            throw new SQLException("Database connection failed.");
        }
        String sql = "SELECT SUM(quantity) FROM books";
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

    // Helper to map result set row to Book object
    private Book mapBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("author"),
                rs.getString("genre"),
                rs.getInt("year"),
                rs.getString("image_path"),
                rs.getBoolean("is_future_release"),
                rs.getString("isbn"),
                rs.getString("description"),
                rs.getInt("quantity"),
                rs.getInt("available_quantity")
        );
    }
}
