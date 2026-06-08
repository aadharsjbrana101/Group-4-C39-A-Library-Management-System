package librarymanagementsystem;

import Controller.LoginController;
import View.UserLogin;
import dao.UserDao;
// import dao.BookDao;
// import dao.BorrowDao;
// import dao.FineDao;
// import dao.AdminLogDao;

    public static void main(String[] args) {
        // Launch UserPaymentsView directly in isolation mode
        java.awt.EventQueue.invokeLater(() -> {
            Model.userdata mockUser = new Model.userdata(1, "Kushal (Payments Isolation)", "kushal@lms.com", "1234", "user", "active");
            View.UserPaymentsView upv = new View.UserPaymentsView(mockUser);
            Controller.UserPaymentsController upc = new Controller.UserPaymentsController(upv, mockUser);
            upc.open();
        });
    }
}