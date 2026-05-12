package View;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserDashboard extends javax.swing.JFrame {

    // Sprint 1 (Kushal): Add fee display containers on UI views
    public UserDashboard() {
        initComponents();
        setupDesign();
    }

    public UserDashboard(Model.userdata user) {
        initComponents();
        setupDesign();
        if (user != null) {
            lblGreeting.setText("Welcome, " + user.getUsername() + "!");
        }
    }

    private void setupDesign() {
        ViewUtils.applySharedDesign(this, sidebarPanel, mainPanel, bgLabel, btnLogout,
            new javax.swing.JButton[]{btnDashboard, btnCatalog, btnMyBooks, btnPayments}, btnDashboard);

        cardBorrowed.setBackground(Color.WHITE);
        cardBorrowed.setAlpha(220);
        cardActive.setBackground(Color.WHITE);
        cardActive.setAlpha(220);
        cardFines.setBackground(Color.WHITE);
        cardFines.setAlpha(220);
        quickSearchPanel.setBackground(Color.WHITE);
        quickSearchPanel.setAlpha(195);
        
        recentScrollPane.getViewport().setOpaque(false);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ViewUtils.handleResize(UserDashboard.this, sidebarPanel, mainPanel, bgLabel, btnLogout);

                int w = getContentPane().getWidth();
                int h = getContentPane().getHeight();
                if (w <= 0 || h <= 0) return;

                int mainW = w - 220;
                int contentW = mainW - 60; 
                
                lblTitle.setBounds(30, 20, 400, 50);
                lblGreeting.setBounds(30, 70, 400, 30);
                
                // Adjust stats boxes
                statsPanel.setBounds(30, 110, contentW - 260, 150);
                int cardW = Math.max(120, (statsPanel.getWidth() - 40) / 3);
                cardBorrowed.setBounds(0, 10, cardW, 120);
                cardActive.setBounds(cardW + 15, 10, cardW, 120);
                cardFines.setBounds((cardW * 2) + 30, 10, cardW, 120);

                quickSearchPanel.setBounds(mainW - 250, 110, 230, h - 150);
                lblQuickSearch.setBounds(15, 15, 200, 25);
                txtQuickSearch.setBounds(15, 50, 150, 30);
                btnQuickSearch.setBounds(170, 50, 45, 30);
                resultsScrollPane.setBounds(15, 90, 200, quickSearchPanel.getHeight() - 110);
                
                lblRecentHeader.setBounds(30, 270, 400, 30);
                recentScrollPane.setBounds(30, 310, contentW - 260, h - 350);
                
                revalidate();
                repaint();
            }
        });
    }

    // Clean Architecture variables accessors
    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnCatalog() { return btnCatalog; }
    public JButton getBtnMyBooks() { return btnMyBooks; }
    public JButton getBtnPayments() { return btnPayments; }
    public JButton getBtnLogout() { return btnLogout; }
    public JLabel getLblGreeting() { return lblGreeting; }
    
    public JLabel getLblBorrowedVal() { return lblBorrowedVal; }
    public JLabel getLblActiveVal() { return lblActiveVal; }
    public JLabel getLblFinesVal() { return lblFinesVal; }
    
    public JTextField getTxtQuickSearch() { return txtQuickSearch; }
    public JButton getBtnQuickSearch() { return btnQuickSearch; }
    public JList<String> getLstQuickResults() { return lstQuickResults; }
    public JPanel getRecentContainer() { return recentContainer; }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sidebarPanel = new View.TranslucentPanel();
        btnDashboard = new javax.swing.JButton();
        btnCatalog = new javax.swing.JButton();
        btnMyBooks = new javax.swing.JButton();
        btnPayments = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        lblGreeting = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        cardBorrowed = new View.TranslucentPanel();
        lblBorrowedTitle = new javax.swing.JLabel();
        lblBorrowedVal = new javax.swing.JLabel();
        cardActive = new View.TranslucentPanel();
        lblActiveTitle = new javax.swing.JLabel();
        lblActiveVal = new javax.swing.JLabel();
        cardFines = new View.TranslucentPanel();
        lblFinesTitle = new javax.swing.JLabel();
        lblFinesVal = new javax.swing.JLabel();
        lblRecentHeader = new javax.swing.JLabel();
        recentScrollPane = new javax.swing.JScrollPane();
        recentContainer = new javax.swing.JPanel();
        quickSearchPanel = new View.TranslucentPanel();
        lblQuickSearch = new javax.swing.JLabel();
        txtQuickSearch = new javax.swing.JTextField();
        btnQuickSearch = new javax.swing.JButton();
        resultsScrollPane = new javax.swing.JScrollPane();
        lstQuickResults = new javax.swing.JList<>();
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

        btnMyBooks.setFont(new java.awt.Font("Cambria Math", 3, 18)); // NOI18N
        btnMyBooks.setForeground(new java.awt.Color(255, 255, 255));
        btnMyBooks.setText("📖 My Books");
        btnMyBooks.setContentAreaFilled(false);
        btnMyBooks.setFocusPainted(false);
        btnMyBooks.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sidebarPanel.add(btnMyBooks);
        btnMyBooks.setBounds(10, 170, 200, 45);

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
        lblTitle.setText("User Dashboard");
        mainPanel.add(lblTitle);
        lblTitle.setBounds(30, 20, 400, 50);

        lblGreeting.setFont(new java.awt.Font("Cambria Math", 2, 20)); // NOI18N
        lblGreeting.setForeground(new java.awt.Color(238, 238, 238));
        lblGreeting.setText("Welcome, user!");
        mainPanel.add(lblGreeting);
        lblGreeting.setBounds(30, 70, 400, 30);

        statsPanel.setOpaque(false);
        statsPanel.setLayout(null);

        cardBorrowed.setBackground(new java.awt.Color(255, 255, 255));
        cardBorrowed.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardBorrowed.setLayout(null);

        lblBorrowedTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblBorrowedTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblBorrowedTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBorrowedTitle.setText("Total Borrowed");
        cardBorrowed.add(lblBorrowedTitle);
        lblBorrowedTitle.setBounds(10, 15, 170, 20);

        lblBorrowedVal.setFont(new java.awt.Font("Cambria Math", 1, 32)); // NOI18N
        lblBorrowedVal.setForeground(new java.awt.Color(102, 81, 0));
        lblBorrowedVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblBorrowedVal.setText("0");
        cardBorrowed.add(lblBorrowedVal);
        lblBorrowedVal.setBounds(10, 45, 170, 50);

        statsPanel.add(cardBorrowed);
        cardBorrowed.setBounds(10, 10, 190, 120);

        cardActive.setBackground(new java.awt.Color(255, 255, 255));
        cardActive.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardActive.setLayout(null);

        lblActiveTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblActiveTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblActiveTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblActiveTitle.setText("Active Borrows");
        cardActive.add(lblActiveTitle);
        lblActiveTitle.setBounds(10, 15, 170, 20);

        lblActiveVal.setFont(new java.awt.Font("Cambria Math", 1, 32)); // NOI18N
        lblActiveVal.setForeground(new java.awt.Color(102, 81, 0));
        lblActiveVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblActiveVal.setText("0");
        cardActive.add(lblActiveVal);
        lblActiveVal.setBounds(10, 45, 170, 50);

        statsPanel.add(cardActive);
        cardActive.setBounds(220, 10, 190, 120);

        cardFines.setBackground(new java.awt.Color(255, 255, 255));
        cardFines.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardFines.setLayout(null);

        lblFinesTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblFinesTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblFinesTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFinesTitle.setText("Fines Pending");
        cardFines.add(lblFinesTitle);
        lblFinesTitle.setBounds(10, 15, 170, 20);

        lblFinesVal.setFont(new java.awt.Font("Cambria Math", 1, 26)); // NOI18N
        lblFinesVal.setForeground(new java.awt.Color(204, 0, 0));
        lblFinesVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblFinesVal.setText("Rs. 0.00");
        cardFines.add(lblFinesVal);
        lblFinesVal.setBounds(10, 45, 170, 50);

        statsPanel.add(cardFines);
        cardFines.setBounds(430, 10, 190, 120);

        mainPanel.add(statsPanel);
        statsPanel.setBounds(30, 110, 670, 150);

        lblRecentHeader.setFont(new java.awt.Font("Cambria Math", 2, 22)); // NOI18N
        lblRecentHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblRecentHeader.setText("Recently Added Books");
        mainPanel.add(lblRecentHeader);
        lblRecentHeader.setBounds(30, 270, 300, 30);

        recentScrollPane.setBorder(null);
        recentScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        recentScrollPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        recentScrollPane.setOpaque(false);

        recentContainer.setOpaque(false);
        recentContainer.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 15, 15));
        recentScrollPane.setViewportView(recentContainer);

        mainPanel.add(recentScrollPane);
        recentScrollPane.setBounds(30, 310, 670, 330);

        quickSearchPanel.setBackground(new java.awt.Color(255, 255, 255));
        quickSearchPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        quickSearchPanel.setLayout(null);

        lblQuickSearch.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        lblQuickSearch.setForeground(new java.awt.Color(51, 51, 51));
        lblQuickSearch.setText("Quick Search");
        quickSearchPanel.add(lblQuickSearch);
        lblQuickSearch.setBounds(15, 15, 210, 25);
        quickSearchPanel.add(txtQuickSearch);
        txtQuickSearch.setBounds(15, 50, 165, 30);

        btnQuickSearch.setBackground(new java.awt.Color(237, 226, 66));
        btnQuickSearch.setText("🔍");
        quickSearchPanel.add(btnQuickSearch);
        btnQuickSearch.setBounds(185, 50, 40, 30);

        lstQuickResults.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        resultsScrollPane.setViewportView(lstQuickResults);

        quickSearchPanel.add(resultsScrollPane);
        resultsScrollPane.setBounds(15, 90, 210, 425);

        mainPanel.add(quickSearchPanel);
        quickSearchPanel.setBounds(710, 110, 240, 530);

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
    private javax.swing.JButton btnMyBooks;
    private javax.swing.JButton btnPayments;
    private javax.swing.JButton btnQuickSearch;
    private View.TranslucentPanel cardActive;
    private View.TranslucentPanel cardBorrowed;
    private View.TranslucentPanel cardFines;
    private javax.swing.JLabel lblActiveTitle;
    private javax.swing.JLabel lblActiveVal;
    private javax.swing.JLabel lblBorrowedTitle;
    private javax.swing.JLabel lblBorrowedVal;
    private javax.swing.JLabel lblFinesTitle;
    private javax.swing.JLabel lblFinesVal;
    private javax.swing.JLabel lblGreeting;
    private javax.swing.JLabel lblQuickSearch;
    private javax.swing.JLabel lblRecentHeader;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JList<String> lstQuickResults;
    private javax.swing.JPanel mainPanel;
    private View.TranslucentPanel quickSearchPanel;
    private javax.swing.JPanel recentContainer;
    private javax.swing.JScrollPane recentScrollPane;
    private javax.swing.JScrollPane resultsScrollPane;
    private View.TranslucentPanel sidebarPanel;
    private javax.swing.JPanel statsPanel;
    private javax.swing.JTextField txtQuickSearch;
    // End of variables declaration//GEN-END:variables
}
