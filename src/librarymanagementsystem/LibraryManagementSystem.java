package librarymanagementsystem;

import Controller.LoginController;
import View.UserLogin;
import dao.UserDao;
// import dao.BookDao;
// import dao.BorrowDao;
// import dao.FineDao;
// import dao.AdminLogDao;

public class LibraryManagementSystem {
    public static void main(String[] args) {
        // Initialize the database tables and schema modifications at startup
        System.out.println("Initializing database schemas...");
        new UserDao();
        // new BookDao();
        // new BorrowDao();
        // new FineDao();
        // new AdminLogDao();
        System.out.println("Database initialization completed.");

        // Start the application GUI
        UserLogin login = new UserLogin();
        LoginController lc = new LoginController(login);
        lc.open();
    }
}