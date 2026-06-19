package Controller;

import dao.UserDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import View.ForgotPassword;
import View.UserLogin;
import java.awt.HeadlessException;

public class ForgotPasswordController {
    private final UserDao userDao = new UserDao();
    private final ForgotPassword userView;

    public ForgotPasswordController(ForgotPassword userView) {
        this.userView = userView;
        userView.addResetPasswordListener(new ResetListener());
        userView.addBackToLoginListener(new BackToLoginListener());
    }

    public void open() {
        this.userView.setVisible(true);
    }

    public void close() {
        this.userView.dispose();
    }

    // Reset Password button
    class ResetListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String email = userView.getEmailField().getText().trim();
                String newPassword = new String(userView.getNewPasswordField().getPassword()).trim();
                String confirmPassword = new String(userView.getConfirmPasswordField().getPassword()).trim();

                if (email.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(userView, "Please fill in all fields.");
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(userView, "Passwords do not match.");
                    return;
                }

                boolean emailExists = userDao.checkEmail(email);
                if (!emailExists) {
                    JOptionPane.showMessageDialog(userView, "Email not found. Please check and try again.");
                    return;
                }

                boolean success = userDao.updatePassword(email, newPassword);
                if (success) {
                    JOptionPane.showMessageDialog(userView, "Password reset successful! Please login.");
                    UserLogin login = new UserLogin();
                    LoginController lc = new LoginController(login);
                    lc.open();
                    userView.dispose();
                } else {
                    JOptionPane.showMessageDialog(userView, "Failed to update password. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (java.sql.SQLException ex) {
                JOptionPane.showMessageDialog(userView, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(userView, "An unexpected error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Back to Login button
    class BackToLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            UserLogin login = new UserLogin();
            LoginController lc = new LoginController(login);
            lc.open();
            userView.dispose();
        }
    }
}