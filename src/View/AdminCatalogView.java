package View;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class AdminCatalogView extends javax.swing.JFrame {
    private final Model.userdata currentAdmin;

    public AdminCatalogView() {
        this.currentAdmin = null;
        initComponents();
        setupDesign();
    }

    public AdminCatalogView(Model.userdata admin) {
        this.currentAdmin = admin;
        initComponents();
        setupDesign();
    }

    private void setupDesign() {
        ViewUtils.applySharedDesign(this, sidebarPanel, mainPanel, bgLabel, btnLogout,
            new javax.swing.JButton[]{btnDashboard, btnCatalog, btnUsers, btnPayments}, btnCatalog);
        
        formPanel.setBackground(new Color(40, 20, 10));
        formPanel.setAlpha(185);
        tablePanel.setBackground(new Color(40, 20, 10));
        tablePanel.setAlpha(185);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ViewUtils.handleResize(AdminCatalogView.this, sidebarPanel, mainPanel, bgLabel, btnLogout);

                int w = getContentPane().getWidth();
                int h = getContentPane().getHeight();
                if (w <= 0 || h <= 0) return;

                int mainW = w - 220;
                int mainH = h;
                
                lblTitle.setBounds(30, 15, 600, 45);
                
                // Form panel is fixed width, table takes remaining space
                int formW = 380;
                int formH = mainH - 120;
                formPanel.setBounds(30, 80, formW, formH);
                
                // Form internals
                int textW = formW - 110;
                txtBookTitle.setBounds(90, 55, textW, 25);
                txtAuthor.setBounds(90, 90, textW, 25);
                cbGenre.setBounds(90, 125, textW, 25);
                txtYear.setBounds(90, 160, textW, 25);
                txtQuantity.setBounds(90, 195, textW, 25);
                txtIsbn.setBounds(90, 230, textW, 25);
                txtImagePath.setBounds(90, 265, textW, 25);
                scrollDesc.setBounds(90, 300, textW, formH - 450);
                chkFuture.setBounds(90, formH - 140, textW, 25);
                
                btnSave.setBounds(90, formH - 100, 130, 35);
                btnReset.setBounds(230, formH - 100, 130, 35);
                btnDelete.setBounds(90, formH - 50, 270, 35);
                
                int tableW = mainW - formW - 90;
                int tableH = mainH - 120;
                tablePanel.setBounds(formW + 60, 80, tableW, tableH);
                scrollBooks.setBounds(15, 15, tableW - 30, tableH - 30);
                
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
    
    public JTextField getTxtBookTitle() { return txtBookTitle; }
    public JTextField getTxtAuthor() { return txtAuthor; }
    public JComboBox<String> getCbGenre() { return cbGenre; }
    public JTextField getTxtYear() { return txtYear; }
    public JTextField getTxtQuantity() { return txtQuantity; }
    public JTextField getTxtIsbn() { return txtIsbn; }
    public JTextField getTxtImagePath() { return txtImagePath; }
    public JTextArea getTxtDescription() { return txtDescription; }
    public JCheckBox getChkFuture() { return chkFuture; }
    
    public JButton getBtnSave() { return btnSave; }
    public JButton getBtnReset() { return btnReset; }
    public JButton getBtnDelete() { return btnDelete; }
    
    public JTable getTblBooks() { return tblBooks; }

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
        formPanel = new View.TranslucentPanel();
        lblFormHeader = new javax.swing.JLabel();
        lblBookTitle = new javax.swing.JLabel();
        txtBookTitle = new javax.swing.JTextField();
        lblAuthor = new javax.swing.JLabel();
        txtAuthor = new javax.swing.JTextField();
        lblGenre = new javax.swing.JLabel();
        cbGenre = new javax.swing.JComboBox<>();
        lblYear = new javax.swing.JLabel();
        txtYear = new javax.swing.JTextField();
        lblQuantity = new javax.swing.JLabel();
        txtQuantity = new javax.swing.JTextField();
        lblIsbn = new javax.swing.JLabel();
        txtIsbn = new javax.swing.JTextField();
        lblImagePath = new javax.swing.JLabel();
        txtImagePath = new javax.swing.JTextField();
        lblDescription = new javax.swing.JLabel();
        scrollDesc = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        chkFuture = new javax.swing.JCheckBox();
        btnSave = new javax.swing.JButton();
        btnReset = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        tablePanel = new View.TranslucentPanel();
        scrollBooks = new javax.swing.JScrollPane();
        tblBooks = new javax.swing.JTable();
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
        lblTitle.setText("Book Inventory Management");
        mainPanel.add(lblTitle);
        lblTitle.setBounds(30, 15, 600, 45);

        formPanel.setBackground(new java.awt.Color(102, 51, 0));
        formPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 5));
        formPanel.setLayout(null);

        lblFormHeader.setFont(new java.awt.Font("Cambria Math", 1, 20)); // NOI18N
        lblFormHeader.setForeground(new java.awt.Color(255, 255, 255));
        lblFormHeader.setText("Add / Edit Book");
        formPanel.add(lblFormHeader);
        lblFormHeader.setBounds(15, 15, 200, 25);

        lblBookTitle.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        lblBookTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblBookTitle.setText("Title");
        formPanel.add(lblBookTitle);
        lblBookTitle.setBounds(15, 55, 70, 25);
        formPanel.add(txtBookTitle);
        txtBookTitle.setBounds(90, 55, 270, 25);

        lblAuthor.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        lblAuthor.setForeground(new java.awt.Color(255, 255, 255));
        lblAuthor.setText("Author");
        formPanel.add(lblAuthor);
        lblAuthor.setBounds(15, 90, 70, 25);
        formPanel.add(txtAuthor);
        txtAuthor.setBounds(90, 90, 270, 25);

        lblGenre.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        lblGenre.setForeground(new java.awt.Color(255, 255, 255));
        lblGenre.setText("Genre");
        formPanel.add(lblGenre);
        lblGenre.setBounds(15, 125, 70, 25);

        cbGenre.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Fantasy", "Sci-Fi", "Classic", "Detective", "Fiction" }));
        formPanel.add(cbGenre);
        cbGenre.setBounds(90, 125, 270, 25);

        lblYear.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        lblYear.setForeground(new java.awt.Color(255, 255, 255));
        lblYear.setText("Year");
        formPanel.add(lblYear);
        lblYear.setBounds(15, 160, 70, 25);
        formPanel.add(txtYear);
        txtYear.setBounds(90, 160, 270, 25);

        lblQuantity.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        lblQuantity.setForeground(new java.awt.Color(255, 255, 255));
        lblQuantity.setText("Copies");
        formPanel.add(lblQuantity);
        lblQuantity.setBounds(15, 195, 70, 25);
        formPanel.add(txtQuantity);
        txtQuantity.setBounds(90, 195, 270, 25);

        lblIsbn.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        lblIsbn.setForeground(new java.awt.Color(255, 255, 255));
        lblIsbn.setText("ISBN");
        formPanel.add(lblIsbn);
        lblIsbn.setBounds(15, 230, 70, 25);
        formPanel.add(txtIsbn);
        txtIsbn.setBounds(90, 230, 270, 25);

        lblImagePath.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        lblImagePath.setForeground(new java.awt.Color(255, 255, 255));
        lblImagePath.setText("Image");
        formPanel.add(lblImagePath);
        lblImagePath.setBounds(15, 265, 70, 25);
        formPanel.add(txtImagePath);
        txtImagePath.setBounds(90, 265, 270, 25);

        lblDescription.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        lblDescription.setForeground(new java.awt.Color(255, 255, 255));
        lblDescription.setText("Desc");
        formPanel.add(lblDescription);
        lblDescription.setBounds(15, 300, 70, 25);

        txtDescription.setColumns(20);
        txtDescription.setLineWrap(true);
        txtDescription.setRows(4);
        txtDescription.setWrapStyleWord(true);
        scrollDesc.setViewportView(txtDescription);

        formPanel.add(scrollDesc);
        scrollDesc.setBounds(90, 300, 270, 90);

        chkFuture.setFont(new java.awt.Font("Cambria Math", 0, 14)); // NOI18N
        chkFuture.setForeground(new java.awt.Color(255, 255, 255));
        chkFuture.setText("Future Release / Upcoming");
        chkFuture.setOpaque(false);
        formPanel.add(chkFuture);
        chkFuture.setBounds(90, 400, 270, 25);

        btnSave.setBackground(new java.awt.Color(237, 226, 66));
        btnSave.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        btnSave.setText("Save Book");
        formPanel.add(btnSave);
        btnSave.setBounds(90, 440, 130, 35);

        btnReset.setText("Reset");
        formPanel.add(btnReset);
        btnReset.setBounds(230, 440, 130, 35);

        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setFont(new java.awt.Font("Cambria Math", 1, 14)); // NOI18N
        btnDelete.setForeground(new java.awt.Color(255, 255, 255));
        btnDelete.setText("Delete Book");
        formPanel.add(btnDelete);
        btnDelete.setBounds(90, 490, 270, 35);

        mainPanel.add(formPanel);
        formPanel.setBounds(30, 80, 380, 550);

        tablePanel.setBackground(new java.awt.Color(102, 51, 0));
        tablePanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 5));
        tablePanel.setLayout(null);

        tblBooks.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Title", "Author", "Genre", "Year", "Qty", "Avail", "ISBN"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollBooks.setViewportView(tblBooks);

        tablePanel.add(scrollBooks);
        scrollBooks.setBounds(15, 15, 490, 520);

        mainPanel.add(tablePanel);
        tablePanel.setBounds(430, 80, 520, 550);

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
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnPayments;
    private javax.swing.JButton btnReset;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnUsers;
    private javax.swing.JComboBox<String> cbGenre;
    private View.TranslucentPanel formPanel;
    private javax.swing.JLabel lblAuthor;
    private javax.swing.JLabel lblBookTitle;
    private javax.swing.JLabel lblDescription;
    private javax.swing.JLabel lblFormHeader;
    private javax.swing.JLabel lblGenre;
    private javax.swing.JLabel lblImagePath;
    private javax.swing.JLabel lblIsbn;
    private javax.swing.JLabel lblQuantity;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblYear;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JScrollPane scrollBooks;
    private javax.swing.JScrollPane scrollDesc;
    private View.TranslucentPanel tablePanel;
    private javax.swing.JTable tblBooks;
    private View.TranslucentPanel sidebarPanel;
    private javax.swing.JTextField txtAuthor;
    private javax.swing.JTextField txtBookTitle;
    private javax.swing.JTextArea txtDescription;
    private javax.swing.JTextField txtImagePath;
    private javax.swing.JTextField txtIsbn;
    private javax.swing.JTextField txtQuantity;
    private javax.swing.JTextField txtYear;
    private javax.swing.JCheckBox chkFuture;
    // End of variables declaration//GEN-END:variables
}
