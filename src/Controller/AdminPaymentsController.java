package Controller;

import Model.Book;
import Model.Borrow;
import Model.Fine;
import Model.userdata;
import View.AdminPaymentsView;
import View.UserLogin;
import dao.AdminLogDao;
import dao.BookDao;
import dao.BorrowDao;
import dao.FineDao;
import dao.UserDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AdminPaymentsController {
    private final AdminPaymentsView view;
    private final userdata currentAdmin;
    private final FineDao fineDao = new FineDao();
    private final BorrowDao borrowDao = new BorrowDao();
    private final BookDao bookDao = new BookDao();
    private final UserDao userDao = new UserDao();
    private final AdminLogDao adminLogDao = new AdminLogDao();

    public AdminPaymentsController(AdminPaymentsView view, userdata admin) {
        this.view = view;
        this.currentAdmin = admin;

        // Button Actions
        this.view.getBtnExport().addActionListener(new ExportCSVListener());

        // Sidebar Navigation
        this.view.getBtnDashboard().addActionListener(new DashboardNavListener());
        this.view.getBtnCatalog().addActionListener(new CatalogNavListener());
        this.view.getBtnUsers().addActionListener(new UsersNavListener());
        this.view.getBtnPayments().addActionListener(e -> { /* Already here */ });
        this.view.getBtnLogout().addActionListener(new LogoutListener());
    }

    public void open() {
        this.view.setVisible(true);
        loadPaymentsData();
    }

    private void loadPaymentsData() {
        try {
            // Recalculate late fees first
            fineDao.calculateFines();

            double totalPaid = fineDao.getTotalFinesReceived();
            double outstanding = fineDao.getTotalOutstandingFines();
            
            // Count overdue borrows (unpaid fines)
            int overdueCount = 0;
            List<Fine> allFines = fineDao.getAllFines();
            for (Fine f : allFines) {
                if ("unpaid".equalsIgnoreCase(f.getStatus())) {
                    overdueCount++;
                }
            }

            view.getLblTotalVal().setText(String.format("Rs. %.2f", totalPaid));
            view.getLblOutstandingVal().setText(String.format("Rs. %.2f", outstanding));
            view.getLblOverdueVal().setText(String.valueOf(overdueCount));

            DefaultTableModel model = (DefaultTableModel) view.getTblPayments().getModel();
            model.setRowCount(0);

            List<userdata> allUsers = userDao.getAllUsers();

            for (Fine f : allFines) {
                // Find Username
                String username = "Unknown";
                for (userdata u : allUsers) {
                    if (u.getId() == f.getUserId()) {
                        username = u.getUsername();
                        break;
                    }
                }

                // Find Book Title and Due Date
                String bookTitle = "Unknown";
                String dueDateStr = "N/A";
                
                // Fetch all borrow history of this user to find matching borrow record
                List<Borrow> borrowHistory = borrowDao.getUserBorrowHistory(f.getUserId());
                for (Borrow b : borrowHistory) {
                    if (b.getId() == f.getBorrowId()) {
                        dueDateStr = b.getDueDate().toString();
                        Book book = bookDao.getBookById(b.getBookId());
                        if (book != null) {
                            bookTitle = book.getTitle();
                        }
                        break;
                    }
                }

                model.addRow(new Object[]{
                    f.getId(),
                    username,
                    bookTitle,
                    String.format("Rs. %.2f", f.getAmount()),
                    f.getStatus().toUpperCase(),
                    f.getPaymentDate() != null ? f.getPaymentDate().toString() : "Unpaid",
                    dueDateStr
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error loading admin payments data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Export Reports to CSV button
    class ExportCSVListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                // Define reports directory inside the project workspace
                File dir = new File("reports");
                if (!dir.exists()) {
                    dir.mkdir();
                }
                File file = new File(dir, "library_payments_report.csv");
                
                try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                    // CSV Headers
                    pw.println("Fine ID,Username,Book Title,Fine Amount,Status,Payment Date,Due Date");
                    
                    DefaultTableModel model = (DefaultTableModel) view.getTblPayments().getModel();
                    for (int i = 0; i < model.getRowCount(); i++) {
                        String amt = model.getValueAt(i, 3).toString().replace("Rs. ", "");
                        pw.printf("%s,%s,\"%s\",%s,%s,%s,%s\n",
                            model.getValueAt(i, 0),
                            model.getValueAt(i, 1),
                            model.getValueAt(i, 2),
                            amt,
                            model.getValueAt(i, 4),
                            model.getValueAt(i, 5),
                            model.getValueAt(i, 6)
                        );
                    }
                }
                
                JOptionPane.showMessageDialog(view, "Payments report successfully exported to CSV!\nLocation: " + file.getAbsolutePath(), "Export Success", JOptionPane.INFORMATION_MESSAGE);
                adminLogDao.logAction(currentAdmin.getId(), "Exported transaction payments report to CSV");
                
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "Failed to export report: " + ex.getMessage(), "Export Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Navigation Listeners
    class DashboardNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.AdminDashboard ad = new View.AdminDashboard(currentAdmin);
            AdminDashboardController adc = new AdminDashboardController(ad, currentAdmin);
            adc.open();
            view.dispose();
        }
    }

    class CatalogNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.AdminCatalogView acv = new View.AdminCatalogView(currentAdmin);
            AdminCatalogController acc = new AdminCatalogController(acv, currentAdmin);
            acc.open();
            view.dispose();
        }
    }

    class UsersNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.AdminUsersView auv = new View.AdminUsersView(currentAdmin);
            AdminUsersController auc = new AdminUsersController(auv, currentAdmin);
            auc.open();
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
