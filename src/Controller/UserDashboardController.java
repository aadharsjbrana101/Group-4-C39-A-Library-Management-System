package Controller;

import Model.Book;
import Model.userdata;
import View.UserDashboard;
import View.UserLogin;
import dao.BookDao;
import dao.BorrowDao;
import dao.FineDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

// Sprint 1 (Kushal): Bind fee updater checks inside user flows
public class UserDashboardController {
    private final UserDashboard view;
    private final userdata currentUser;
    private final BookDao bookDao = new BookDao();
    private final BorrowDao borrowDao = new BorrowDao();
    private final FineDao fineDao = new FineDao();
    private List<Book> quickSearchResults = new ArrayList<>();

    public UserDashboardController(UserDashboard view, userdata user) {
        this.view = view;
        this.currentUser = user;

        // Visual initialisation
        this.view.getLblGreeting().setText("Welcome, " + user.getUsername() + "!");

        // Action Listeners
        this.view.getBtnDashboard().addActionListener(e -> { /* Already here */ });
        this.view.getBtnCatalog().addActionListener(new CatalogNavListener());
        this.view.getBtnMyBooks().addActionListener(new MyBooksNavListener());
        this.view.getBtnPayments().addActionListener(new PaymentsNavListener());
        this.view.getBtnLogout().addActionListener(new LogoutListener());
        this.view.getBtnQuickSearch().addActionListener(new QuickSearchListener());
        
        // Quick search list double click
        this.view.getLstQuickResults().addListSelectionListener(new QuickResultSelectionListener());
    }

    public void open() {
        this.view.setVisible(true);
        loadStats();
        loadRecentBooks();
    }

    private void loadStats() {
        try {
            // Trigger fine calculations to be fresh
            fineDao.calculateFines();
            
            int totalBorrowed = borrowDao.getTotalBorrowsCountForUser(currentUser.getId());
            int activeBorrows = borrowDao.getActiveBorrowsCountForUser(currentUser.getId());
            double pendingFines = fineDao.getUnpaidFinesSumForUser(currentUser.getId());

            view.getLblBorrowedVal().setText(String.valueOf(totalBorrowed));
            view.getLblActiveVal().setText(String.valueOf(activeBorrows));
            view.getLblFinesVal().setText(String.format("Rs. %.2f", pendingFines));
        } catch (SQLException e) {
            System.err.println("Error loading user dashboard stats: " + e.getMessage());
        }
    }

    private void loadRecentBooks() {
        try {
            List<Book> books = bookDao.getCatalogBooks("", "All Genres");
            view.getRecentContainer().removeAll();
            
            // Show up to 5 most recently added books
            int count = Math.min(books.size(), 5);
            for (int i = 0; i < count; i++) {
                Book book = books.get(i);
                View.BookCard card = new View.BookCard(book);
                
                // Add click listener on book card to open details
                card.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        openBookDetails(book);
                    }
                });
                view.getRecentContainer().add(card);
            }
            view.getRecentContainer().revalidate();
            view.getRecentContainer().repaint();
        } catch (SQLException e) {
            System.err.println("Error loading recent books: " + e.getMessage());
        }
    }

    private void openBookDetails(Book book) {
        View.BookDetailView detailView = new View.BookDetailView(currentUser);
        BookDetailController bdc = new BookDetailController(detailView, book, currentUser);
        bdc.open();
        view.dispose();
    }

    // Sidebar navigation listeners
    class CatalogNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.CatalogView cv = new View.CatalogView();
            CatalogController cc = new CatalogController(cv, currentUser);
            cc.open();
            view.dispose();
        }
    }

    class MyBooksNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.UserBooksView ubv = new View.UserBooksView(currentUser);
            UserBooksController ubc = new UserBooksController(ubv, currentUser);
            ubc.open();
            view.dispose();
        }
    }

    class PaymentsNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.UserPaymentsView upv = new View.UserPaymentsView(currentUser);
            UserPaymentsController upc = new UserPaymentsController(upv, currentUser);
            upc.open();
            view.dispose();
        }
    }

    class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                view.dispose();
                UserLogin login = new UserLogin();
                LoginController lc = new LoginController(login);
                lc.open();
            }
        }
    }

    // Quick Search action listener
    class QuickSearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String query = view.getTxtQuickSearch().getText().trim();
            if (query.isEmpty()) {
                return;
            }
            try {
                quickSearchResults = bookDao.getCatalogBooks(query, "All Genres");
                DefaultListModel<String> listModel = new DefaultListModel<>();
                for (Book b : quickSearchResults) {
                    listModel.addElement(b.getTitle() + " - " + b.getAuthor());
                }
                if (quickSearchResults.isEmpty()) {
                    listModel.addElement("No books found.");
                }
                view.getLstQuickResults().setModel(listModel);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // Quick Result Selection listener
    class QuickResultSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int index = view.getLstQuickResults().getSelectedIndex();
                if (index >= 0 && index < quickSearchResults.size()) {
                    Book selectedBook = quickSearchResults.get(index);
                    openBookDetails(selectedBook);
                }
            }
        }
    }
}
