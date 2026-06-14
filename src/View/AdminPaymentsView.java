package View;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminPaymentsView extends javax.swing.JFrame {
    private final Model.UserData currentAdmin;

    public AdminPaymentsView() {
        this.currentAdmin = null;
        initComponents();
        setupDesign();
    }

    public AdminPaymentsView(Model.UserData admin) {
        this.currentAdmin = admin;
        initComponents();
        setupDesign();
    }

    private void setupDesign() {
        ViewUtils.applySharedDesign(this, sidebarPanel, mainPanel, bgLabel, btnLogout,
            new javax.swing.JButton[]{btnDashboard, btnCatalog, btnUsers, btnPayments}, btnPayments);
        
        cardTotal.setBackground(Color.WHITE);
        cardTotal.setAlpha(220);
        cardOutstanding.setBackground(Color.WHITE);
        cardOutstanding.setAlpha(220);
        cardOverdue.setBackground(Color.WHITE);
        cardOverdue.setAlpha(220);
        
        tablePanel.setBackground(new Color(40, 20, 10));
        tablePanel.setAlpha(185);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ViewUtils.handleResize(AdminPaymentsView.this, sidebarPanel, mainPanel, bgLabel, btnLogout);

                int w = getContentPane().getWidth();
                int h = getContentPane().getHeight();
                if (w <= 0 || h <= 0) return;

                int mainW = w - 220;
                int mainH = h;
                
                lblTitle.setBounds(30, 15, 600, 45);
                
                int panelW = mainW - 60;
                statsPanel.setBounds(30, 90, panelW, 150);
                int cardW = Math.max(120, (panelW - 40) / 3);
                cardTotal.setBounds(0, 10, cardW, 120);
                cardOutstanding.setBounds(cardW + 15, 10, cardW, 120);
                cardOverdue.setBounds((cardW * 2) + 30, 10, cardW, 120);
                
                int tblH = mainH - 280;
                tablePanel.setBounds(30, 260, panelW, tblH);
                scrollPayments.setBounds(15, 15, panelW - 30, tblH - 70);
                btnExport.setBounds(15, tblH - 45, 220, 35);
                
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
    
    public JLabel getLblTotalVal() { return lblTotalVal; }
    public JLabel getLblOutstandingVal() { return lblOutstandingVal; }
    public JLabel getLblOverdueVal() { return lblOverdueVal; }
    
    public JTable getTblPayments() { return tblPayments; }
    public JButton getBtnExport() { return btnExport; }

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
        cardTotal = new View.TranslucentPanel();
        lblTotalTitle = new javax.swing.JLabel();
        lblTotalVal = new javax.swing.JLabel();
        cardOutstanding = new View.TranslucentPanel();
        lblOutstandingTitle = new javax.swing.JLabel();
        lblOutstandingVal = new javax.swing.JLabel();
        cardOverdue = new View.TranslucentPanel();
        lblOverdueTitle = new javax.swing.JLabel();
        lblOverdueVal = new javax.swing.JLabel();
        tablePanel = new View.TranslucentPanel();
        scrollPayments = new javax.swing.JScrollPane();
        tblPayments = new javax.swing.JTable();
        btnExport = new javax.swing.JButton();
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

        lblTitle.setFont(new java.awt.Font("Cambria Math", 1, 38)); // NOI18N
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("Transactions & Fine History");
        mainPanel.add(lblTitle);
        lblTitle.setBounds(30, 15, 600, 45);

        statsPanel.setOpaque(false);
        statsPanel.setLayout(null);

        cardTotal.setBackground(new java.awt.Color(255, 255, 255));
        cardTotal.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardTotal.setLayout(null);

        lblTotalTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblTotalTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblTotalTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalTitle.setText("Total Paid Received");
        cardTotal.add(lblTotalTitle);
        lblTotalTitle.setBounds(10, 15, 200, 20);

        lblTotalVal.setFont(new java.awt.Font("Cambria Math", 1, 26)); // NOI18N
        lblTotalVal.setForeground(new java.awt.Color(0, 204, 0));
        lblTotalVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTotalVal.setText("Rs. 0.00");
        cardTotal.add(lblTotalVal);
        lblTotalVal.setBounds(10, 45, 200, 50);

        statsPanel.add(cardTotal);
        cardTotal.setBounds(10, 10, 220, 120);

        cardOutstanding.setBackground(new java.awt.Color(255, 255, 255));
        cardOutstanding.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardOutstanding.setLayout(null);

        lblOutstandingTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblOutstandingTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblOutstandingTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOutstandingTitle.setText("Outstanding Fines");
        cardOutstanding.add(lblOutstandingTitle);
        lblOutstandingTitle.setBounds(10, 15, 200, 20);

        lblOutstandingVal.setFont(new java.awt.Font("Cambria Math", 1, 26)); // NOI18N
        lblOutstandingVal.setForeground(new java.awt.Color(204, 0, 0));
        lblOutstandingVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOutstandingVal.setText("Rs. 0.00");
        cardOutstanding.add(lblOutstandingVal);
        lblOutstandingVal.setBounds(10, 45, 200, 50);

        statsPanel.add(cardOutstanding);
        cardOutstanding.setBounds(250, 10, 220, 120);

        cardOverdue.setBackground(new java.awt.Color(255, 255, 255));
        cardOverdue.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 2));
        cardOverdue.setLayout(null);

        lblOverdueTitle.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        lblOverdueTitle.setForeground(new java.awt.Color(51, 51, 51));
        lblOverdueTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOverdueTitle.setText("Overdue Records");
        cardOverdue.add(lblOverdueTitle);
        lblOverdueTitle.setBounds(10, 15, 200, 20);

        lblOverdueVal.setFont(new java.awt.Font("Cambria Math", 1, 26)); // NOI18N
        lblOverdueVal.setForeground(new java.awt.Color(81, 81, 81));
        lblOverdueVal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblOverdueVal.setText("0");
        cardOverdue.add(lblOverdueVal);
        lblOverdueVal.setBounds(10, 45, 200, 50);

        statsPanel.add(cardOverdue);
        cardOverdue.setBounds(490, 10, 220, 120);

        mainPanel.add(statsPanel);
        statsPanel.setBounds(30, 90, 920, 150);

        tablePanel.setBackground(new java.awt.Color(102, 51, 0));
        tablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 5));
        tablePanel.setLayout(null);

        tblPayments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Fine ID", "User Name", "Book Title", "Fine Amount", "Status", "Payment Date", "Due Date"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPayments.setViewportView(tblPayments);

        tablePanel.add(scrollPayments);
        scrollPayments.setBounds(15, 15, 890, 280);

        btnExport.setBackground(new java.awt.Color(237, 226, 66));
        btnExport.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        btnExport.setText("Export Reports to CSV");
        tablePanel.add(btnExport);
        btnExport.setBounds(15, 310, 220, 35);

        mainPanel.add(tablePanel);
        tablePanel.setBounds(30, 260, 920, 360);

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
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPayments;
    private javax.swing.JButton btnUsers;
    private View.TranslucentPanel cardOutstanding;
    private View.TranslucentPanel cardOverdue;
    private View.TranslucentPanel cardTotal;
    private javax.swing.JLabel lblOutstandingTitle;
    private javax.swing.JLabel lblOutstandingVal;
    private javax.swing.JLabel lblOverdueTitle;
    private javax.swing.JLabel lblOverdueVal;
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
