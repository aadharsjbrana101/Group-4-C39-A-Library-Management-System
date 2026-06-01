package View;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminDashboard extends javax.swing.JFrame {
    private final Model.userdata currentAdmin;

    public AdminDashboard() {
        this.currentAdmin = null;
        initComponents();
        setupDesign();
    }

    public AdminDashboard(Model.userdata admin) {
        this.currentAdmin = admin;
        initComponents();
        setupDesign();
    }

    private void setupDesign() {
        ViewUtils.applySharedDesign(this, sidebarPanel, mainPanel, bgLabel, btnLogout,
            new javax.swing.JButton[]{btnDashboard, btnCatalog, btnUsers, btnPayments}, btnDashboard);

        cardBooks.setBackground(Color.WHITE);
        cardBooks.setAlpha(220);
        cardUsers.setBackground(Color.WHITE);
        cardUsers.setAlpha(220);
        cardIssued.setBackground(Color.WHITE);
        cardIssued.setAlpha(220);
        cardFines.setBackground(Color.WHITE);
        cardFines.setAlpha(220);
        logPanel.setBackground(new Color(40, 20, 10));
        logPanel.setAlpha(185);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ViewUtils.handleResize(AdminDashboard.this, sidebarPanel, mainPanel, bgLabel, btnLogout);

                int w = getContentPane().getWidth();
                int h = getContentPane().getHeight();
                if (w <= 0 || h <= 0) return;

                int mainW = w - 220;
                int mainH = h;
                
                lblTitle.setBounds(30, 20, 400, 50);
                
                int panelW = mainW - 60;
                statsPanel.setBounds(30, 100, panelW, 150);
                int cardW = Math.max(100, (panelW - 50) / 4);
                cardBooks.setBounds(0, 10, cardW, 120);
                cardUsers.setBounds(cardW + 15, 10, cardW, 120);
                cardIssued.setBounds((cardW * 2) + 30, 10, cardW, 120);
                cardFines.setBounds((cardW * 3) + 45, 10, cardW, 120);
                
                lblRecentHeader.setBounds(30, 270, 300, 30);
                int logH = mainH - 350;
                logPanel.setBounds(30, 310, panelW, logH);
                scrollActivities.setBounds(15, 15, panelW - 30, logH - 30);
                
                revalidate();
                repaint();
            }
        });
    }

    // View Accessors for Clean Architecture
    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnCatalog() { return btnCatalog; }
    public JButton getBtnUsers() { return btnUsers; }
    public JButton getBtnPayments() { return btnPayments; }
    public JButton getBtnLogout() { return btnLogout; }
    
    public JLabel getLblBooksVal() { return lblBooksVal; }
    public JLabel getLblUsersVal() { return lblUsersVal; }
    public JLabel getLblIssuedVal() { return lblIssuedVal; }
    public JLabel getLblFinesVal() { return lblFinesVal; }
    public JList<String> getLstActivities() { return lstActivities; }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebarPanel = new View.TranslucentPanel();
        btnDashboard = new javax.swing.JButton();
        btnCatalog = new javax.swing.JButton();
        btnUsers = new javax.swing.JButton();
        btnPayments = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        cardBooks = new View.TranslucentPanel();
        lblBooksTitle = new javax.swing.JLabel();
        lblBooksVal = new javax.swing.JLabel();
        cardUsers = new View.TranslucentPanel();
        lblUsersTitle = new javax.swing.JLabel();
        lblUsersVal = new javax.swing.JLabel();
        cardIssued = new View.TranslucentPanel();
        lblIssuedTitle = new javax.swing.JLabel();
        lblIssuedVal = new javax.swing.JLabel();
        cardFines = new View.TranslucentPanel();
        lblFinesTitle = new javax.swing.JLabel();
        lblFinesVal = new javax.swing.JLabel();
        lblRecentHeader = new javax.swing.JLabel();
        logPanel = new View.TranslucentPanel();
        scrollActivities = new javax.swing.JScrollPane();
        lstActivities = new javax.swing.JList<>();
        bgLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 700));
        getContentPane().setLayout(null);

        sidebarPanel.setOpaque(false);
        sidebarPanel.setLayout(null);

        btnDashboard.setFont(new java.awt.Font("Cambria Math", 3, 18)); // NOI18N
        btnDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard.setText("🏠 Dashboard");
        btnDashboard.setContentAreaFilled(false);
        btnDashboard.setFocusPainted(false);
        btnDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sidebarPanel.add(btnDashboard);
        btnDashboard.setBounds(10, 50, 200, 45);

        btnCatalog.setFont(new java.awt.Font("Cambria Math", 3, 18)); // NOI18N
        btnCatalog.setForeground(new java.awt.Color(255, 255, 255));
        btnCatalog.setText("📚 Catalog");
        btnCatalog.setContentAreaFilled(false);
        btnCatalog.setFocusPainted(false);
        btnCatalog.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sidebarPanel.add(btnCatalog);
        btnCatalog.setBounds(10, 110, 200, 45);

        btnUsers.setFont(new java.awt.Font("Cambria Math", 3, 18)); // NOI18N
        btnUsers.setForeground(new java.awt.Color(255, 255, 255));
        btnUsers.setText("👥 Manage Users");
        btnUsers.setContentAreaFilled(false);
        btnUsers.setFocusPainted(false);
        btnUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sidebarPanel.add(btnUsers);
        btnUsers.setBounds(10, 170, 200, 45);

        btnPayments.setFont(new java.awt.Font("Cambria Math", 3, 18)); // NOI18N
        btnPayments.setForeground(new java.awt.Color(255, 255, 255));
        btnPayments.setText("💳 Payments");
        btnPayments.setContentAreaFilled(false);
        btnPayments.setFocusPainted(false);
        btnPayments.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sidebarPanel.add(btnPayments);
        btnPayments.setBounds(10, 230, 200, 45);

        btnLogout.setFont(new java.awt.Font("Cambria Math", 3, 18)); // NOI18N
        btnLogout.setForeground(new java.awt.Color(255, 255, 255));
        btnLogout.setText("🚪 Logout");
        btnLogout.setContentAreaFilled(false);
        btnLogout.setFocusPainted(false);
        btnLogout.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sidebarPanel.add(btnLogout);
        btnLogout.setBounds(10, 600, 200, 45);

        getContentPane().add(sidebarPanel);
        sidebarPanel.setBounds(0, 0, 220, 700);

        mainPanel.setOpaque(false);
        mainPanel.setLayout(null);

        lblTitle.setFont(new java.awt.Font("Cambria Math", 1, 42)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Admin Dashboard");
        mainPanel.add(lblTitle);
        lblTitle.setBounds(30, 20, 400, 50);

        statsPanel.setOpaque(false);
        statsPanel.setLayout(null);

        cardBooks.setBackground(new java.awt.Color(255, 255, 255));
        cardBooks.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardBooks.setLayout(null);

        lblBooksTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblBooksTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblBooksTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBooksTitle.setText("Total Books");
        cardBooks.add(lblBooksTitle);
        lblBooksTitle.setBounds(10, 15, 180, 20);

        lblBooksVal.setFont(new java.awt.Font("Cambria Math", 1, 32)); // NOI18N
        lblBooksVal.setForeground(new java.awt.Color(102, 81, 0));
        lblBooksVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBooksVal.setText("0");
        cardBooks.add(lblBooksVal);
        lblBooksVal.setBounds(10, 45, 180, 50);

        statsPanel.add(cardBooks);
        cardBooks.setBounds(10, 10, 200, 120);

        cardUsers.setBackground(new java.awt.Color(255, 255, 255));
        cardUsers.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardUsers.setLayout(null);

        lblUsersTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblUsersTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblUsersTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsersTitle.setText("Total Users");
        cardUsers.add(lblUsersTitle);
        lblUsersTitle.setBounds(10, 15, 180, 20);

        lblUsersVal.setFont(new java.awt.Font("Cambria Math", 1, 32)); // NOI18N
        lblUsersVal.setForeground(new java.awt.Color(102, 81, 0));
        lblUsersVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblUsersVal.setText("0");
        cardUsers.add(lblUsersVal);
        lblUsersVal.setBounds(10, 45, 180, 50);

        statsPanel.add(cardUsers);
        cardUsers.setBounds(235, 10, 200, 120);

        cardIssued.setBackground(new java.awt.Color(255, 255, 255));
        cardIssued.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardIssued.setLayout(null);

        lblIssuedTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblIssuedTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblIssuedTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIssuedTitle.setText("Issued Books");
        cardIssued.add(lblIssuedTitle);
        lblIssuedTitle.setBounds(10, 15, 180, 20);

        lblIssuedVal.setFont(new java.awt.Font("Cambria Math", 1, 32)); // NOI18N
        lblIssuedVal.setForeground(new java.awt.Color(102, 81, 0));
        lblIssuedVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIssuedVal.setText("0");
        cardIssued.add(lblIssuedVal);
        lblIssuedVal.setBounds(10, 45, 180, 50);

        statsPanel.add(cardIssued);
        cardIssued.setBounds(460, 10, 200, 120);

        cardFines.setBackground(new java.awt.Color(255, 255, 255));
        cardFines.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardFines.setLayout(null);

        lblFinesTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblFinesTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblFinesTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFinesTitle.setText("Pending Fines");
        cardFines.add(lblFinesTitle);
        lblFinesTitle.setBounds(10, 15, 180, 20);

        lblFinesVal.setFont(new java.awt.Font("Cambria Math", 1, 24)); // NOI18N
        lblFinesVal.setForeground(new java.awt.Color(204, 0, 0));
        lblFinesVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFinesVal.setText("Rs. 0.00");
        cardFines.add(lblFinesVal);
        lblFinesVal.setBounds(10, 45, 180, 50);

        statsPanel.add(cardFines);
        cardFines.setBounds(685, 10, 200, 120);

        mainPanel.add(statsPanel);
        statsPanel.setBounds(30, 100, 920, 150);

        lblRecentHeader.setFont(new java.awt.Font("Cambria Math", 2, 22)); // NOI18N
        lblRecentHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblRecentHeader.setText("Recent Activities");
        mainPanel.add(lblRecentHeader);
        lblRecentHeader.setBounds(30, 270, 300, 30);

        logPanel.setBackground(new java.awt.Color(102, 51, 0));
        logPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 5));
        logPanel.setLayout(null);

        lstActivities.setFont(new java.awt.Font("Monospaced", 0, 14)); // NOI18N
        scrollActivities.setViewportView(lstActivities);

        logPanel.add(scrollActivities);
        scrollActivities.setBounds(15, 15, 890, 300);

        mainPanel.add(logPanel);
        logPanel.setBounds(30, 310, 920, 330);

        getContentPane().add(mainPanel);
        mainPanel.setBounds(220, 30, 980, 670);

        bgLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Untitled design (5).png"))); // NOI18N
        getContentPane().add(bgLabel);
        bgLabel.setBounds(0, 0, 1200, 700);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bgLabel;
    private javax.swing.JButton btnCatalog;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPayments;
    private javax.swing.JButton btnUsers;
    private View.TranslucentPanel cardBooks;
    private View.TranslucentPanel cardFines;
    private View.TranslucentPanel cardIssued;
    private View.TranslucentPanel cardUsers;
    private javax.swing.JLabel lblBooksTitle;
    private javax.swing.JLabel lblBooksVal;
    private javax.swing.JLabel lblFinesTitle;
    private javax.swing.JLabel lblFinesVal;
    private javax.swing.JLabel lblIssuedTitle;
    private javax.swing.JLabel lblIssuedVal;
    private javax.swing.JLabel lblRecentHeader;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsersTitle;
    private javax.swing.JLabel lblUsersVal;
    private javax.swing.JList<String> lstActivities;
    private javax.swing.JPanel mainPanel;
    private View.TranslucentPanel logPanel;
    private javax.swing.JScrollPane scrollActivities;
    private View.TranslucentPanel sidebarPanel;
    private javax.swing.JPanel statsPanel;
    // End of variables declaration//GEN-END:variables
}
