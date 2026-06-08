package Controller;

import Model.Book;
import Model.Borrow;
import Model.userdata;
import View.UserBooksView;
// import View.UserLogin;
import dao.BookDao;
import dao.BorrowDao;
import dao.FineDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// Sprint 3: Renew book action listener
// Sprint 4: Table populators for history
public class UserBooksController {
    private final UserBooksView view;
    private final userdata currentUser;
    private final BorrowDao borrowDao = new BorrowDao();
    private final BookDao bookDao = new BookDao();
    private final FineDao fineDao = new FineDao();

    public UserBooksController(UserBooksView view, userdata user) {
        this.view = view;
        this.currentUser = user;

        // Button actions
        this.view.getBtnReturn().addActionListener(new ReturnBookListener());
        this.view.getBtnRenew().addActionListener(new RenewBookListener());
        this.view.getBtnDashboard().addActionListener(new DashboardNavListener());
        this.view.getBtnCatalog().addActionListener(new CatalogNavListener());
        this.view.getBtnMyBooks().addActionListener(e -> { /* Already here */ });
        this.view.getBtnPayments().addActionListener(new PaymentsNavListener());
        this.view.getBtnLogout().addActionListener(new LogoutListener());
    }

    public void open() {
        this.view.setVisible(true);
        loadTablesData();
    }

    private void loadTablesData() {
        try {
            // Trigger fine updates first
            fineDao.calculateFines();
            
            // 1. Load active borrows
            List<Borrow> active = borrowDao.getActiveBorrowsForUser(currentUser.getId());
            DefaultTableModel modelBorrowed = (DefaultTableModel) view.getTblBorrowed().getModel();
            modelBorrowed.setRowCount(0);
            for (Borrow b : active) {
                Book book = bookDao.getBookById(b.getBookId());
                String title = book != null ? book.getTitle() : "Unknown";
                String author = book != null ? book.getAuthor() : "Unknown";
                modelBorrowed.addRow(new Object[]{
                    b.getId(),
                    title,
                    author,
                    b.getBorrowDate(),
                    b.getDueDate(),
                    b.getRenewCount() + " / 2",
                    b.getStatus()
                });
            }

            // 2. Load returned books
            List<Borrow> returned = borrowDao.getReturnedBorrowsForUser(currentUser.getId());
            DefaultTableModel modelReturned = (DefaultTableModel) view.getTblReturned().getModel();
            modelReturned.setRowCount(0);
            for (Borrow b : returned) {
                Book book = bookDao.getBookById(b.getBookId());
                String title = book != null ? book.getTitle() : "Unknown";
                String author = book != null ? book.getAuthor() : "Unknown";
                modelReturned.addRow(new Object[]{
                    b.getId(),
                    title,
                    author,
                    b.getBorrowDate(),
                    b.getDueDate(),
                    b.getReturnDate(),
                    b.getStatus()
                });
            }

            // 3. Load all history
            List<Borrow> history = borrowDao.getUserBorrowHistory(currentUser.getId());
            DefaultTableModel modelHistory = (DefaultTableModel) view.getTblHistory().getModel();
            modelHistory.setRowCount(0);
            for (Borrow b : history) {
                Book book = bookDao.getBookById(b.getBookId());
                String title = book != null ? book.getTitle() : "Unknown";
                String author = book != null ? book.getAuthor() : "Unknown";
                modelHistory.addRow(new Object[]{
                    b.getId(),
                    title,
                    author,
                    b.getBorrowDate(),
                    b.getDueDate(),
                    b.getReturnDate() != null ? b.getReturnDate() : "Active",
                    b.getStatus()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error loading borrow data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Return Book Listener
    class ReturnBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = view.getTblBorrowed().getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(view, "Please select an active borrow record to return.", "Select Row", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int borrowId = (int) view.getTblBorrowed().getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to return this book?", "Return Book", JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = borrowDao.returnBook(borrowId);
                    if (success) {
                        JOptionPane.showMessageDialog(view, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadTablesData();
                    } else {
                        JOptionPane.showMessageDialog(view, "Failed to return book. Record not found or already returned.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }

    // Renew Book Listener
    class RenewBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = view.getTblBorrowed().getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(view, "Please select an active borrow record to renew.", "Select Row", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int borrowId = (int) view.getTblBorrowed().getValueAt(selectedRow, 0);
            try {
                boolean success = borrowDao.renewBook(borrowId);
                if (success) {
                    JOptionPane.showMessageDialog(view, "Book renewed successfully for another 14 days!", "Renewal Success", JOptionPane.INFORMATION_MESSAGE);
                    loadTablesData();
                } else {
                    JOptionPane.showMessageDialog(view, "Renewal failed!\nEnsure you haven't reached the 2 renewals limit, and that the book is not already overdue.", "Renewal Failed", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Navigation Listeners
    class DashboardNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // View.UserDashboard ud = new View.UserDashboard(currentUser);
            // UserDashboardController udc = new UserDashboardController(ud, currentUser);
            // udc.open();
            // view.dispose();
            JOptionPane.showMessageDialog(view, "Dashboard is stubbed in books branch.");
        }
    }

    class CatalogNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // View.CatalogView cv = new View.CatalogView();
            // CatalogController cc = new CatalogController(cv, currentUser);
            // cc.open();
            // view.dispose();
            JOptionPane.showMessageDialog(view, "Catalog is stubbed in books branch.");
        }
    }

    class PaymentsNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // View.UserPaymentsView upv = new View.UserPaymentsView(currentUser);
            // UserPaymentsController upc = new UserPaymentsController(upv, currentUser);
            // upc.open();
            // view.dispose();
            JOptionPane.showMessageDialog(view, "Payments is stubbed in books branch.");
        }
    }

    class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                view.dispose();
                // UserLogin loginFrame = new UserLogin();
                // LoginController lc = new LoginController(loginFrame);
                // lc.open();
                JOptionPane.showMessageDialog(null, "Logout Successful! (Login screen is stubbed in books branch)");
            }
        }
    }
}
