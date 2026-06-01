package Controller;

import Model.Book;
import Model.Borrow;
import Model.Fine;
import Model.userdata;
// import View.UserLogin;
import View.UserPaymentsView;
import dao.BookDao;
import dao.BorrowDao;
import dao.FineDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

// Sprint 3: Set user alert reminders trigger
public class UserPaymentsController {
    private final UserPaymentsView view;
    private final userdata currentUser;
    private final FineDao fineDao = new FineDao();
    private final BorrowDao borrowDao = new BorrowDao();
    private final BookDao bookDao = new BookDao();

    public UserPaymentsController(UserPaymentsView view, userdata user) {
        this.view = view;
        this.currentUser = user;

        // Listeners
        this.view.getBtnPay().addActionListener(new PayFineListener());
        this.view.getBtnDashboard().addActionListener(new DashboardNavListener());
        this.view.getBtnCatalog().addActionListener(new CatalogNavListener());
        this.view.getBtnMyBooks().addActionListener(new MyBooksNavListener());
        this.view.getBtnPayments().addActionListener(e -> { /* Already here */ });
        this.view.getBtnLogout().addActionListener(new LogoutListener());
    }

    public void open() {
        this.view.setVisible(true);
        loadFinesData();
    }

    private void loadFinesData() {
        try {
            // Trigger latest calculations
            fineDao.calculateFines();

            double pending = fineDao.getUnpaidFinesSumForUser(currentUser.getId());
            double paid = fineDao.getPaidFinesSumForUser(currentUser.getId());
            double total = pending + paid;

            view.getLblTotalVal().setText(String.format("Rs. %.2f", total));
            view.getLblPendingVal().setText(String.format("Rs. %.2f", pending));
            view.getLblPaidVal().setText(String.format("Rs. %.2f", paid));

            List<Fine> fines = fineDao.getFinesForUser(currentUser.getId());
            DefaultTableModel model = (DefaultTableModel) view.getTblPayments().getModel();
            model.setRowCount(0);

            for (Fine f : fines) {
                // Fetch borrow and book details
                String bookTitle = "Unknown";
                // Since f.getBorrowId() maps to borrows, we lookup:
                List<Borrow> history = borrowDao.getUserBorrowHistory(currentUser.getId());
                Borrow match = null;
                for (Borrow b : history) {
                    if (b.getId() == f.getBorrowId()) {
                        match = b;
                        break;
                    }
                }
                if (match != null) {
                    Book book = bookDao.getBookById(match.getBookId());
                    if (book != null) {
                        bookTitle = book.getTitle();
                    }
                }

                String dateStr = f.getPaymentDate() != null ? f.getPaymentDate().toString() : "Due: " + (match != null ? match.getDueDate().toString() : "N/A");

                model.addRow(new Object[]{
                    f.getId(),
                    f.getBorrowId(),
                    bookTitle,
                    String.format("Rs. %.2f", f.getAmount()),
                    f.getStatus().toUpperCase(),
                    dateStr
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error loading payments details: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Pay Fine Listener
    class PayFineListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = view.getTblPayments().getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(view, "Please select an unpaid fine transaction from the table.", "Select Transaction", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int fineId = (int) view.getTblPayments().getValueAt(selectedRow, 0);
            String status = (String) view.getTblPayments().getValueAt(selectedRow, 4);

            if ("PAID".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(view, "This fine has already been paid.", "Transaction Complete", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view, "Proceed with paying Rs. " + view.getTblPayments().getValueAt(selectedRow, 3) + " for this fine?", "Pay Fine", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = fineDao.payFine(fineId);
                    if (success) {
                        JOptionPane.showMessageDialog(view, "Payment Successful! Fine marked as Paid.", "Payment Success", JOptionPane.INFORMATION_MESSAGE);
                        loadFinesData();
                    } else {
                        JOptionPane.showMessageDialog(view, "Fine payment failed. Transaction record not found.", "Payment Failed", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
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
            JOptionPane.showMessageDialog(view, "Dashboard is stubbed in payments branch.");
        }
    }

    class CatalogNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // View.CatalogView cv = new View.CatalogView();
            // CatalogController cc = new CatalogController(cv, currentUser);
            // cc.open();
            // view.dispose();
            JOptionPane.showMessageDialog(view, "Catalog is stubbed in payments branch.");
        }
    }

    class MyBooksNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // View.UserBooksView ubv = new View.UserBooksView(currentUser);
            // UserBooksController ubc = new UserBooksController(ubv, currentUser);
            // ubc.open();
            // view.dispose();
            JOptionPane.showMessageDialog(view, "My Books is stubbed in payments branch.");
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
                JOptionPane.showMessageDialog(null, "Logout Successful! (Login screen is stubbed in payments branch)");
            }
        }
    }
}
