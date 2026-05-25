package View;

import Model.Book;
import dao.bookcatalogDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Visual Overhaul of the User Catalog with full NetBeans GUI Builder compatibility.
 * Keeps NetBeans' guarded blocks completely untouched, and overrides the layout
 * programmatically at runtime in customSetup() to render high-fidelity graphics.
 * @author Amanm
 */
public class UserCaataalog extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(UserCaataalog.class.getName());
    
    // Core data and layout managers
    private final bookcatalogDAO catalogDAO = new bookcatalogDAO();
    private List<Book> allBooks = new ArrayList<>();
    private List<Book> displayedBooks = new ArrayList<>();
    private Book selectedBook = null;

    // View panels
    private BackgroundPanel backgroundPanel;
    private CatalogViewPanel catalogPanel;
    private MyBooksViewPanel myBooksPanel;
    private BookDetailsViewPanel detailsPanel;
    private DashboardViewPanel dashboardPanel;
    private PaymentsViewPanel paymentsPanel;

    // Active views
    private JPanel activeContentPanel;

    // Custom sidebar buttons to avoid casting issues
    private SidebarButton sidebarBtn1;
    private SidebarButton sidebarBtn2;
    private SidebarButton sidebarBtn3;
    private SidebarButton sidebarBtn4;
    private SidebarButton sidebarBtn5;

    // Drag and Drop state
    private boolean isDragging = false;
    private Book draggedBook = null;
    private DragGlassPane glassPane;

    /**
     * Creates new form UserCaataalog
     */
    public UserCaataalog() {
        // First load the NetBeans standard generated components
        initComponents();
        
        // Load data safely
        try {
            allBooks = catalogDAO.getAllBooks();
        } catch (Exception e) {
            logger.log(java.util.logging.Level.SEVERE, "Failed to connect to database.", e);
        }

        // Run our premium visual overlay without corrupting NetBeans form metadata
        customSetup();
    }

    /**
     * Programmatic high-fidelity setup. Removes standard Swing components
     * and wraps the window in glassmorphic, visual components at runtime.
     */
    private void customSetup() {
        setTitle("LMS - High Fidelity User Book Catalog");
        setResizable(false);

        // Setup custom Drag-and-Drop glass pane
        glassPane = new DragGlassPane();
        setGlassPane(glassPane);

        // Remove NetBeans' default absolute components
        getContentPane().remove(jButton1);
        getContentPane().remove(jButton2);
        getContentPane().remove(jButton3);
        getContentPane().remove(jButton4);
        getContentPane().remove(jLabel1);
        getContentPane().remove(jLabel2);
        getContentPane().remove(jLabel3);
        getContentPane().remove(jLabel4);

        // Inject our background panel as the primary content pane
        backgroundPanel = new BackgroundPanel();
        setContentPane(backgroundPanel);

        // Initialize beautiful Sidebar Buttons
        sidebarBtn1 = new SidebarButton("Dashboard");
        sidebarBtn2 = new SidebarButton("Catalog");
        sidebarBtn3 = new SidebarButton("My Books");
        sidebarBtn4 = new SidebarButton("Payments");
        sidebarBtn5 = new SidebarButton("Logout");

        // Layout matching absolute positions
        sidebarBtn1.setBounds(0, 0, 260, 120);
        sidebarBtn2.setBounds(0, 120, 260, 120);
        sidebarBtn3.setBounds(0, 240, 260, 120);
        sidebarBtn4.setBounds(0, 360, 260, 120);
        sidebarBtn5.setBounds(0, 480, 260, 120);

        backgroundPanel.add(sidebarBtn1);
        backgroundPanel.add(sidebarBtn2);
        backgroundPanel.add(sidebarBtn3);
        backgroundPanel.add(sidebarBtn4);
        backgroundPanel.add(sidebarBtn5);

        // Connect view swapping action listeners
        sidebarBtn1.addActionListener(e -> switchView("Dashboard"));
        sidebarBtn2.addActionListener(e -> switchView("Catalog"));
        sidebarBtn3.addActionListener(e -> switchView("My Books"));
        sidebarBtn4.addActionListener(e -> switchView("Payments"));
        sidebarBtn5.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to Logout?", "Logout confirmation", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        // Initialize high-fidelity content panels
        catalogPanel = new CatalogViewPanel();
        myBooksPanel = new MyBooksViewPanel();
        detailsPanel = new BookDetailsViewPanel();
        dashboardPanel = new DashboardViewPanel();
        paymentsPanel = new PaymentsViewPanel();

        // Default active view
        switchView("Catalog");
    }

    /**
     * Swaps the active content panel taking space to the right of the sidebar
     */
    private void switchView(String viewName) {
        if (activeContentPanel != null) {
            backgroundPanel.remove(activeContentPanel);
        }

        // Reset active sidebar highlights
        sidebarBtn1.setActive(false);
        sidebarBtn2.setActive(false);
        sidebarBtn3.setActive(false);
        sidebarBtn4.setActive(false);
        sidebarBtn5.setActive(false);

        switch (viewName) {
            case "Dashboard":
                activeContentPanel = dashboardPanel;
                sidebarBtn1.setActive(true);
                dashboardPanel.refreshStats();
                break;
            case "Catalog":
                activeContentPanel = catalogPanel;
                sidebarBtn2.setActive(true);
                catalogPanel.refreshCatalog();
                break;
            case "My Books":
                activeContentPanel = myBooksPanel;
                sidebarBtn3.setActive(true);
                myBooksPanel.refreshList();
                break;
            case "Payments":
                activeContentPanel = paymentsPanel;
                sidebarBtn4.setActive(true);
                break;
            case "BookDetails":
                activeContentPanel = detailsPanel;
                sidebarBtn2.setActive(true);
                detailsPanel.displayBook(selectedBook);
                break;
        }

        // Perfect fitting to the right of sidebar
        activeContentPanel.setBounds(260, 0, 1440, 700);
        backgroundPanel.add(activeContentPanel);
        
        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    /**
     * Triggers capsule sliding success alerts
     */
    public void showToast(String message, Color color) {
        ToastNotification toast = new ToastNotification(message, color);
        backgroundPanel.add(toast);
        backgroundPanel.setComponentZOrder(toast, 0); // Overlay on top
        toast.showNotification();
    }

    /**
     * Image utility supporting smooth scaling and runtime failover
     */
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

    private void paintRoundedImage(Graphics2D g2d, Image img, int x, int y, int w, int h, int arc) {
        g2d.setClip(new java.awt.geom.RoundRectangle2D.Double(x, y, w, h, arc, arc));
        g2d.drawImage(img, x, y, w, h, null);
        g2d.setClip(null);
    }

    // =========================================================================
    // INNER CLASSES FOR PREMIUM GUI COMPONENTS & VIEW PANELS
    // =========================================================================

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

    private class BookCard extends JPanel {
        private final Book book;
        private final boolean isFutureRelease;
        private final Image coverImage;
        private boolean isHovered = false;
        private Point dragStartPoint;

        public BookCard(Book book, boolean isFutureRelease) {
            this.book = book;
            this.isFutureRelease = isFutureRelease;

            int w = isFutureRelease ? 130 : 160;
            int h = isFutureRelease ? 180 : 220;

            setPreferredSize(new Dimension(w, h));
            setMinimumSize(new Dimension(w, h));
            setMaximumSize(new Dimension(w, h));
            setOpaque(false);

            String path = "/images/" + book.getImagePath();
            coverImage = getScaledImage(path, w, h - (isFutureRelease ? 35 : 45));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (isFutureRelease) {
                        showToast("Upcoming release: " + book.getTitle() + "!", new Color(230, 126, 34));
                    } else {
                        selectedBook = book;
                        switchView("BookDetails");
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if (!isFutureRelease) {
                        dragStartPoint = e.getPoint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (isDragging) {
                        endDrag(e);
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    isHovered = true;
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    isHovered = false;
                    repaint();
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (isFutureRelease || dragStartPoint == null) return;
                    
                    int dragThreshold = 8;
                    if (e.getPoint().distance(dragStartPoint) > dragThreshold) {
                        startDrag(e);
                    }
                }
            });
        }

        private void startDrag(MouseEvent e) {
            isDragging = true;
            draggedBook = book;

            Point framePoint = SwingUtilities.convertPoint(this, e.getPoint(), UserCaataalog.this);
            boolean isOverMyBooks = (framePoint.x >= 0 && framePoint.x <= 260 && framePoint.y >= 240 && framePoint.y <= 360);
            
            sidebarBtn3.setActive(isOverMyBooks);

            glassPane.setDragInfo(framePoint, coverImage, book.getTitle(), isOverMyBooks);
            glassPane.setVisible(true);
            glassPane.repaint();
        }

        private void endDrag(MouseEvent e) {
            isDragging = false;
            glassPane.setVisible(false);
            sidebarBtn3.setActive(false);

            Point framePoint = SwingUtilities.convertPoint(this, e.getPoint(), UserCaataalog.this);
            boolean isOverMyBooks = (framePoint.x >= 0 && framePoint.x <= 260 && framePoint.y >= 240 && framePoint.y <= 360);

            if (isOverMyBooks) {
                boolean success = catalogDAO.borrowBook(1, book.getId());
                if (success) {
                    showToast("Borrowed: " + book.getTitle(), new Color(46, 204, 113));
                    myBooksPanel.refreshList();
                } else {
                    showToast("Already Borrowed: " + book.getTitle(), new Color(230, 126, 34));
                }
            }
            draggedBook = null;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();
            int imgH = h - (isFutureRelease ? 35 : 45);

            if (isHovered) {
                g2d.setColor(new Color(241, 196, 15, 120));
                g2d.setStroke(new BasicStroke(4));
                g2d.drawRoundRect(1, 1, w - 2, imgH - 2, 10, 10);
            }

            if (coverImage != null) {
                paintRoundedImage(g2d, coverImage, 3, 3, w - 6, imgH - 6, 10);
            } else {
                GradientPaint gp = new GradientPaint(3, 3, new Color(44, 62, 80), w - 6, imgH - 6, new Color(26, 36, 43));
                g2d.setPaint(gp);
                g2d.fillRoundRect(3, 3, w - 6, imgH - 6, 10, 10);

                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.drawRoundRect(3, 3, w - 6, imgH - 6, 10, 10);

                g2d.setColor(new Color(241, 196, 15, 60));
                g2d.fillRect(10, 3, 4, imgH - 6);

                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, isFutureRelease ? 11 : 13));
                String displayTitle = book.getTitle();
                if (g2d.getFontMetrics().stringWidth(displayTitle) > w - 20) {
                    displayTitle = displayTitle.substring(0, Math.min(displayTitle.length(), 10)) + "...";
                }
                g2d.drawString(displayTitle, (w - g2d.getFontMetrics().stringWidth(displayTitle)) / 2, imgH / 2);
            }

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, isFutureRelease ? 12 : 14));
            String title = book.getTitle();
            if (g2d.getFontMetrics().stringWidth(title) > w - 10) {
                title = title.substring(0, Math.min(title.length(), 13)) + "...";
            }
            g2d.drawString(title, (w - g2d.getFontMetrics().stringWidth(title)) / 2, imgH + 18);

            g2d.setColor(new Color(200, 200, 200));
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, isFutureRelease ? 10 : 11));
            String author = book.getAuthor();
            if (g2d.getFontMetrics().stringWidth(author) > w - 10) {
                author = author.substring(0, Math.min(author.length(), 15)) + "...";
            }
            g2d.drawString(author, (w - g2d.getFontMetrics().stringWidth(author)) / 2, imgH + 32);

            g2d.dispose();
        }
    }

    private class ToastNotification extends JPanel {
        private final String message;
        private final Color bgColor;
        private float opacity = 0.0f;
        private Timer fadeInTimer;
        private Timer fadeOutTimer;
        private Timer displayTimer;

        public ToastNotification(String message, Color bgColor) {
            this.message = message;
            this.bgColor = bgColor;
            setOpaque(false);
            setBounds(700, -60, 400, 50);

            fadeInTimer = new Timer(12, new ActionListener() {
                int targetY = 20;
                int currentY = -60;
                @Override
                public void actionPerformed(ActionEvent e) {
                    opacity += 0.08f;
                    currentY += 6;
                    if (opacity >= 1.0f) {
                        opacity = 1.0f;
                        currentY = targetY;
                        fadeInTimer.stop();
                        displayTimer.start();
                    }
                    setLocation(getX(), currentY);
                    repaint();
                }
            });

            displayTimer = new Timer(2200, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    displayTimer.stop();
                    fadeOutTimer.start();
                }
            });

            fadeOutTimer = new Timer(12, new ActionListener() {
                int currentY = 20;
                @Override
                public void actionPerformed(ActionEvent e) {
                    opacity -= 0.08f;
                    currentY -= 6;
                    if (opacity <= 0.0f) {
                        opacity = 0.0f;
                        fadeOutTimer.stop();
                        if (getParent() != null) {
                            getParent().remove(ToastNotification.this);
                            getParent().repaint();
                        }
                    }
                    setLocation(getX(), currentY);
                    repaint();
                }
            });
        }

        public void showNotification() {
            fadeInTimer.start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity));

            g2d.setColor(bgColor);
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

            g2d.setColor(new Color(255, 255, 255, 60));
            g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 15));
            FontMetrics fm = g2d.getFontMetrics();
            g2d.drawString("✓  " + message, (getWidth() - fm.stringWidth("✓  " + message)) / 2, (getHeight() - fm.getHeight()) / 2 + fm.getAscent());

            g2d.dispose();
        }
    }

    private class DragGlassPane extends JComponent {
        private Point mousePoint;
        private Image coverImage;
        private String bookTitle;
        private boolean isOverMyBooks;

        public void setDragInfo(Point mousePoint, Image coverImage, String bookTitle, boolean isOverMyBooks) {
            this.mousePoint = mousePoint;
            this.coverImage = coverImage;
            this.bookTitle = bookTitle;
            this.isOverMyBooks = isOverMyBooks;
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (mousePoint == null) return;
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int x = mousePoint.x;
            int y = mousePoint.y;
            int w = 90;
            int h = 130;

            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f));

            if (coverImage != null) {
                paintRoundedImage(g2d, coverImage, x - w / 2, y - h / 2, w, h, 10);
            } else {
                GradientPaint gp = new GradientPaint(x - w / 2, y - h / 2, new Color(52, 73, 94), x + w / 2, y + h / 2, new Color(44, 62, 80));
                g2d.setPaint(gp);
                g2d.fillRoundRect(x - w / 2, y - h / 2, w, h, 10, 10);
            }

            if (isOverMyBooks) {
                g2d.setColor(new Color(46, 204, 113));
                g2d.setStroke(new BasicStroke(5));
                g2d.drawRoundRect(x - w / 2 - 6, y - h / 2 - 6, w + 12, h + 12, 12, 12);
                
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2d.drawString("Drop to Borrow!", x - w/2, y - h/2 - 15);
            }

            g2d.dispose();
        }
    }

    private class CatalogViewPanel extends JPanel {
        private final JLabel mainTitle;
        private final JLabel futureSubtitle;
        private final JPanel futurePanel;
        private final JPanel filterBar;
        private final JTextField searchField;
        private final JComboBox<String> genreBox;
        private final JPanel gridPanel;
        private final JScrollPane scrollPane;

        public CatalogViewPanel() {
            setLayout(null);
            setOpaque(false);

            mainTitle = new JLabel("Book Catalog");
            mainTitle.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 38));
            mainTitle.setForeground(Color.WHITE);
            mainTitle.setBounds(50, 20, 300, 50);
            add(mainTitle);

            futureSubtitle = new JLabel("Future releases");
            futureSubtitle.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 24));
            futureSubtitle.setForeground(new Color(230, 230, 230));
            futureSubtitle.setBounds(50, 75, 200, 35);
            add(futureSubtitle);

            futurePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 35, 0));
            futurePanel.setOpaque(false);
            futurePanel.setBounds(50, 115, 1340, 190);
            add(futurePanel);

            filterBar = new JPanel(null);
            filterBar.setOpaque(false);
            filterBar.setBounds(50, 315, 1340, 45);
            add(filterBar);

            searchField = new JTextField("Search books...") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(0, 0, 0, 70));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2d.setColor(new Color(255, 255, 255, 45));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                    g2d.dispose();
                    super.paintComponent(g);
                }
            };
            searchField.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            searchField.setForeground(new Color(180, 180, 180));
            searchField.setOpaque(false);
            searchField.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
            searchField.setCaretColor(Color.WHITE);
            searchField.setBounds(0, 5, 300, 35);
            filterBar.add(searchField);

            searchField.addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (searchField.getText().equals("Search books...")) {
                        searchField.setText("");
                        searchField.setForeground(Color.WHITE);
                    }
                }
                @Override
                public void focusLost(FocusEvent e) {
                    if (searchField.getText().isEmpty()) {
                        searchField.setText("Search books...");
                        searchField.setForeground(new Color(180, 180, 180));
                    }
                }
            });

            genreBox = new JComboBox<>(new String[]{"All Genres", "Classic", "Fantasy", "Comedy", "Adventure", "Sci-Fi", "Mystery"});
            genreBox.setFont(new Font("Segoe UI", Font.BOLD, 14));
            genreBox.setForeground(Color.WHITE);
            genreBox.setBackground(new Color(30, 34, 42));
            genreBox.setBounds(330, 5, 150, 35);
            filterBar.add(genreBox);

            gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 45, 15));
            gridPanel.setOpaque(false);

            scrollPane = new JScrollPane(gridPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(null);
            scrollPane.setBounds(50, 375, 1340, 270);
            
            scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            add(scrollPane);

            searchField.addKeyListener(new KeyAdapter() {
                @Override
                public void keyReleased(KeyEvent e) {
                    applyFilters();
                }
            });

            genreBox.addActionListener(e -> applyFilters());
        }

        public void refreshCatalog() {
            allBooks = catalogDAO.getAllBooks();
            
            futurePanel.removeAll();
            List<Book> future = allBooks.stream().filter(Book::isFuture).collect(Collectors.toList());
            for (Book b : future) {
                futurePanel.add(new BookCard(b, true));
            }

            applyFilters();
            futurePanel.revalidate();
            futurePanel.repaint();
        }

        private void applyFilters() {
            gridPanel.removeAll();
            
            String rawQuery = searchField.getText().trim().toLowerCase();
            final String query = rawQuery.equals("search books...") ? "" : rawQuery;
            
            String genre = (String) genreBox.getSelectedItem();

            List<Book> filtered = allBooks.stream()
                .filter(b -> !b.isFuture())
                .filter(b -> {
                    if (genre.equals("All Genres")) return true;
                    return b.getGenre().equalsIgnoreCase(genre);
                })
                .filter(b -> {
                    if (query.isEmpty()) return true;
                    return b.getTitle().toLowerCase().contains(query) || b.getAuthor().toLowerCase().contains(query);
                })
                .collect(Collectors.toList());

            for (Book b : filtered) {
                gridPanel.add(new BookCard(b, false));
            }

            int rowCount = (int) Math.ceil((double) filtered.size() / 6.0);
            gridPanel.setPreferredSize(new Dimension(1300, Math.max(250, rowCount * 260)));

            gridPanel.revalidate();
            gridPanel.repaint();
        }
    }

    private class MyBooksViewPanel extends JPanel {
        private final JLabel mainTitle;
        private final JPanel gridPanel;
        private final JLabel emptyLabel;

        public MyBooksViewPanel() {
            setLayout(null);
            setOpaque(false);

            mainTitle = new JLabel("My Books");
            mainTitle.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 38));
            mainTitle.setForeground(Color.WHITE);
            mainTitle.setBounds(50, 20, 400, 50);
            add(mainTitle);

            emptyLabel = new JLabel("Your reading list is empty. Drag books here to borrow!");
            emptyLabel.setFont(new Font("Segoe UI", Font.ITALIC, 20));
            emptyLabel.setForeground(new Color(180, 180, 180));
            emptyLabel.setBounds(50, 100, 800, 40);
            add(emptyLabel);

            gridPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 45, 25));
            gridPanel.setOpaque(false);

            JScrollPane scrollPane = new JScrollPane(gridPanel);
            scrollPane.setOpaque(false);
            scrollPane.getViewport().setOpaque(false);
            scrollPane.setBorder(null);
            scrollPane.setBounds(50, 100, 1340, 530);
            add(scrollPane);
        }

        public void refreshList() {
            gridPanel.removeAll();
            List<Book> borrowed = catalogDAO.getBorrowedBooks(1);

            if (borrowed.isEmpty()) {
                emptyLabel.setVisible(true);
            } else {
                emptyLabel.setVisible(false);
                for (Book b : borrowed) {
                    JPanel returnCard = new JPanel(new BorderLayout());
                    returnCard.setOpaque(false);
                    
                    BookCard card = new BookCard(b, false);
                    returnCard.add(card, BorderLayout.CENTER);
                    
                    JButton btnReturn = new JButton("Return") {
                        @Override
                        protected void paintComponent(Graphics g) {
                            Graphics2D g2d = (Graphics2D) g.create();
                            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                            g2d.setColor(new Color(231, 76, 60, 180));
                            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                            g2d.dispose();
                            super.paintComponent(g);
                        }
                    };
                    btnReturn.setFont(new Font("Segoe UI", Font.BOLD, 12));
                    btnReturn.setForeground(Color.WHITE);
                    btnReturn.setContentAreaFilled(false);
                    btnReturn.setFocusPainted(false);
                    btnReturn.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    btnReturn.setPreferredSize(new Dimension(160, 25));
                    
                    btnReturn.addActionListener(e -> {
                        boolean success = catalogDAO.returnBook(1, b.getId());
                        if (success) {
                            showToast("Returned: " + b.getTitle(), new Color(46, 204, 113));
                            refreshList();
                        }
                    });
                    
                    returnCard.add(btnReturn, BorderLayout.SOUTH);
                    gridPanel.add(returnCard);
                }
            }
            gridPanel.revalidate();
            gridPanel.repaint();
        }
    }

    private class BookDetailsViewPanel extends JPanel {
        private final JLabel mainTitle;
        private final JButton btnBack;
        private final JPanel goldBox;
        private JLabel imgLabel;
        private JLabel titleLabel;
        private JLabel authorLabel;
        private JLabel genreLabel;
        private JLabel publishedLabel;
        private JTextArea descArea;
        private JButton btnAction;

        public BookDetailsViewPanel() {
            setLayout(null);
            setOpaque(false);

            mainTitle = new JLabel("Book Details");
            mainTitle.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 38));
            mainTitle.setForeground(Color.WHITE);
            mainTitle.setBounds(50, 20, 300, 50);
            add(mainTitle);

            btnBack = new JButton("← Back to Catalog") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(255, 255, 255, 20));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                    g2d.dispose();
                    super.paintComponent(g);
                }
            };
            btnBack.setFont(new Font("Segoe UI", Font.BOLD, 14));
            btnBack.setForeground(Color.WHITE);
            btnBack.setFocusPainted(false);
            btnBack.setContentAreaFilled(false);
            btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnBack.setBounds(1180, 30, 200, 40);
            btnBack.addActionListener(e -> switchView("Catalog"));
            add(btnBack);

            goldBox = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(new Color(0, 0, 0, 160));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                    g2d.setColor(new Color(241, 196, 15));
                    g2d.setStroke(new BasicStroke(3));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                    
                    g2d.dispose();
                }
            };
            goldBox.setOpaque(false);
            goldBox.setBounds(50, 95, 1340, 520);
            add(goldBox);

            imgLabel = new JLabel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(new Color(241, 196, 15, 150));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                    
                    g2d.dispose();
                    super.paintComponent(g);
                }
            };
            imgLabel.setBounds(50, 60, 250, 360);
            goldBox.add(imgLabel);

            titleLabel = new JLabel("Book Title");
            titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 30));
            titleLabel.setForeground(new Color(241, 196, 15));
            titleLabel.setBounds(340, 50, 900, 45);
            goldBox.add(titleLabel);

            authorLabel = new JLabel("Author: ");
            authorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            authorLabel.setForeground(Color.WHITE);
            authorLabel.setBounds(340, 110, 900, 30);
            goldBox.add(authorLabel);

            genreLabel = new JLabel("Genre: ");
            genreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            genreLabel.setForeground(Color.WHITE);
            genreLabel.setBounds(340, 150, 900, 30);
            goldBox.add(genreLabel);

            publishedLabel = new JLabel("Published: ");
            publishedLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            publishedLabel.setForeground(Color.WHITE);
            publishedLabel.setBounds(340, 190, 900, 30);
            goldBox.add(publishedLabel);

            descArea = new JTextArea("Along the way, Harry, Ron, and Hermione discover the existence of three mystical objects that grant their possessor infinite power...");
            descArea.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            descArea.setForeground(new Color(220, 220, 220));
            descArea.setLineWrap(true);
            descArea.setWrapStyleWord(true);
            descArea.setEditable(false);
            descArea.setOpaque(false);
            descArea.setBounds(340, 240, 900, 160);
            goldBox.add(descArea);

            btnAction = new JButton("Borrow Book") {
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
            btnAction.setFont(new Font("Segoe UI", Font.BOLD, 18));
            btnAction.setForeground(new Color(30, 30, 30));
            btnAction.setContentAreaFilled(false);
            btnAction.setFocusPainted(false);
            btnAction.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAction.setBounds(340, 420, 250, 50);
            goldBox.add(btnAction);
        }

        public void displayBook(Book b) {
            titleLabel.setText(b.getTitle());
            authorLabel.setText("Author: " + b.getAuthor());
            genreLabel.setText("Genre: " + b.getGenre());
            publishedLabel.setText("Published: " + b.getPublishedYear());
            descArea.setText(b.getDescription());

            String path = "/images/" + b.getImagePath();
            Image scaled = getScaledImage(path, 242, 352);
            if (scaled != null) {
                BufferedImage bufferedImage = new BufferedImage(242, 352, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = bufferedImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                paintRoundedImage(g2, scaled, 0, 0, 242, 352, 10);
                g2.dispose();
                imgLabel.setIcon(new ImageIcon(bufferedImage));
            } else {
                imgLabel.setIcon(null);
                imgLabel.setText("No cover image");
                imgLabel.setForeground(Color.LIGHT_GRAY);
                imgLabel.setHorizontalAlignment(SwingConstants.CENTER);
            }

            boolean isBorrowed = catalogDAO.isBorrowed(1, b.getId());
            if (isBorrowed) {
                btnAction.setText("Return Book");
                for (ActionListener al : btnAction.getActionListeners()) btnAction.removeActionListener(al);
                btnAction.addActionListener(e -> {
                    boolean ok = catalogDAO.returnBook(1, b.getId());
                    if (ok) {
                        showToast("Returned: " + b.getTitle(), new Color(46, 204, 113));
                        displayBook(b);
                    }
                });
            } else {
                btnAction.setText("Borrow Book");
                for (ActionListener al : btnAction.getActionListeners()) btnAction.removeActionListener(al);
                btnAction.addActionListener(e -> {
                    boolean ok = catalogDAO.borrowBook(1, b.getId());
                    if (ok) {
                        showToast("Borrowed: " + b.getTitle(), new Color(46, 204, 113));
                        displayBook(b);
                    }
                });
            }
        }
    }

    private class DashboardViewPanel extends JPanel {
        private final JLabel mainTitle;
        private final JPanel cardContainer;
        private final JLabel stat1, stat2, stat3, stat4;

        public DashboardViewPanel() {
            setLayout(null);
            setOpaque(false);

            mainTitle = new JLabel("LMS Dashboard");
            mainTitle.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 38));
            mainTitle.setForeground(Color.WHITE);
            mainTitle.setBounds(50, 20, 400, 50);
            add(mainTitle);

            cardContainer = new JPanel(new GridLayout(2, 2, 45, 45));
            cardContainer.setOpaque(false);
            cardContainer.setBounds(50, 100, 1340, 500);
            add(cardContainer);

            stat1 = createStatCard("Total Library Catalog", "8 Books Available", new Color(52, 152, 219));
            stat2 = createStatCard("My Borrowed Books", "0 Borrowed", new Color(46, 204, 113));
            stat3 = createStatCard("Upcoming Future Releases", "4 Scheduled", new Color(241, 196, 15));
            stat4 = createStatCard("LMS Account Tier", "Premium Member", new Color(155, 89, 182));

            cardContainer.add(stat1);
            cardContainer.add(stat2);
            cardContainer.add(stat3);
            cardContainer.add(stat4);
        }

        public void refreshStats() {
            List<Book> borrowed = catalogDAO.getBorrowedBooks(1);
            stat2.setText("<html><center><font size='6' color='#ffffff'>" + borrowed.size() + "</font><br><font size='4' color='#cccccc'>Borrowed Books</font></center></html>");
        }

        private JLabel createStatCard(String title, String desc, Color highlight) {
            JLabel card = new JLabel("<html><center><font size='6' color='#ffffff'>" + desc + "</font><br><font size='4' color='#cccccc'>" + title + "</font></center></html>", SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    g2d.setColor(new Color(0, 0, 0, 130));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                    g2d.setColor(new Color(highlight.getRed(), highlight.getGreen(), highlight.getBlue(), 120));
                    g2d.setStroke(new BasicStroke(2));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                    
                    g2d.dispose();
                    super.paintComponent(g);
                }
            };
            card.setOpaque(false);
            card.setFont(new Font("Segoe UI", Font.BOLD, 18));
            return card;
        }
    }

    private class PaymentsViewPanel extends JPanel {
        public PaymentsViewPanel() {
            setLayout(null);
            setOpaque(false);

            JLabel mainTitle = new JLabel("Payments Portal");
            mainTitle.setFont(new Font("Cambria", Font.BOLD | Font.ITALIC, 38));
            mainTitle.setForeground(Color.WHITE);
            mainTitle.setBounds(50, 20, 400, 50);
            add(mainTitle);

            JPanel paymentCard = new JPanel(null) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setColor(new Color(0, 0, 0, 140));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);
                    
                    g2d.setColor(new Color(255, 255, 255, 25));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                    g2d.dispose();
                }
            };
            paymentCard.setOpaque(false);
            paymentCard.setBounds(50, 100, 1340, 500);
            add(paymentCard);

            JLabel heading = new JLabel("LMS Fine and Subscription Tracker");
            heading.setFont(new Font("Segoe UI", Font.BOLD, 22));
            heading.setForeground(new Color(241, 196, 15));
            heading.setBounds(50, 40, 500, 30);
            paymentCard.add(heading);

            JLabel line1 = new JLabel("Active Premium Subscription:   $4.99 / Month (Paid)");
            line1.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            line1.setForeground(Color.WHITE);
            line1.setBounds(50, 100, 600, 30);
            paymentCard.add(line1);

            JLabel line2 = new JLabel("Pending Library Fines:                   $0.00 (No late returns!)");
            line2.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            line2.setForeground(new Color(46, 204, 113));
            line2.setBounds(50, 150, 600, 30);
            paymentCard.add(line2);

            JLabel line3 = new JLabel("Next Billing Cycle Renewal:     June 1, 2026");
            line3.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            line3.setForeground(Color.LIGHT_GRAY);
            line3.setBounds(50, 200, 600, 30);
            paymentCard.add(line3);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new java.awt.Dimension(1700, 700));
        getContentPane().setLayout(null);

        jButton1.setText("jButton1");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(0, 0, 260, 120);

        jButton2.setText("jButton1");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(0, 120, 260, 120);

        jButton3.setText("jButton1");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(0, 240, 260, 120);

        jButton4.setText("jButton1");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton4);
        jButton4.setBounds(0, 360, 260, 120);

        jLabel4.setFont(new java.awt.Font("Cambria Math", 3, 24)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Future Releases");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(330, 100, 180, 50);

        jLabel3.setFont(new java.awt.Font("Cambria Math", 3, 36)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Book Catalog");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(330, 30, 240, 60);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/background image of lms-2 1.jpg"))); // NOI18N
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 0, 260, 700);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Untitled design.png"))); // NOI18N
        getContentPane().add(jLabel1);
        jLabel1.setBounds(0, -10, 1700, 720);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new UserCaataalog().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
