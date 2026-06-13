package View;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class UserPaymentsView extends javax.swing.JFrame {
    private final Model.UserData currentUser;

    public UserPaymentsView() {
        this.currentUser = null;
        initComponents();
        setupDesign();
    }

    public UserPaymentsView(Model.UserData user) {
        this.currentUser = user;
        initComponents();
        setupDesign();
    }

    private void setupDesign() {
        ViewUtils.applySharedDesign(this, sidebarPanel, mainPanel, bgLabel, btnLogout,
            new javax.swing.JButton[]{btnDashboard, btnCatalog, btnMyBooks, btnPayments}, btnPayments);

        cardTotal.setBackground(Color.WHITE);
        cardTotal.setAlpha(220);
        cardPending.setBackground(Color.WHITE);
        cardPending.setAlpha(220);
        cardPaid.setBackground(Color.WHITE);
        cardPaid.setAlpha(220);
        tablePanel.setBackground(new Color(40, 20, 10));
        tablePanel.setAlpha(185);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ViewUtils.handleResize(UserPaymentsView.this, sidebarPanel, mainPanel, bgLabel, btnLogout);

                int w = getContentPane().getWidth();
                int h = getContentPane().getHeight();
                if (w <= 0 || h <= 0) return;

                int mainW = w - 220;
                int mainH = h;
                
                lblTitle.setBounds(30, 20, 400, 50);
                lblDetails.setBounds(30, 70, 100, 30);
                
                int panelW = mainW - 60;
                statsPanel.setBounds(30, 110, panelW, 150);
                int cardW = Math.max(120, (panelW - 40) / 3);
                cardTotal.setBounds(0, 10, cardW, 120);
                cardPending.setBounds(cardW + 15, 10, cardW, 120);
                cardPaid.setBounds((cardW * 2) + 30, 10, cardW, 120);
                
                int tblPanelH = mainH - 290;
                tablePanel.setBounds(30, 270, panelW, tblPanelH);
                scrollPayments.setBounds(15, 15, panelW - 30, tblPanelH - 70);
                btnPay.setBounds(15, tblPanelH - 45, 180, 35);
                
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
    
    public JLabel getLblTotalVal() { return lblTotalVal; }
    public JLabel getLblPendingVal() { return lblPendingVal; }
    public JLabel getLblPaidVal() { return lblPaidVal; }
    public JTable getTblPayments() { return tblPayments; }
    public JButton getBtnPay() { return btnPay; }

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
        lblDetails = new javax.swing.JLabel();
        statsPanel = new javax.swing.JPanel();
        cardTotal = new View.TranslucentPanel();
        lblTotalTitle = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();
        cardPending = new View.TranslucentPanel();
        lblPendingTitle = new javax.swing.JLabel();
        lblPendingVal = new javax.swing.JLabel();
        cardPaid = new View.TranslucentPanel();
        lblPaidTitle = new javax.swing.JLabel();
        lblPaidVal = new javax.swing.JLabel();
        tablePanel = new View.TranslucentPanel();
        scrollPayments = new javax.swing.JScrollPane();
        tblPayments = new javax.swing.JTable();
        btnPay = new javax.swing.JButton();
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
        lblTitle.setText("Payments");
        mainPanel.add(lblTitle);
        lblTitle.setBounds(30, 20, 400, 50);

        lblDetails.setFont(new java.awt.Font("Cambria Math", 2, 20)); // NOI18N
        lblDetails.setForeground(new java.awt.Color(255, 255, 255));
        lblDetails.setText("Details:");
        mainPanel.add(lblDetails);
        lblDetails.setBounds(30, 70, 100, 30);

        statsPanel.setOpaque(false);
        statsPanel.setLayout(null);

        cardTotal.setBackground(new java.awt.Color(255, 255, 255));
        cardTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardTotal.setLayout(null);

        lblTotalTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblTotalTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblTotalTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalTitle.setText("Total Fines");
        cardTotal.add(lblTotalTitle);
        lblTotalTitle.setBounds(10, 15, 200, 20);

        lblTotalVal.setFont(new java.awt.Font("Cambria Math", 1, 26)); // NOI18N
        lblTotalVal.setForeground(new java.awt.Color(51, 51, 51));
        lblTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalVal.setText("Rs. 0.00");
        cardTotal.add(lblTotalVal);
        lblTotalVal.setBounds(10, 45, 200, 50);

        statsPanel.add(cardTotal);
        cardTotal.setBounds(10, 10, 220, 120);

        cardPending.setBackground(new java.awt.Color(255, 255, 255));
        cardPending.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardPending.setLayout(null);

        lblPendingTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblPendingTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblPendingTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPendingTitle.setText("Pending Fines");
        cardPending.add(lblPendingTitle);
        lblPendingTitle.setBounds(10, 15, 200, 20);

        lblPendingVal.setFont(new java.awt.Font("Cambria Math", 1, 26)); // NOI18N
        lblPendingVal.setForeground(new java.awt.Color(204, 0, 0));
        lblPendingVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPendingVal.setText("Rs. 0.00");
        cardPending.add(lblPendingVal);
        lblPendingVal.setBounds(10, 45, 200, 50);

        statsPanel.add(cardPending);
        cardPending.setBounds(250, 10, 220, 120);

        cardPaid.setBackground(new java.awt.Color(255, 255, 255));
        cardPaid.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardPaid.setLayout(null);

        lblPaidTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblPaidTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblPaidTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPaidTitle.setText("Paid Fines");
        cardPaid.add(lblPaidTitle);
        lblPaidTitle.setBounds(10, 15, 200, 20);

        lblPaidVal.setFont(new java.awt.Font("Cambria Math", 1, 26)); // NOI18N
        lblPaidVal.setForeground(new java.awt.Color(0, 204, 0));
        lblPaidVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPaidVal.setText("Rs. 0.00");
        cardPaid.add(lblPaidVal);
        lblPaidVal.setBounds(10, 45, 200, 50);

        statsPanel.add(cardPaid);
        cardPaid.setBounds(490, 10, 220, 120);

        mainPanel.add(statsPanel);
        statsPanel.setBounds(30, 110, 920, 150);

        tablePanel.setBackground(new java.awt.Color(102, 51, 0));
        tablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 5));
        tablePanel.setLayout(null);

        tblPayments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fine ID", "Borrow ID", "Book Title", "Fine Amount", "Status", "Payment/Due Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPayments.setViewportView(tblPayments);

        tablePanel.add(scrollPayments);
        scrollPayments.setBounds(15, 15, 890, 255);

        btnPay.setBackground(new java.awt.Color(237, 226, 66));
        btnPay.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        btnPay.setText("Pay Selected Fine");
        tablePanel.add(btnPay);
        btnPay.setBounds(15, 285, 180, 35);

        mainPanel.add(tablePanel);
        tablePanel.setBounds(30, 270, 920, 340);

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
    private javax.swing.JButton btnPay;
    private javax.swing.JButton btnPayments;
    private View.TranslucentPanel cardPaid;
    private View.TranslucentPanel cardPending;
    private View.TranslucentPanel cardTotal;
    private javax.swing.JLabel lblDetails;
    private javax.swing.JLabel lblPaidTitle;
    private javax.swing.JLabel lblPaidVal;
    private javax.swing.JLabel lblPendingTitle;
    private javax.swing.JLabel lblPendingVal;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblTotalTitle;
    private javax.swing.JLabel lblTotalVal;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane scrollPayments;
    private javax.swing.JPanel statsPanel;
    private View.TranslucentPanel tablePanel;
    private View.TranslucentPanel sidebarPanel;
    private javax.swing.JTable tblPayments;
    // End of variables declaration//GEN-END:variables
}
