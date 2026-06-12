package Controller;

import Model.Book;
import Model.UserData;
import View.BookDetailView;
import View.UserLogin;
import dao.BookDao;
import dao.BorrowDao;
import dao.FineDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class BookDetailController {
    private final BookDetailView view;
    private final Book book;
    private final UserData currentUser;
    private final BorrowDao borrowDao = new BorrowDao();
    private final BookDao bookDao = new BookDao();
    private final FineDao fineDao = new FineDao();

    public BookDetailController(BookDetailView view, Book book, UserData user) {
        this.view = view;
        this.book = book;
        this.currentUser = user;

        // Action listeners
        this.view.getBtnBorrow().addActionListener(new BorrowBookListener());
        this.view.getBtnBack().addActionListener(new BackListener());
        this.view.getBtnDashboard().addActionListener(new DashboardNavListener());
        this.view.getBtnCatalog().addActionListener(new CatalogNavListener());
        this.view.getBtnMyBooks().addActionListener(new MyBooksNavListener());
        this.view.getBtnPayments().addActionListener(new PaymentsNavListener());
        this.view.getBtnLogout().addActionListener(new LogoutListener());
    }

    public void open() {
        this.view.setVisible(true);
        refreshBookDetails();
    }

    private void refreshBookDetails() {
        try {
            Book updatedBook = bookDao.getBookById(book.getId());
            if (updatedBook != null) {
                this.view.setBookDetails(updatedBook);
            } else {
                this.view.setBookDetails(book);
            }
        } catch (SQLException e) {
            this.view.setBookDetails(book);
        }
    }

    // Borrow Book button
    class BorrowBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // 1. Check if user has unpaid fines
                double outstandingFines = fineDao.getUnpaidFinesSumForUser(currentUser.getId());
                if (outstandingFines > 0.0) {
                    JOptionPane.showMessageDialog(view, "Borrow Denied! You have outstanding fines of Rs. " + String.format("%.2f", outstandingFines) + ". Please pay them first.", "Account Blocked", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // 2. Check if already borrowing
                if (borrowDao.hasActiveBorrow(currentUser.getId(), book.getId())) {
                    JOptionPane.showMessageDialog(view, "You are already borrowing an active copy of this book.", "Duplicate Borrow", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // 3. Attempt borrow
                boolean success = borrowDao.borrowBook(currentUser.getId(), book.getId());
                if (success) {
                    JOptionPane.showMessageDialog(view, "Book borrowed successfully! It is due in 14 days.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshBookDetails();
                    
                    // Route to My Books to view active borrow
                    View.UserBooksView ubv = new View.UserBooksView(currentUser);
                    UserBooksController ubc = new UserBooksController(ubv, currentUser);
                    ubc.open();
                    view.dispose();
                } else {
                    JOptionPane.showMessageDialog(view, "Book is currently out of stock.", "Out of Stock", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.CatalogView cv = new View.CatalogView();
            CatalogController cc = new CatalogController(cv, currentUser);
            cc.open();
            view.dispose();
        }
    }

    // Navigation Listeners
    class DashboardNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.UserDashboard ud = new View.UserDashboard(currentUser);
            UserDashboardController udc = new UserDashboardController(ud, currentUser);
            udc.open();
            view.dispose();
        }
    }

    class CatalogNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.CatalogView cv = new View.CatalogView();
            CatalogController cc = new CatalogController(cv, currentUser);
            cc.open();
            view.dispose();
        }
    }

    class MyBooksNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.UserBooksView ubv = new View.UserBooksView(currentUser);
            UserBooksController ubc = new UserBooksController(ubv, currentUser);
            ubc.open();
            view.dispose();
        }
    }

    class PaymentsNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.UserPaymentsView upv = new View.UserPaymentsView(currentUser);
            UserPaymentsController upc = new UserPaymentsController(upv, currentUser);
            upc.open();
            view.dispose();
        }
    }

    class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                view.dispose();
                UserLogin loginFrame = new UserLogin();
                LoginController lc = new LoginController(loginFrame);
                lc.open();
            }
        }
    }
}
