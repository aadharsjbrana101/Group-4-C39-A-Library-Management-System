package Controller;

import dao.UserDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import Model.userdata;
import View.UserLogin;
import View.UserSignUp;
import View.ForgotPassword;

// Sprint 4: Logout handler logic
public class LoginController {
    private final UserDao userDao = new UserDao();
    private final UserLogin userView;

    public LoginController(UserLogin userView) {
        this.userView = userView;
        userView.addLoginListener(new LoginListener());
        userView.addSignUpListener(new SignUpListener());
        userView.addForgotPasswordListener(new ForgotPasswordListener());
    }

    public void open() {
        this.userView.setVisible(true);
    }

    public void close() {
        this.userView.dispose();
    }

    // Login button
    class LoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String username = userView.getUsernameField().getText().trim();
                String password = new String(userView.getPasswordField().getPassword()).trim();

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(userView, "Please enter username and password.");
                    return;
                }

                userdata authUser = userDao.authenticate(username, password);

                if (authUser != null) {
                    if ("blocked".equalsIgnoreCase(authUser.getStatus())) {
                        JOptionPane.showMessageDialog(userView, "Your account has been blocked by the administrator.", "Access Denied", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    JOptionPane.showMessageDialog(userView, "Login Successful! Welcome, " + authUser.getUsername());
                    userView.dispose();
                    
                    if ("admin".equalsIgnoreCase(authUser.getRole())) {
                        // Open Admin Dashboard
                        View.AdminDashboard adv = new View.AdminDashboard(authUser);
                        AdminDashboardController adc = new AdminDashboardController(adv, authUser);
                        adc.open();
                    } else {
                        // Open User Dashboard
                        View.UserDashboard udv = new View.UserDashboard(authUser);
                        UserDashboardController udc = new UserDashboardController(udv, authUser);
                        udc.open();
                    }
                } else {
                    JOptionPane.showMessageDialog(userView, "Invalid username or password.");
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

    // SignUp button
    class SignUpListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            UserSignUp signUp = new UserSignUp();
            signupcontroller sc = new signupcontroller(signUp);
            sc.open();
            userView.dispose();
        }
    }

    // Forgot Password button
    class ForgotPasswordListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            ForgotPassword fp = new ForgotPassword();
            ForgotPasswordController fpc = new ForgotPasswordController(fp);
            fpc.open();
            userView.dispose();
        }
    }
}