package Controller;

import View.AdminDashboard;
import dao.bookDao;
import Model.bookdata;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class AdminDashboardController {

    private final bookDao bookdao = new bookDao();
    private final AdminDashboard dashboardView;

    public AdminDashboardController(AdminDashboard dashboardView) {
        this.dashboardView = dashboardView;

        loadDashboardCards();
        loadBooksTable();

        dashboardView.addCatalogListener(new CatalogListener());
        dashboardView.addLogoutListener(new LogoutListener());
        dashboardView.addDashboardListener(new DashboardListener());
    }

    // ── Populate the stat cards ─────────────────────────────────────
    private void loadDashboardCards() {
        int totalBooks = bookdao.getTotalBookCount();
        int issuedBooks = bookdao.getIssuedBookCount();
        dashboardView.setTotalBooksLabel(String.valueOf(totalBooks));
        dashboardView.setIssuedBooksLabel(String.valueOf(issuedBooks));
    }

    // ── Populate jTable1 with book inventory ────────────────────────
    private void loadBooksTable() {
        List<bookdata> books = bookdao.getAllBooks();

        String[] columns = {"ID", "Title", "Author", "Genre", "Total", "Available", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        for (bookdata book : books) {
            model.addRow(new Object[]{
                book.getBookId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getTotalCopies(),
                book.getAvailableCopies(),
                book.getStatus()
            });
        }

        JTable table = (JTable) dashboardView.getBooksTable();
        table.setModel(model);
    }

    // ── Listeners ───────────────────────────────────────────────────
    class CatalogListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // new BookCatalog().setVisible(true);
            // dashboardView.dispose();
        }
    }

    class LogoutListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            dashboardView.dispose();
            // new Login().setVisible(true);
        }
    }

    class DashboardListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            loadDashboardCards();
            loadBooksTable();
        }
    }
}
