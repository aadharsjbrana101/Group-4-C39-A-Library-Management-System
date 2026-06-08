package librarymanagementsystem;

import Controller.LoginController;
import View.UserLogin;
import dao.UserDao;
// import dao.BookDao;
// import dao.BorrowDao;
// import dao.FineDao;
// import dao.AdminLogDao;

    public static void main(String[] args) {
        // Launch UserBooksView directly in isolation mode
        java.awt.EventQueue.invokeLater(() -> {
            Model.userdata mockUser = new Model.userdata(1, "Rashmi (Books Isolation)", "rashmi@lms.com", "1234", "user", "active");
            View.UserBooksView ubv = new View.UserBooksView(mockUser);
            Controller.UserBooksController ubc = new Controller.UserBooksController(ubv, mockUser);
            ubc.open();
        });
    }
}