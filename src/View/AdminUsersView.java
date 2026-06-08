package View;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminUsersView extends javax.swing.JFrame {
    private final Model.userdata currentAdmin;

    public AdminUsersView() {
        this.currentAdmin = null;
        initComponents();
        setupDesign();
    }

    public AdminUsersView(Model.userdata admin) {
        this.currentAdmin = admin;
        initComponents();
        setupDesign();
    }

    private void setupDesign() {
        ViewUtils.applySharedDesign(this, sidebarPanel, mainPanel, bgLabel, btnLogout,
            new javax.swing.JButton[]{btnDashboard, btnCatalog, btnUsers, btnPayments}, btnUsers);
        
        tablePanel.setBackground(new Color(40, 20, 10));
        tablePanel.setAlpha(185);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ViewUtils.handleResize(AdminUsersView.this, sidebarPanel, mainPanel, bgLabel, btnLogout);

                int w = getContentPane().getWidth();
                int h = getContentPane().getHeight();
                if (w <= 0 || h <= 0) return;

                int mainW = w - 220;
                int mainH = h;
                
                lblTitle.setBounds(30, 20, 400, 50);
                
                int panelW = mainW - 60;
                int panelH = mainH - 150;
                tablePanel.setBounds(30, 90, panelW, panelH);
                
                scrollUsers.setBounds(15, 15, panelW - 30, panelH - 80);
                btnBlock.setBounds(15, panelH - 50, 160, 35);
                btnActivate.setBounds(185, panelH - 50, 160, 35);
                
                revalidate();
                repaint();
            }
        });
    }

    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnCatalog() { return btnCatalog; }
    public JButton getBtnUsers() { return btnUsers; }
    public JButton getBtnPayments() { return btnPayments; }
    public JButton getBtnLogout() { return btnLogout; }
    
    public JTable getTblUsers() { return tblUsers; }
    public JButton getBtnBlock() { return btnBlock; }
    public JButton getBtnActivate() { return btnActivate; }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        sidebarPanel = new View.TranslucentPanel();
        btnDashboard = new javax.swing.JButton();
        btnCatalog = new javax.swing.JButton();
        btnUsers = new javax.swing.JButton();
        btnPayments = new javax.swing.JButton();
        btnLogout = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        tablePanel = new View.TranslucentPanel();
        scrollUsers = new javax.swing.JScrollPane();
        tblUsers = new javax.swing.JTable();
        btnBlock = new javax.swing.JButton();
        btnActivate = new javax.swing.JButton();
        bgLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 700));
        getContentPane().setLayout(null);

        sidebarPanel.setOpaque(false);
        sidebarPanel.setLayout(null);

        btnDashboard.setFont(new java.awt.Font("Cambria Math", 3, 18));
        btnDashboard.setForeground(new java.awt.Color(255, 255, 255));
        btnDashboard.setText("🏠 Dashboard");
        btnDashboard.setContentAreaFilled(false);
        btnDashboard.setFocusPainted(false);
        btnDashboard.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sidebarPanel.add(btnDashboard);
        btnDashboard.setBounds(10, 50, 200, 45);

        btnCatalog.setFont(new java.awt.Font("Cambria Math", 3, 18));
        btnCatalog.setForeground(new java.awt.Color(255, 255, 255));
        btnCatalog.setText("📚 Catalog");
        btnCatalog.setContentAreaFilled(false);
        btnCatalog.setFocusPainted(false);
        btnCatalog.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sidebarPanel.add(btnCatalog);
        btnCatalog.setBounds(10, 110, 200, 45);

        btnUsers.setFont(new java.awt.Font("Cambria Math", 3, 18));
        btnUsers.setForeground(new java.awt.Color(255, 255, 255));
        btnUsers.setText("👥 Manage Users");
        btnUsers.setContentAreaFilled(false);
        btnUsers.setFocusPainted(false);
        btnUsers.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sidebarPanel.add(btnUsers);
        btnUsers.setBounds(10, 170, 200, 45);

        btnPayments.setFont(new java.awt.Font("Cambria Math", 3, 18));
        btnPayments.setForeground(new java.awt.Color(255, 255, 255));
        btnPayments.setText("💳 Payments");
        btnPayments.setContentAreaFilled(false);
        btnPayments.setFocusPainted(false);
        btnPayments.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        sidebarPanel.add(btnPayments);
        btnPayments.setBounds(10, 230, 200, 45);

        btnLogout.setFont(new java.awt.Font("Cambria Math", 3, 18));
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

        lblTitle.setFont(new java.awt.Font("Cambria Math", 1, 42));
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setText("User Management");
        mainPanel.add(lblTitle);
        lblTitle.setBounds(30, 20, 400, 50);

        tablePanel.setBackground(new java.awt.Color(102, 51, 0));
        tablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 5));
        tablePanel.setLayout(null);

        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] {
                "User ID", "Username", "Email", "Role", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollUsers.setViewportView(tblUsers);

        tablePanel.add(scrollUsers);
        scrollUsers.setBounds(15, 15, 890, 425);

        btnBlock.setBackground(new java.awt.Color(255, 51, 51));
        btnBlock.setFont(new java.awt.Font("Cambria Math", 1, 14));
        btnBlock.setForeground(new java.awt.Color(255, 255, 255));
        btnBlock.setText("Block User");
        tablePanel.add(btnBlock);
        btnBlock.setBounds(15, 460, 160, 35);

        btnActivate.setBackground(new java.awt.Color(237, 226, 66));
        btnActivate.setFont(new java.awt.Font("Cambria Math", 1, 14));
        btnActivate.setText("Activate User");
        tablePanel.add(btnActivate);
        btnActivate.setBounds(185, 460, 160, 35);

        mainPanel.add(tablePanel);
        tablePanel.setBounds(30, 90, 920, 520);

        getContentPane().add(mainPanel);
        mainPanel.setBounds(220, 30, 980, 670);

        bgLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Untitled design (5).png")));
        getContentPane().add(bgLabel);
        bgLabel.setBounds(0, 0, 1200, 700);

        pack();
    }

    private javax.swing.JLabel bgLabel;
    private javax.swing.JButton btnActivate;
    private javax.swing.JButton btnBlock;
    private javax.swing.JButton btnCatalog;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPayments;
    private javax.swing.JButton btnUsers;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane scrollUsers;
    private View.TranslucentPanel tablePanel;
    private View.TranslucentPanel sidebarPanel;
    private javax.swing.JTable tblUsers;
}
