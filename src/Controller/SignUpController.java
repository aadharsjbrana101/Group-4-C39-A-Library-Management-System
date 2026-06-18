package Controller;

import dao.UserDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import Model.UserData;
import View.UserSignUp;
import View.UserLogin;

public class SignUpController {
    private final UserDao userDao = new UserDao();
    private final UserSignUp userView;

    public SignUpController(UserSignUp userView) {
        this.userView = userView;
        userView.addCreateAccountListener(new CreateAccountListener());
        userView.addLoginListener(new GoToLoginListener());
    }

    public void open() {
        this.userView.setVisible(true);
    }

    public void close() {
        this.userView.dispose();
    }

    // Create Account button
    class CreateAccountListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String username = userView.getNameField().getText().trim();
                String email = userView.getEmailField().getText().trim();
                String password = new String(userView.getPasswordField().getPassword()).trim();
                String confirmPassword = new String(userView.getConfirmPasswordField().getPassword()).trim();

                System.out.println("Button clicked!");
                System.out.println("Username: " + username);
                System.out.println("Email: " + email);
                System.out.println("Password: " + password);

                if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                    JOptionPane.showMessageDialog(userView, "Please fill in all fields.");
                    return;
                }

                if (!password.equals(confirmPassword)) {
                    JOptionPane.showMessageDialog(userView, "Passwords do not match.");
                    return;
                }

                UserData user = new UserData(username, email, password);
                boolean exists = userDao.checkUser(user);

                if (exists) {
                    JOptionPane.showMessageDialog(userView, "Username or email already exists.");
                } else {
                    boolean success = userDao.signUp(user);
                    if (success) {
                        JOptionPane.showMessageDialog(userView, "Account created successfully! Please login.");
                        UserLogin login = new UserLogin();
                        LoginController lc = new LoginController(login);
                        lc.open();
                        userView.dispose();
                    } else {
                        JOptionPane.showMessageDialog(userView, "Failed to create account. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
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

    // Login button
    class GoToLoginListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            UserLogin login = new UserLogin();
            LoginController lc = new LoginController(login);
            lc.open();
            userView.dispose();
        }
    }
}