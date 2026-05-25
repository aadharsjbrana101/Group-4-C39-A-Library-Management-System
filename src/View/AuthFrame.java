package View;

import Model.Book;
import dao.bookcatalogDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.imageio.ImageIO;

/**
 * Premium Glassmorphic Authentication Frame for LMS.
 * Handles both Login and Signup with elegant design matching the LMS aesthetics.
 * @author Amanm
 */
public class AuthFrame extends JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AuthFrame.class.getName());
    private final bookcatalogDAO catalogDAO = new bookcatalogDAO();

    // UI Panels
    private BackgroundPanel backgroundPanel;
    private JPanel glassCard;
    private CardLayout cardLayout;
    private JPanel cardContainer;

    // Login components
    private JTextField loginUserField;
    private JPasswordField loginPassField;
    private JButton btnLogin;
    private JLabel linkToRegister;

    // Register components
    private JTextField regUserField;
    private JTextField regEmailField;
    private JPasswordField regPassField;
    private JPasswordField regConfirmPassField;
    private JButton btnRegister;
    private JLabel linkToLogin;

    // Message notification overlay
    private JLabel errorLabel;

    public AuthFrame() {
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setTitle("LMS - High Fidelity Authentication");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main Background Panel
        backgroundPanel = new BackgroundPanel();
        setContentPane(backgroundPanel);

        // Error message label at top
        errorLabel = new JLabel("", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        errorLabel.setForeground(new Color(231, 76, 60));
        errorLabel.setBounds(100, 10, 600, 30);
        backgroundPanel.add(errorLabel);

        // Glassmorphic central card
        glassCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Semi-transparent black card back
                g2d.setColor(new Color(0, 0, 0, 180));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                // Gold Accent Border
                g2d.setColor(new Color(241, 196, 15));
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 30, 30);

                g2d.dispose();
            }
        };
        glassCard.setOpaque(false);
        glassCard.setBounds(175, 60, 450, 440);
        backgroundPanel.add(glassCard);

        // Header Title in central card
        JLabel systemTitle = new JLabel("LIBRARY MANAGEMENT SYSTEM");
        systemTitle.setFont(new Font("Cambria", Font.BOLD, 22));
        systemTitle.setForeground(new Color(241, 196, 15));
        systemTitle.setHorizontalAlignment(SwingConstants.CENTER);
        systemTitle.setBounds(20, 20, 410, 35);
        glassCard.add(systemTitle);

        // CardLayout Panel container inside Glass Card
        cardLayout = new CardLayout();
        cardContainer = new JPanel(cardLayout);
        cardContainer.setOpaque(false);
        cardContainer.setBounds(20, 70, 410, 350);
        glassCard.add(cardContainer);

        // Create the Login & Register Cards
        createLoginCard();
        createRegisterCard();

        cardLayout.show(cardContainer, "Login");
    }

    private void createLoginCard() {
        JPanel loginPanel = new JPanel(null);
        loginPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Sign In");
        lblTitle.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(10, 5, 390, 35);
        loginPanel.add(lblTitle);

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblUser.setForeground(new Color(200, 200, 200));
        lblUser.setBounds(10, 60, 390, 20);
        loginPanel.add(lblUser);

        loginUserField = createStyledTextField("Enter Username");
        loginUserField.setBounds(10, 85, 390, 40);
        loginPanel.add(loginUserField);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPass.setForeground(new Color(200, 200, 200));
        lblPass.setBounds(10, 135, 390, 20);
        loginPanel.add(lblPass);

        loginPassField = createStyledPasswordField();
        loginPassField.setBounds(10, 160, 390, 40);
        loginPanel.add(loginPassField);

        btnLogin = new JButton("Login") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(241, 196, 15), getWidth(), getHeight(), new Color(212, 172, 13));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnLogin.setForeground(new Color(30, 30, 30));
        btnLogin.setContentAreaFilled(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(null);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setBounds(10, 230, 390, 45);
        loginPanel.add(btnLogin);

        linkToRegister = new JLabel("Don't have an account? Sign Up", SwingConstants.CENTER);
        linkToRegister.setFont(new Font("Segoe UI", Font.BOLD, 13));
        linkToRegister.setForeground(new Color(241, 196, 15));
        linkToRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkToRegister.setBounds(10, 290, 390, 25);
        loginPanel.add(linkToRegister);

        // Listeners
        linkToRegister.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showMsg("");
                cardLayout.show(cardContainer, "Register");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                linkToRegister.setText("<html><u>Don't have an account? Sign Up</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                linkToRegister.setText("Don't have an account? Sign Up");
            }
        });

        btnLogin.addActionListener(e -> handleLogin());

        cardContainer.add(loginPanel, "Login");
    }

    private void createRegisterCard() {
        JPanel regPanel = new JPanel(null);
        regPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("Create Account");
        lblTitle.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 24));
        lblTitle.setForeground(Color.WHITE);
        lblTitle.setBounds(10, 5, 390, 30);
        regPanel.add(lblTitle);

        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblUser.setForeground(new Color(200, 200, 200));
        lblUser.setBounds(10, 40, 190, 15);
        regPanel.add(lblUser);

        regUserField = createStyledTextField("Enter Username");
        regUserField.setBounds(10, 60, 190, 35);
        regPanel.add(regUserField);

        JLabel lblEmail = new JLabel("Email Address");
        lblEmail.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblEmail.setForeground(new Color(200, 200, 200));
        lblEmail.setBounds(210, 40, 190, 15);
        regPanel.add(lblEmail);

        regEmailField = createStyledTextField("Enter Email");
        regEmailField.setBounds(210, 60, 190, 35);
        regPanel.add(regEmailField);

        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblPass.setForeground(new Color(200, 200, 200));
        lblPass.setBounds(10, 110, 390, 15);
        regPanel.add(lblPass);

        regPassField = createStyledPasswordField();
        regPassField.setBounds(10, 130, 390, 35);
        regPanel.add(regPassField);

        JLabel lblConfirm = new JLabel("Confirm Password");
        lblConfirm.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblConfirm.setForeground(new Color(200, 200, 200));
        lblConfirm.setBounds(10, 180, 390, 15);
        regPanel.add(lblConfirm);

        regConfirmPassField = createStyledPasswordField();
        regConfirmPassField.setBounds(10, 200, 390, 35);
        regPanel.add(regConfirmPassField);

        btnRegister = new JButton("Sign Up") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(241, 196, 15), getWidth(), getHeight(), new Color(212, 172, 13));
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btnRegister.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btnRegister.setForeground(new Color(30, 30, 30));
        btnRegister.setContentAreaFilled(false);
        btnRegister.setFocusPainted(false);
        btnRegister.setBorder(null);
        btnRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnRegister.setBounds(10, 255, 390, 42);
        regPanel.add(btnRegister);

        linkToLogin = new JLabel("Already have an account? Sign In", SwingConstants.CENTER);
        linkToLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        linkToLogin.setForeground(new Color(241, 196, 15));
        linkToLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        linkToLogin.setBounds(10, 310, 390, 25);
        regPanel.add(linkToLogin);

        // Listeners
        linkToLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showMsg("");
                cardLayout.show(cardContainer, "Login");
            }
            @Override
            public void mouseEntered(MouseEvent e) {
                linkToLogin.setText("<html><u>Already have an account? Sign In</u></html>");
            }
            @Override
            public void mouseExited(MouseEvent e) {
                linkToLogin.setText("Already have an account? Sign In");
            }
        });

        btnRegister.addActionListener(e -> handleRegister());

        cardContainer.add(regPanel, "Register");
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField tf = new JTextField(placeholder) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setForeground(new Color(180, 180, 180));
        tf.setCaretColor(Color.WHITE);
        tf.setOpaque(false);
        tf.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));

        tf.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) {
                    tf.setText("");
                    tf.setForeground(Color.WHITE);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) {
                    tf.setText(placeholder);
                    tf.setForeground(new Color(180, 180, 180));
                }
            }
        });
        return tf;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField pf = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 20));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.setColor(new Color(255, 255, 255, 40));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        pf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        pf.setForeground(Color.WHITE);
        pf.setCaretColor(Color.WHITE);
        pf.setOpaque(false);
        pf.setBorder(BorderFactory.createEmptyBorder(0, 12, 0, 12));
        return pf;
    }

    private void handleLogin() {
        String username = loginUserField.getText().trim();
        String password = new String(loginPassField.getPassword()).trim();

        if (username.isEmpty() || username.equals("Enter Username") || password.isEmpty()) {
            showMsg("Please enter both username and password!");
            return;
        }

        showMsg("Connecting...");

        // Authenticate in database
        int userId = catalogDAO.authenticateUser(username, password);
        if (userId == -1) {
            showMsg("Invalid Username or Password!");
        } else if (userId == -999) {
            // Logged in as administrator!
            JOptionPane.showMessageDialog(this, "Welcome, Librarian!", "Access Granted", JOptionPane.INFORMATION_MESSAGE);
            new AdminFrame().setVisible(true);
            this.dispose();
        } else {
            // Logged in as user!
            JOptionPane.showMessageDialog(this, "Login Successful! Welcome " + username, "LMS Access", JOptionPane.INFORMATION_MESSAGE);
            new UserCaataalog(userId, username).setVisible(true);
            this.dispose();
        }
    }

    private void handleRegister() {
        String username = regUserField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = new String(regPassField.getPassword()).trim();
        String confirm = new String(regConfirmPassField.getPassword()).trim();

        if (username.isEmpty() || username.equals("Enter Username") ||
            email.isEmpty() || email.equals("Enter Email") ||
            password.isEmpty() || confirm.isEmpty()) {
            showMsg("All fields are required!");
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            showMsg("Please enter a valid email address!");
            return;
        }

        if (!password.equals(confirm)) {
            showMsg("Passwords do not match!");
            return;
        }

        if (password.length() < 4) {
            showMsg("Password must be at least 4 characters!");
            return;
        }

        // Register in database
        boolean ok = catalogDAO.registerUser(username, password, email);
        if (ok) {
            JOptionPane.showMessageDialog(this, "Account created successfully! Please Sign In.", "Account Registered", JOptionPane.INFORMATION_MESSAGE);
            cardLayout.show(cardContainer, "Login");
            loginUserField.setText(username);
            loginUserField.setForeground(Color.WHITE);
            loginPassField.requestFocus();
            showMsg("");
        } else {
            showMsg("Username already exists or Registration failed!");
        }
    }

    private void showMsg(String text) {
        errorLabel.setText(text);
    }

    // Helper method to load scaled images safely
    private Image getScaledImage(String path, int width, int height) {
        try {
            java.net.URL imgUrl = getClass().getResource(path);
            if (imgUrl != null) {
                Image img = new ImageIcon(imgUrl).getImage();
                return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            } else {
                File file = new File("src" + path);
                if (file.exists()) {
                    Image img = ImageIO.read(file);
                    return img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
                }
            }
        } catch (Exception e) {
            logger.log(java.util.logging.Level.FINE, "Failed to load: " + path);
        }
        return null;
    }

    // Inner class for background rendering
    private class BackgroundPanel extends JPanel {
        private final Image bgImage;

        public BackgroundPanel() {
            setLayout(null);
            bgImage = getScaledImage("/images/Untitled design.png", 800, 600);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            if (bgImage != null) {
                g2d.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                GradientPaint gp = new GradientPaint(0, 0, new Color(20, 24, 33), getWidth(), getHeight(), new Color(10, 12, 16));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }

            // High-contrast background overlay
            g2d.setColor(new Color(0, 0, 0, 90));
            g2d.fillRect(0, 0, getWidth(), getHeight());

            g2d.dispose();
        }
    }
}
