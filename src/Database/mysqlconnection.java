package Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.*;

public class mysqlconnection implements Db {

    @Override
    public Connection openConnection() {
        try {
            String username = "root";
            String password = "1234";
            String database = "lms";
            Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + database, username, password
            );
            if (conn != null) {
                System.out.println("Connection successful");
            }
            return conn;
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Connection closed");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    @Override
    public ResultSet runQuery(Connection conn, String query) {
        try {
            return conn.createStatement().executeQuery(query);
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    @Override
    public int executeUpdate(Connection conn, String query) {
        try {
            return conn.createStatement().executeUpdate(query);
        } catch (SQLException e) {
            System.out.println(e);
            return 0;
        }
    }
}