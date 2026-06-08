package librarymanagementsystem;

import Database.mysqlconnection;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class DatabaseTest {
    public static void main(String[] args) {
        // Sprint 2 (Ronish): Define testing suite structure
        // Sprint 2 (Ronish): Construct validation helper tests
        // Sprint 2 (Ronish): Document unit test behaviors
        mysqlconnection mysql = new mysqlconnection();
        Connection conn = mysql.openconnection();
        if (conn == null) {
            System.out.println("Connection Failed.");
            return;
        }
        try {
            printTableDetails(conn, "users");
            printTableDetails(conn, "books");
            printTableDetails(conn, "borrows");
            printTableDetails(conn, "fines");
            printTableDetails(conn, "admin_logs");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mysql.closeconnection(conn);
        }
    }

    // Sprint 2 (Ronish): Construct validation helper tests
    private static void printTableDetails(Connection conn, String tableName) throws Exception {
        System.out.println("\n--- Table: " + tableName + " ---");
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 5")) {
            ResultSetMetaData meta = rs.getMetaData();
            int colCount = meta.getColumnCount();
            for (int i = 1; i <= colCount; i++) {
                System.out.print(meta.getColumnName(i) + " (" + meta.getColumnTypeName(i) + ") | ");
            }
            System.out.println("\nRows Count: " + getRowCount(conn, tableName));
        } catch (Exception e) {
            System.out.println("Error reading table " + tableName + ": " + e.getMessage());
        }
    }

    private static int getRowCount(Connection conn, String tableName) throws Exception {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM " + tableName)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
}
