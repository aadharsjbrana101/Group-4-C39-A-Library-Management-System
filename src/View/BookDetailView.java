package View;

import java.awt.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class BookDetailView extends javax.swing.JFrame {
    private final Model.UserData currentUser;

    public BookDetailView() {
        this.currentUser = null;
        initComponents();
        setupDesign();
    }

    public BookDetailView(Model.UserData user) {
        this.currentUser = user;
        initComponents();
        setupDesign();
    }

    private void setupDesign() {
        ViewUtils.applySharedDesign(this, sidebarPanel, mainPanel, bgLabel, btnLogout,
            new javax.swing.JButton[]{btnDashboard, btnCatalog, btnMyBooks, btnPayments}, btnCatalog);

        bookPanel.setBackground(new Color(40, 20, 10));
        bookPanel.setAlpha(185);

        descriptionScrollPane.getViewport().setOpaque(false);
        txtDescription.setBackground(new Color(0, 0, 0, 0));
        txtDescription.setBorder(null);

        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent evt) {
                ViewUtils.handleResize(BookDetailView.this, sidebarPanel, mainPanel, bgLabel, btnLogout);

                int w = getContentPane().getWidth();
                int h = getContentPane().getHeight();
                if (w <= 0 || h <= 0) return;

                int mainW = w - 220;
                int mainH = h;
                
                lblTitle.setBounds(30, 20, 400, 50);
                
                int panelW = mainW - 60;
                int panelH = mainH - 150;
                bookPanel.setBounds(30, 90, panelW, panelH);
                
                // Position internal components responsively
                lblCover.setBounds(30, 30, 240, 320);
                int textX = 300;
                int textW = panelW - 330;
                lblBookTitle.setBounds(textX, 30, textW, 40);
                lblAuthor.setBounds(textX, 80, textW, 25);
                lblGenre.setBounds(textX, 115, textW, 25);
                lblYear.setBounds(textX, 150, textW, 25);
                lblIsbn.setBounds(textX, 185, textW, 25);
                lblQuantity.setBounds(textX, 220, textW, 25);
                
                lblDescriptionTitle.setBounds(textX, 260, textW, 25);
                descriptionScrollPane.setBounds(textX, 290, textW, panelH - 370);
                
                btnBorrow.setBounds(textX, panelH - 70, 220, 40);
                btnBack.setBounds(30, panelH - 70, 180, 40);
                
                revalidate();
                repaint();
            }
        });
    }

    // Populate book data
    public void setBookDetails(Model.Book book) {
        if (book == null) return;
        lblBookTitle.setText(book.getTitle());
        lblAuthor.setText("Author: " + book.getAuthor());
        lblGenre.setText("Genre: " + book.getGenre());
        lblYear.setText("Published Year: " + book.getYear());
        lblIsbn.setText("ISBN: " + book.getIsbn());
        lblQuantity.setText("Available: " + book.getAvailableQuantity() + " / " + book.getQuantity() + " copies");
        txtDescription.setText(book.getDescription());

        // Load image
        int coverW = 240;
        int coverH = 320;
        try {
            String path = "/images/" + book.getImagePath();
            URL imgUrl = getClass().getResource(path);
            if (imgUrl != null) {
                ImageIcon icon = new ImageIcon(imgUrl);
                Image img = icon.getImage().getScaledInstance(coverW, coverH, Image.SCALE_SMOOTH);
                lblCover.setIcon(new ImageIcon(img));
            } else {
                lblCover.setIcon(new ImageIcon(createPlaceholderCover(book.getTitle(), coverW, coverH)));
            }
        } catch (Exception e) {
            lblCover.setIcon(new ImageIcon(createPlaceholderCover(book.getTitle(), coverW, coverH)));
        }

        // Disable borrow if no copies or future release
        if (book.isFutureRelease()) {
            btnBorrow.setEnabled(false);
            btnBorrow.setText("Upcoming Release");
            lblQuantity.setText("Available: Upcoming Release");
        } else if (book.getAvailableQuantity() <= 0) {
            btnBorrow.setEnabled(false);
            btnBorrow.setText("Out of Stock");
        } else {
            btnBorrow.setEnabled(true);
            btnBorrow.setText("Borrow Book");
        }
    }

    private Image createPlaceholderCover(String title, int width, int height) {
        java.awt.image.BufferedImage img = new java.awt.image.BufferedImage(width, height, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gp = new GradientPaint(0, 0, new Color(48, 43, 99), 0, height, new Color(15, 12, 75));
        g2.setPaint(gp);
        g2.fillRoundRect(0, 0, width, height, 15, 15);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Cambria Math", Font.BOLD, 16));
        FontMetrics fm = g2.getFontMetrics();
        String[] words = title.split(" ");
        int y = 80;
        for (String word : words) {
            int strW = fm.stringWidth(word);
            g2.drawString(word, (width - strW) / 2, y);
            y += fm.getHeight();
            if (y > height - 30) break;
        }
        g2.dispose();
        return img;
    }

    // View Accessors for Clean Architecture
    public JButton getBtnDashboard() { return btnDashboard; }
    public JButton getBtnCatalog() { return btnCatalog; }
    public JButton getBtnMyBooks() { return btnMyBooks; }
    public JButton getBtnPayments() { return btnPayments; }
    public JButton getBtnLogout() { return btnLogout; }
    
    public JButton getBtnBorrow() { return btnBorrow; }
    public JButton getBtnBack() { return btnBack; }

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
        bookPanel = new View.TranslucentPanel();
        lblCover = new javax.swing.JLabel();
        lblBookTitle = new javax.swing.JLabel();
        lblAuthor = new javax.swing.JLabel();
        lblGenre = new javax.swing.JLabel();
        lblYear = new javax.swing.JLabel();
        lblIsbn = new javax.swing.JLabel();
        lblQuantity = new javax.swing.JLabel();
        lblDescriptionTitle = new javax.swing.JLabel();
        descriptionScrollPane = new javax.swing.JScrollPane();
        txtDescription = new javax.swing.JTextArea();
        btnBorrow = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
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
        lblTitle.setText("Book Details");
        mainPanel.add(lblTitle);
        lblTitle.setBounds(30, 20, 400, 50);

        bookPanel.setBackground(new java.awt.Color(102, 51, 0));
        bookPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 234, 0), 5));
        bookPanel.setLayout(null);

        lblCover.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 255, 255), 2));
        bookPanel.add(lblCover);
        lblCover.setBounds(30, 30, 300, 400);

        lblBookTitle.setFont(new java.awt.Font("Cambria Math", 1, 28)); // NOI18N
        lblBookTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblBookTitle.setText("Book Title");
        bookPanel.add(lblBookTitle);
        lblBookTitle.setBounds(360, 30, 510, 40);

        lblAuthor.setFont(new java.awt.Font("Cambria Math", 2, 18)); // NOI18N
        lblAuthor.setForeground(new java.awt.Color(221, 221, 221));
        lblAuthor.setText("Author: Author Name");
        bookPanel.add(lblAuthor);
        lblAuthor.setBounds(360, 80, 510, 25);

        lblGenre.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N
        lblGenre.setForeground(new java.awt.Color(221, 221, 221));
        lblGenre.setText("Genre: Genre Type");
        bookPanel.add(lblGenre);
        lblGenre.setBounds(360, 115, 510, 25);

        lblYear.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N
        lblYear.setForeground(new java.awt.Color(221, 221, 221));
        lblYear.setText("Published Year: 0000");
        bookPanel.add(lblYear);
        lblYear.setBounds(360, 150, 510, 25);

        lblIsbn.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N
        lblIsbn.setForeground(new java.awt.Color(221, 221, 221));
        lblIsbn.setText("ISBN: 000-0000000000");
        bookPanel.add(lblIsbn);
        lblIsbn.setBounds(360, 185, 510, 25);

        lblQuantity.setFont(new java.awt.Font("Cambria Math", 0, 18)); // NOI18N
        lblQuantity.setForeground(new java.awt.Color(221, 221, 221));
        lblQuantity.setText("Available: 0 / 0 copies");
        bookPanel.add(lblQuantity);
        lblQuantity.setBounds(360, 220, 510, 25);

        lblDescriptionTitle.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        lblDescriptionTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblDescriptionTitle.setText("Description:");
        bookPanel.add(lblDescriptionTitle);
        lblDescriptionTitle.setBounds(360, 260, 150, 25);

        descriptionScrollPane.setBorder(null);
        descriptionScrollPane.setOpaque(false);

        txtDescription.setEditable(false);
        txtDescription.setColumns(20);
        txtDescription.setFont(new java.awt.Font("Cambria Math", 2, 16)); // NOI18N
        txtDescription.setForeground(new java.awt.Color(238, 238, 238));
        txtDescription.setLineWrap(true);
        txtDescription.setRows(5);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setOpaque(false);
        descriptionScrollPane.setViewportView(txtDescription);

        bookPanel.add(descriptionScrollPane);
        descriptionScrollPane.setBounds(360, 290, 510, 140);

        btnBorrow.setBackground(new java.awt.Color(237, 226, 66));
        btnBorrow.setFont(new java.awt.Font("Cambria Math", 1, 18)); // NOI18N
        btnBorrow.setText("Borrow Book");
        bookPanel.add(btnBorrow);
        btnBorrow.setBounds(360, 450, 220, 40);

        btnBack.setFont(new java.awt.Font("Cambria Math", 0, 16)); // NOI18N
        btnBack.setForeground(new java.awt.Color(255, 255, 255));
        btnBack.setText("< Back to Catalog");
        btnBack.setContentAreaFilled(false);
        bookPanel.add(btnBack);
        btnBack.setBounds(30, 450, 160, 40);

        mainPanel.add(bookPanel);
        bookPanel.setBounds(30, 90, 900, 520);

        getContentPane().add(mainPanel);
        mainPanel.setBounds(220, 30, 980, 670);

        bgLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Untitled design (5).png"))); // NOI18N
        getContentPane().add(bgLabel);
        bgLabel.setBounds(0, 0, 1200, 700);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel bgLabel;
    private View.TranslucentPanel bookPanel;
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnBorrow;
    private javax.swing.JButton btnCatalog;
    private javax.swing.JButton btnDashboard;
    private javax.swing.JButton btnLogout;
    private javax.swing.JButton btnMyBooks;
    private javax.swing.JButton btnPayments;
    private javax.swing.JScrollPane descriptionScrollPane;
    private javax.swing.JLabel lblAuthor;
    private javax.swing.JLabel lblBookTitle;
    private javax.swing.JLabel lblCover;
    private javax.swing.JLabel lblDescriptionTitle;
    private javax.swing.JLabel lblGenre;
    private javax.swing.JLabel lblQuantity;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblYear;
    private javax.swing.JLabel lblIsbn;
    private javax.swing.JPanel mainPanel;
    private View.TranslucentPanel sidebarPanel;
    private javax.swing.JTextArea txtDescription;
    // End of variables declaration//GEN-END:variables
}
