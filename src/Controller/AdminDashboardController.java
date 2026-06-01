package Controller;

import Model.AdminLog;
import Model.userdata;
import View.AdminDashboard;
// import View.UserLogin;
import dao.AdminLogDao;
import dao.BookDao;
import dao.BorrowDao;
import dao.FineDao;
import dao.UserDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

public class AdminDashboardController {
    private final AdminDashboard view;
    private final userdata currentAdmin;
    private final BookDao bookDao = new BookDao();
    private final UserDao userDao = new UserDao();
    private final BorrowDao borrowDao = new BorrowDao();
    private final FineDao fineDao = new FineDao();
    private final AdminLogDao adminLogDao = new AdminLogDao();
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AdminDashboardController(AdminDashboard view, userdata admin) {
        this.view = view;
        this.currentAdmin = admin;

        // Button Actions
        this.view.getBtnDashboard().addActionListener(e -> { /* Already here */ });
        this.view.getBtnCatalog().addActionListener(new CatalogNavListener());
        this.view.getBtnUsers().addActionListener(new UsersNavListener());
        this.view.getBtnPayments().addActionListener(new PaymentsNavListener());
        this.view.getBtnLogout().addActionListener(new LogoutListener());
    }

    public void open() {
        this.view.setVisible(true);
        loadDashboardData();
    }

    private void loadDashboardData() {
        try {
            // Trigger dynamic fine calculations to keep outstanding fine stat fresh
            fineDao.calculateFines();
            
            int totalBooks = bookDao.getTotalBooksCount();
            int totalUsers = userDao.getAllUsers().size();
            int activeIssued = borrowDao.getActiveBorrowsCount();
            double outstandingFines = fineDao.getTotalOutstandingFines();

            view.getLblBooksVal().setText(String.valueOf(totalBooks));
            view.getLblUsersVal().setText(String.valueOf(totalUsers));
            view.getLblIssuedVal().setText(String.valueOf(activeIssued));
            view.getLblFinesVal().setText(String.format("Rs. %.2f", outstandingFines));

            // Load Admin Activity Logs
            List<AdminLog> logs = adminLogDao.getAllLogs();
            DefaultListModel<String> listModel = new DefaultListModel<>();
            
            for (AdminLog log : logs) {
                // Find admin username
                String adminName = "admin"; // Default fallback
                if (log.getAdminId() == currentAdmin.getId()) {
                    adminName = currentAdmin.getUsername();
                } else {
                    List<userdata> users = userDao.getAllUsers();
                    for (userdata u : users) {
                        if (u.getId() == log.getAdminId()) {
                            adminName = u.getUsername();
                            break;
                        }
                    }
                }
                
                String timeStr = dateFormat.format(log.getActionDate());
                listModel.addElement(String.format("[%s] Admin '%s' performed: %s", timeStr, adminName, log.getAction()));
            }
            if (logs.isEmpty()) {
                listModel.addElement("No recent activities logged.");
            }
            view.getLstActivities().setModel(listModel);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error loading admin dashboard stats: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Navigation Listeners
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

    class PaymentsNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // View.AdminPaymentsView apv = new View.AdminPaymentsView(currentAdmin);
            // AdminPaymentsController apc = new AdminPaymentsController(apv, currentAdmin);
            // apc.open();
            // view.dispose();
            JOptionPane.showMessageDialog(view, "Admin Payments is stubbed in dashboard branch.");
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
                JOptionPane.showMessageDialog(null, "Logout Successful! (Login screen is stubbed in dashboard branch)");
            }
        }
    }
}
