package View;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserBooksView extends javax.swing.JFrame {
    private final Model.userdata currentUser;

    // Sprint 3: Renew book button design
    // Sprint 4: Borrow history log tables
    public UserBooksView() {
        this.currentUser = null;
        initComponents();
        setupDesign();
    }

    public UserBooksView(Model.userdata user) {
        this.currentUser = user;
        initComponents();
        setupDesign();
    }

    private void setupDesign() {
        ViewUtils.applySharedDesign(this, sidebarPanel, mainPanel, bgLabel, btnLogout,
            new javax.swing.JButton[]{btnDashboard, btnCatalog, btnMyBooks, btnPayments}, btnMyBooks);

        pnlBorrowed.setBackground(new Color(40, 20, 10));
        pnlBorrowed.setAlpha(185);
        pnlReturned.setBackground(new Color(40, 20, 10));
        pnlReturned.setAlpha(185);
        pnlHistory.setBackground(new Color(40, 20, 10));
        pnlHistory.setAlpha(185);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ViewUtils.handleResize(UserBooksView.this, sidebarPanel, mainPanel, bgLabel, btnLogout);

                int w = getContentPane().getWidth();
                int h = getContentPane().getHeight();
                if (w <= 0 || h <= 0) return;

                int mainW = w - 220;
                int mainH = h;
                
                lblTitle.setBounds(30, 20, 400, 50);
                
                int paneW = mainW - 60;
                int paneH = mainH - 150;
                tabbedPane.setBounds(30, 90, paneW, paneH);
                
                // Adjust active borrows tab internal components
                scrollBorrowed.setBounds(10, 10, paneW - 25, paneH - 95);
                btnReturn.setBounds(10, paneH - 75, 160, 35);
                btnRenew.setBounds(185, paneH - 75, 200, 35);
                
                // Adjust returned tab
                scrollReturned.setBounds(10, 10, paneW - 25, paneH - 55);
                
                // Adjust history tab
                scrollHistory.setBounds(10, 10, paneW - 25, paneH - 55);
                
                revalidate();
                repaint();
            }
        });
    }

    // View Accessors for Clean Architecture
    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnCatalog() { return btnCatalog; }
    public JButton getBtnMyBooks() { return btnMyBooks; }
    public JButton getBtnPayments() { return btnPayments; }
    public JButton getBtnLogout() { return btnLogout; }
    
    public JTable getTblBorrowed() { return tblBorrowed; }
    public JTable getTblReturned() { return tblReturned; }
    public JTable getTblHistory() { return tblHistory; }
    
    public JButton getBtnReturn() { return btnReturn; }
    public JButton getBtnRenew() { return btnRenew; }
    public JTabbedPane getTabbedPane() { return tabbedPane; }

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
        tabbedPane = new javax.swing.JTabbedPane();
        pnlBorrowed = new View.TranslucentPanel();
        scrollBorrowed = new javax.swing.JScrollPane();
        tblBorrowed = new javax.swing.JTable();
        btnReturn = new javax.swing.JButton();
        btnRenew = new javax.swing.JButton();
        pnlReturned = new View.TranslucentPanel();
        scrollReturned = new javax.swing.JScrollPane();
        tblReturned = new javax.swing.JTable();
        pnlHistory = new View.TranslucentPanel();
        scrollHistory = new javax.swing.JScrollPane();
        tblHistory = new javax.swing.JTable();
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
        lblTitle.setText("My Books");
        mainPanel.add(lblTitle);
        lblTitle.setBounds(30, 20, 400, 50);

        tabbedPane.setFont(new java.awt.Font("Cambria Math", 1, 16)); // NOI18N

        pnlBorrowed.setLayout(null);

        tblBorrowed.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Borrow ID", "Book Title", "Author", "Borrow Date", "Due Date", "Renewals", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollBorrowed.setViewportView(tblBorrowed);

        pnlBorrowed.add(scrollBorrowed);
        scrollBorrowed.setBounds(10, 10, 900, 400);

        btnReturn.setBackground(new java.awt.Color(237, 226, 66));
        btnReturn.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        btnReturn.setText("Return Book");
        pnlBorrowed.add(btnReturn);
        btnReturn.setBounds(10, 420, 160, 35);

        btnRenew.setBackground(new java.awt.Color(237, 226, 66));
        btnRenew.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        btnRenew.setText("Renew Book (14 Days)");
        pnlBorrowed.add(btnRenew);
        btnRenew.setBounds(185, 420, 200, 35);

        tabbedPane.addTab("Borrowed", pnlBorrowed);

        pnlReturned.setLayout(null);

        tblReturned.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Borrow ID", "Book Title", "Author", "Borrow Date", "Due Date", "Return Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollReturned.setViewportView(tblReturned);

        pnlReturned.add(scrollReturned);
        scrollReturned.setBounds(10, 10, 900, 450);

        tabbedPane.addTab("Returned", pnlReturned);

        pnlHistory.setLayout(null);

        tblHistory.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Borrow ID", "Book Title", "Author", "Borrow Date", "Due Date", "Return Date", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollHistory.setViewportView(tblHistory);

        pnlHistory.add(scrollHistory);
        scrollHistory.setBounds(10, 10, 900, 450);

        tabbedPane.addTab("History", pnlHistory);

        mainPanel.add(tabbedPane);
        tabbedPane.setBounds(30, 90, 920, 520);

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
    private javax.swing.JButton btnRenew;
    private javax.swing.JButton btnReturn;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel mainPanel;
    private View.TranslucentPanel pnlBorrowed;
    private View.TranslucentPanel pnlHistory;
    private View.TranslucentPanel pnlReturned;
    private javax.swing.JScrollPane scrollBorrowed;
    private javax.swing.JScrollPane scrollHistory;
    private javax.swing.JScrollPane scrollReturned;
    private javax.swing.JTabbedPane tabbedPane;
    private View.TranslucentPanel sidebarPanel;
    private javax.swing.JTable tblBorrowed;
    private javax.swing.JTable tblHistory;
    private javax.swing.JTable tblReturned;
    // End of variables declaration//GEN-END:variables
}
