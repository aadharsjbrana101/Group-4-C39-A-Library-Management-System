package Controller;

import Model.Book;
import Model.UserData;
import View.CatalogView;
import View.UserLogin;
import dao.BookDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

public class CatalogController {
    private final CatalogView view;
    private final BookDao bookDao;
    private final UserData currentUser;

    public CatalogController(CatalogView view, UserData user) {
        this.view = view;
        this.currentUser = user;
        this.bookDao = new BookDao();

        // Register listeners (Flow Step 5)
        this.view.addSearchListener(new SearchListener());
        this.view.addGenreFilterListener(new FilterListener());
        this.view.addDashboardListener(new DashboardNavListener());
        this.view.addMyBooksListener(new MyBooksNavListener());
        this.view.addPaymentsListener(new PaymentsNavListener());
        this.view.addLogoutListener(new LogoutListener());
    }

    public void open() {
        this.view.setVisible(true);
        loadInitialData();
    }

    private void loadInitialData() {
        try {
            // Load Future Releases
            List<Book> future = bookDao.getFutureReleases();
            view.setFutureReleases(future, new CardClickListener());

            // Load Catalog Books
            List<Book> catalog = bookDao.getCatalogBooks("", "All Genres");
            view.setCatalogBooks(catalog, new CardClickListener());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error loading catalog data: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void performSearch() {
        try {
            String query = view.getSearchField().getText();
            String genre = view.getGenreComboBox().getSelectedItem().toString();

            List<Book> filtered = bookDao.getCatalogBooks(query, genre);
            view.setCatalogBooks(filtered, new CardClickListener());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error during search: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    // Book Card Click Listener
    class CardClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            Object src = e.getSource();
            if (src instanceof View.BookCard) {
                View.BookCard card = (View.BookCard) src;
                Book book = card.getBook();
                if (book != null) {
                    View.BookDetailView detailView = new View.BookDetailView(currentUser);
                    BookDetailController bdc = new BookDetailController(detailView, book, currentUser);
                    bdc.open();
                    view.dispose();
                }
            }
        }
    }

    // Navigation Listeners
    class DashboardNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.UserDashboard ud = new View.UserDashboard(currentUser);
            UserDashboardController udc = new UserDashboardController(ud, currentUser);
            udc.open();
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

    // Search action listener
    class SearchListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            performSearch();
        }
    }

    // Genre filter action listener
    class FilterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            performSearch();
        }
    }

    // Logout listener
    class LogoutListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                view.dispose();
                UserLogin loginFrame = new UserLogin();
                LoginController lc = new LoginController(loginFrame);
                lc.open();
            }
        }
    }
}
