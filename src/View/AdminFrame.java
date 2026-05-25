package View;

import Model.Book;
import dao.bookcatalogDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * Premium Administrator & Librarian Control Panel.
 * Designed with standard dark-gold visual excellence matching LMS catalog.
 * Supports complete Book CRUD operations and active borrow tracking.
 * @author Amanm
 */
public class AdminFrame extends JFrame {
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AdminFrame.class.getName());
    private final bookcatalogDAO catalogDAO = new bookcatalogDAO();

    // UI Structure
    private BackgroundPanel backgroundPanel;
    private JPanel activeContentPanel;

    // Sidebar buttons
    private SidebarButton sidebarBtnBooks;
    private SidebarButton sidebarBtnCirculation;
    private SidebarButton sidebarBtnLogout;

    // Sub-panels
    private JPanel bookMgmtPanel;
    private JPanel circulationPanel;

    // Table Models
    private DefaultTableModel bookTableModel;
    private DefaultTableModel circulationTableModel;
    private JTable bookTable;
    private JTable circulationTable;

    private List<Book> allBooks;

    public AdminFrame() {
        initComponentsCustom();
    }

    private void initComponentsCustom() {
        setTitle("LMS - Administrator Control Panel");
        setSize(1700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        // Core primary background
        backgroundPanel = new BackgroundPanel();
        setContentPane(backgroundPanel);

        // Initialize beautiful Sidebar Buttons
        sidebarBtnBooks = new SidebarButton("Book Catalog Mgmt");
        sidebarBtnCirculation = new SidebarButton("Borrowing Logs");
        sidebarBtnLogout = new SidebarButton("Librarian Logout");

        sidebarBtnBooks.setBounds(0, 0, 260, 120);
        sidebarBtnCirculation.setBounds(0, 120, 260, 120);
        sidebarBtnLogout.setBounds(0, 240, 260, 120);

        backgroundPanel.add(sidebarBtnBooks);
        backgroundPanel.add(sidebarBtnCirculation);
        backgroundPanel.add(sidebarBtnLogout);

        sidebarBtnBooks.addActionListener(e -> switchView("Books"));
        sidebarBtnCirculation.addActionListener(e -> switchView("Circulation"));
        sidebarBtnLogout.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to Logout from Admin panel?", "Logout confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                new AuthFrame().setVisible(true);
                this.dispose();
            }
        });

        // Initialize panels
        createBookMgmtPanel();
        createCirculationPanel();

        // Default active view
        switchView("Books");
    }

    private void switchView(String viewName) {
        if (activeContentPanel != null) {
            backgroundPanel.remove(activeContentPanel);
        }

        sidebarBtnBooks.setActive(false);
        sidebarBtnCirculation.setActive(false);

        if ("Books".equals(viewName)) {
            activeContentPanel = bookMgmtPanel;
            sidebarBtnBooks.setActive(true);
            refreshBookTable();
        } else {
            activeContentPanel = circulationPanel;
            sidebarBtnCirculation.setActive(true);
            refreshCirculationTable();
        }

        activeContentPanel.setBounds(260, 0, 1440, 700);
        backgroundPanel.add(activeContentPanel);

        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    private void createBookMgmtPanel() {
        bookMgmtPanel = new JPanel(null);
        bookMgmtPanel.setOpaque(false);

        JLabel mainTitle = new JLabel("Librarian Book Catalog Management");
        mainTitle.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 38));
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setBounds(50, 20, 700, 50);
        bookMgmtPanel.add(mainTitle);

        // Styling elements for Table Container
        JPanel tableContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 130));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(new Color(255, 255, 255, 25));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };
        tableContainer.setOpaque(false);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tableContainer.setBounds(50, 90, 1340, 440);
        bookMgmtPanel.add(tableContainer);

        // Build JTable
        String[] columns = {"ID", "Title", "Author", "Genre", "Published Year", "Image Path", "Release Type"};
        bookTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(bookTableModel);
        styleJTable(bookTable);

        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        // Control Buttons
        JButton btnAdd = createStyledButton("Add New Book", new Color(46, 204, 113));
        JButton btnEdit = createStyledButton("Edit Book Details", new Color(241, 196, 15));
        JButton btnDelete = createStyledButton("Delete Book", new Color(231, 76, 60));

        btnAdd.setBounds(50, 555, 220, 50);
        btnEdit.setBounds(300, 555, 220, 50);
        btnDelete.setBounds(550, 555, 220, 50);

        bookMgmtPanel.add(btnAdd);
        bookMgmtPanel.add(btnEdit);
        bookMgmtPanel.add(btnDelete);

        btnAdd.addActionListener(e -> showBookDialog(null));
        btnEdit.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book from the table to edit!", "Select Book", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Book selected = allBooks.get(selectedRow);
            showBookDialog(selected);
        });
        btnDelete.addActionListener(e -> {
            int selectedRow = bookTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Please select a book from the table to delete!", "Select Book", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Book selected = allBooks.get(selectedRow);
            int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete '" + selected.getTitle() + "'?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                boolean ok = catalogDAO.deleteBook(selected.getId());
                if (ok) {
                    JOptionPane.showMessageDialog(this, "Book deleted successfully!", "Deleted", JOptionPane.INFORMATION_MESSAGE);
                    refreshBookTable();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete book.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private void createCirculationPanel() {
        circulationPanel = new JPanel(null);
        circulationPanel.setOpaque(false);

        JLabel mainTitle = new JLabel("LMS Borrowed Books Monitoring Logs");
        mainTitle.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 38));
        mainTitle.setForeground(Color.WHITE);
        mainTitle.setBounds(50, 20, 800, 50);
        circulationPanel.add(mainTitle);

        JPanel tableContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 130));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(new Color(255, 255, 255, 25));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };
        tableContainer.setOpaque(false);
        tableContainer.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        tableContainer.setBounds(50, 90, 1340, 515);
        circulationPanel.add(tableContainer);

        String[] columns = {"Borrower Username", "Borrower Email Address", "Borrowed Book Title", "Borrow Timestamp"};
        circulationTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        circulationTable = new JTable(circulationTableModel);
        styleJTable(circulationTable);

        JScrollPane scrollPane = new JScrollPane(circulationTable);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        tableContainer.add(scrollPane, BorderLayout.CENTER);
    }

    private void refreshBookTable() {
        bookTableModel.setRowCount(0);
        allBooks = catalogDAO.getAllBooks();
        for (Book b : allBooks) {
            bookTableModel.addRow(new Object[]{
                b.getId(),
                b.getTitle(),
                b.getAuthor(),
                b.getGenre(),
                b.getPublishedYear(),
                b.getImagePath(),
                b.isFuture() ? "Future Release" : "Catalog Active"
            });
        }
    }

    private void refreshCirculationTable() {
        circulationTableModel.setRowCount(0);
        List<String[]> borrowedLogs = catalogDAO.getAllBorrowedBooksAdmin();
        for (String[] log : borrowedLogs) {
            circulationTableModel.addRow(log);
        }
    }

    private void styleJTable(JTable table) {
        table.setOpaque(false);
        table.setBackground(new Color(30, 34, 42, 0));
        table.setForeground(Color.WHITE);
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(241, 196, 15, 80));
        table.setSelectionForeground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        table.setGridColor(new Color(255, 255, 255, 20));
        table.setShowGrid(true);

        JTableHeader header = table.getTableHeader();
        header.setOpaque(false);
        header.setBackground(new Color(30, 34, 42));
        header.setForeground(new Color(241, 196, 15));
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
    }

    private JButton createStyledButton(String text, Color baseColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(baseColor);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(null);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void showBookDialog(Book editingBook) {
        JDialog dialog = new JDialog(this, editingBook == null ? "Add New Book" : "Edit Book", true);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(null);
        dialog.getContentPane().setBackground(new Color(30, 34, 42));

        // Form Fields
        JLabel lblTitle = createDialogLabel("Book Title");
        lblTitle.setBounds(30, 20, 440, 20);
        dialog.add(lblTitle);

        JTextField tfTitle = createDialogField(editingBook == null ? "" : editingBook.getTitle());
        tfTitle.setBounds(30, 45, 440, 35);
        dialog.add(tfTitle);

        JLabel lblAuthor = createDialogLabel("Author Name");
        lblAuthor.setBounds(30, 90, 440, 20);
        dialog.add(lblAuthor);

        JTextField tfAuthor = createDialogField(editingBook == null ? "" : editingBook.getAuthor());
        tfAuthor.setBounds(30, 115, 440, 35);
        dialog.add(tfAuthor);

        JLabel lblGenre = createDialogLabel("Genre / Category");
        lblGenre.setBounds(30, 160, 210, 20);
        dialog.add(lblGenre);

        JComboBox<String> cbGenre = new JComboBox<>(new String[]{"Classic", "Fantasy", "Comedy", "Adventure", "Sci-Fi", "Mystery"});
        cbGenre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        cbGenre.setBackground(new Color(40, 45, 55));
        cbGenre.setForeground(Color.WHITE);
        cbGenre.setBounds(30, 185, 210, 35);
        if (editingBook != null) cbGenre.setSelectedItem(editingBook.getGenre());
        dialog.add(cbGenre);

        JLabel lblYear = createDialogLabel("Published Year");
        lblYear.setBounds(260, 160, 210, 20);
        dialog.add(lblYear);

        JTextField tfYear = createDialogField(editingBook == null ? "" : String.valueOf(editingBook.getPublishedYear()));
        tfYear.setBounds(260, 185, 210, 35);
        dialog.add(tfYear);

        JLabel lblDesc = createDialogLabel("Book Description");
        lblDesc.setBounds(30, 230, 440, 20);
        dialog.add(lblDesc);

        JTextArea taDesc = new JTextArea(editingBook == null ? "" : editingBook.getDescription());
        taDesc.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        taDesc.setBackground(new Color(20, 24, 30));
        taDesc.setForeground(Color.WHITE);
        taDesc.setCaretColor(Color.WHITE);
        taDesc.setLineWrap(true);
        taDesc.setWrapStyleWord(true);
        taDesc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        JScrollPane descScroll = new JScrollPane(taDesc);
        descScroll.setBounds(30, 255, 440, 100);
        dialog.add(descScroll);

        JLabel lblCover = createDialogLabel("Cover Image Filename");
        lblCover.setBounds(30, 365, 440, 20);
        dialog.add(lblCover);

        JTextField tfCover = createDialogField(editingBook == null ? "" : editingBook.getImagePath());
        tfCover.setBounds(30, 390, 310, 35);
        dialog.add(tfCover);

        JButton btnChooseFile = new JButton("Upload Image") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(241, 196, 15));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btnChooseFile.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnChooseFile.setForeground(new Color(30, 30, 30));
        btnChooseFile.setContentAreaFilled(false);
        btnChooseFile.setFocusPainted(false);
        btnChooseFile.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnChooseFile.setBounds(350, 390, 120, 35);
        dialog.add(btnChooseFile);

        btnChooseFile.addActionListener(e -> {
            String uploadedName = handleImageSelection();
            if (uploadedName != null) {
                tfCover.setText(uploadedName);
            }
        });

        JCheckBox chkFuture = new JCheckBox("Mark as Upcoming Future Release");
        chkFuture.setFont(new Font("Segoe UI", Font.BOLD, 13));
        chkFuture.setForeground(Color.WHITE);
        chkFuture.setOpaque(false);
        chkFuture.setBounds(30, 440, 440, 25);
        if (editingBook != null) chkFuture.setSelected(editingBook.isFuture());
        dialog.add(chkFuture);

        JButton btnSave = new JButton("Save Book Details") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(46, 204, 113));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnSave.setForeground(Color.WHITE);
        btnSave.setContentAreaFilled(false);
        btnSave.setFocusPainted(false);
        btnSave.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSave.setBounds(30, 490, 440, 45);
        dialog.add(btnSave);

        btnSave.addActionListener(e -> {
            String title = tfTitle.getText().trim();
            String author = tfAuthor.getText().trim();
            String genre = (String) cbGenre.getSelectedItem();
            String yearStr = tfYear.getText().trim();
            String desc = taDesc.getText().trim();
            String cover = tfCover.getText().trim();
            boolean future = chkFuture.isSelected();

            if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty() || cover.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Please fill in all required fields (Title, Author, Year, and Cover)!", "Fields Missing", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int year;
            try {
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(dialog, "Published year must be a numeric integer!", "Format Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Book book = editingBook == null ? new Book() : editingBook;
            book.setTitle(title);
            book.setAuthor(author);
            book.setGenre(genre);
            book.setPublishedYear(year);
            book.setDescription(desc);
            book.setImagePath(cover);
            book.setFuture(future);

            boolean success;
            if (editingBook == null) {
                success = catalogDAO.addBook(book);
            } else {
                success = catalogDAO.updateBook(book);
            }

            if (success) {
                JOptionPane.showMessageDialog(dialog, "Book details saved successfully!", "Saved", JOptionPane.INFORMATION_MESSAGE);
                dialog.dispose();
                refreshBookTable();
            } else {
                JOptionPane.showMessageDialog(dialog, "Failed to save book to the database.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private JLabel createDialogLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(241, 196, 15));
        return lbl;
    }

    private JTextField createDialogField(String text) {
        JTextField tf = new JTextField(text);
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tf.setBackground(new Color(20, 24, 30));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(255, 255, 255, 30), 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return tf;
    }

    private String handleImageSelection() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Book Cover Image");
        fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Image Files", "jpg", "png", "jpeg"));
        int userSelection = fileChooser.showOpenDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File sourceFile = fileChooser.getSelectedFile();
            String fileName = sourceFile.getName();
            
            // Try saving to build/classes/images and src/images
            File destDirSrc = new File("src/images");
            File destDirBuild = new File("build/classes/images");

            if (!destDirSrc.exists()) destDirSrc.mkdirs();
            if (!destDirBuild.exists()) destDirBuild.mkdirs();

            try {
                Files.copy(sourceFile.toPath(), new File(destDirSrc, fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                Files.copy(sourceFile.toPath(), new File(destDirBuild, fileName).toPath(), StandardCopyOption.REPLACE_EXISTING);
                return fileName; // Save name in DB
            } catch (Exception ex) {
                logger.log(java.util.logging.Level.SEVERE, "Failed to copy cover image", ex);
                JOptionPane.showMessageDialog(this, "Failed to save cover image: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        return null;
    }

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

    private class BackgroundPanel extends JPanel {
        private final Image bgImage;
        private final Image sidebarBgImage;

        public BackgroundPanel() {
            setLayout(null);
            bgImage = getScaledImage("/images/Untitled design.png", 1700, 700);
            sidebarBgImage = getScaledImage("/images/background image of lms-2 1.jpg", 260, 700);
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

            if (sidebarBgImage != null) {
                g2d.drawImage(sidebarBgImage, 0, 0, 260, getHeight(), this);
            } else {
                g2d.setColor(new Color(25, 29, 38));
                g2d.fillRect(0, 0, 260, getHeight());
            }

            g2d.setColor(new Color(0, 0, 0, 110));
            g2d.fillRect(260, 0, getWidth() - 260, getHeight());

            g2d.setColor(new Color(255, 255, 255, 30));
            g2d.drawLine(260, 0, 260, getHeight());

            g2d.dispose();
        }
    }

    private class SidebarButton extends JButton {
        private boolean active = false;
        private final Color hoverColor = new Color(255, 255, 255, 20);
        private final Color activeColor = new Color(255, 255, 255, 35);
        private final Color accentColor = new Color(241, 196, 15);

        public SidebarButton(String text) {
            super(text);
            setFont(new Font("Cambria", Font.BOLD, 22));
            setForeground(Color.WHITE);
            setFocusPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setBorder(BorderFactory.createEmptyBorder(0, 35, 0, 15));
            setHorizontalAlignment(SwingConstants.LEFT);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    repaint();
                }
            });
        }

        public void setActive(boolean active) {
            this.active = active;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (active) {
                g2d.setColor(activeColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.setColor(accentColor);
                g2d.fillRect(0, 0, 7, getHeight());
            } else if (getModel().isRollover()) {
                g2d.setColor(hoverColor);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }

            g2d.setColor(new Color(255, 255, 255, 15));
            g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

            g2d.dispose();
            super.paintComponent(g);
        }
    }
}
