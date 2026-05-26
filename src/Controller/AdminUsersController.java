package Controller;

import Model.userdata;
import View.AdminUsersView;
import View.UserLogin;
import dao.AdminLogDao;
import dao.UserDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class AdminUsersController {
    private final AdminUsersView view;
    private final userdata currentAdmin;
    private final UserDao userDao = new UserDao();
    private final AdminLogDao adminLogDao = new AdminLogDao();

    public AdminUsersController(AdminUsersView view, userdata admin) {
        this.view = view;
        this.currentAdmin = admin;

        // Button Actions
        this.view.getBtnBlock().addActionListener(new BlockUserListener());
        this.view.getBtnActivate().addActionListener(new ActivateUserListener());

        // Sidebar Navigation
        this.view.getBtnDashboard().addActionListener(new DashboardNavListener());
        this.view.getBtnCatalog().addActionListener(new CatalogNavListener());
        this.view.getBtnUsers().addActionListener(e -> { /* Already here */ });
        this.view.getBtnPayments().addActionListener(new PaymentsNavListener());
        this.view.getBtnLogout().addActionListener(new LogoutListener());
    }

    public void open() {
        this.view.setVisible(true);
        loadUsers();
    }

    private void loadUsers() {
        try {
            List<userdata> usersList = userDao.getAllUsers();
            DefaultTableModel model = (DefaultTableModel) view.getTblUsers().getModel();
            model.setRowCount(0);

            for (userdata u : usersList) {
                model.addRow(new Object[]{
                    u.getId(),
                    u.getUsername(),
                    u.getEmail(),
                    u.getRole().toUpperCase(),
                    u.getStatus().toUpperCase()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error loading users: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Block User Listener
    class BlockUserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = view.getTblUsers().getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(view, "Please select a user from the table to block.", "Select User", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = (int) view.getTblUsers().getValueAt(selectedRow, 0);
            String username = (String) view.getTblUsers().getValueAt(selectedRow, 1);
            String role = (String) view.getTblUsers().getValueAt(selectedRow, 3);
            String status = (String) view.getTblUsers().getValueAt(selectedRow, 4);

            if (userId == currentAdmin.getId()) {
                JOptionPane.showMessageDialog(view, "You cannot block yourself!", "Action Denied", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ("ADMIN".equalsIgnoreCase(role)) {
                JOptionPane.showMessageDialog(view, "You cannot block other administrators!", "Action Denied", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if ("BLOCKED".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(view, "This user is already blocked.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to block user '" + username + "'? They will not be able to log in.", "Block User", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = userDao.updateUserStatus(userId, "blocked");
                    if (success) {
                        adminLogDao.logAction(currentAdmin.getId(), "Blocked user ID " + userId + " (" + username + ")");
                        JOptionPane.showMessageDialog(view, "User blocked successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadUsers();
                    } else {
                        JOptionPane.showMessageDialog(view, "Failed to block user.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }

    // Activate User Listener
    class ActivateUserListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedRow = view.getTblUsers().getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(view, "Please select a user from the table to activate.", "Select User", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int userId = (int) view.getTblUsers().getValueAt(selectedRow, 0);
            String username = (String) view.getTblUsers().getValueAt(selectedRow, 1);
            String status = (String) view.getTblUsers().getValueAt(selectedRow, 4);

            if ("ACTIVE".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(view, "This user is already active.", "Information", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view, "Activate user '" + username + "'? They will regain access to log in.", "Activate User", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = userDao.updateUserStatus(userId, "active");
                    if (success) {
                        adminLogDao.logAction(currentAdmin.getId(), "Activated user ID " + userId + " (" + username + ")");
                        JOptionPane.showMessageDialog(view, "User activated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadUsers();
                    } else {
                        JOptionPane.showMessageDialog(view, "Failed to activate user.", "Error", JOptionPane.ERROR_MESSAGE);
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

    class PaymentsNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.AdminPaymentsView apv = new View.AdminPaymentsView(currentAdmin);
            AdminPaymentsController apc = new AdminPaymentsController(apv, currentAdmin);
            apc.open();
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
