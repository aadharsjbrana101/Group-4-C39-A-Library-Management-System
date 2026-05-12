package Controller;

import Model.Book;
import Model.userdata;
import View.AdminCatalogView;
import View.UserLogin;
import dao.AdminLogDao;
import dao.BookDao;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class AdminCatalogController {
    private final AdminCatalogView view;
    private final userdata currentAdmin;
    private final BookDao bookDao = new BookDao();
    private final AdminLogDao adminLogDao = new AdminLogDao();
    private int selectedBookId = -1;
    private List<Book> allBooksList = new ArrayList<>();

    public AdminCatalogController(AdminCatalogView view, userdata admin) {
        this.view = view;
        this.currentAdmin = admin;

        // Button Actions
        this.view.getBtnSave().addActionListener(new SaveBookListener());
        this.view.getBtnReset().addActionListener(new ResetFormListener());
        this.view.getBtnDelete().addActionListener(new DeleteBookListener());
        
        // JTable Row Selection
        this.view.getTblBooks().getSelectionModel().addListSelectionListener(new BookTableSelectionListener());

        // Sidebar Navigation
        this.view.getBtnDashboard().addActionListener(new DashboardNavListener());
        this.view.getBtnCatalog().addActionListener(e -> { /* Already here */ });
        this.view.getBtnUsers().addActionListener(new UsersNavListener());
        this.view.getBtnPayments().addActionListener(new PaymentsNavListener());
        this.view.getBtnLogout().addActionListener(new LogoutListener());
    }

    public void open() {
        this.view.setVisible(true);
        loadBooks();
        resetForm();
    }

    private void loadBooks() {
        try {
            // Load both normal and future releases to manage them
            List<Book> normal = bookDao.getCatalogBooks("", "All Genres");
            List<Book> future = bookDao.getFutureReleases();
            
            allBooksList = new ArrayList<>();
            allBooksList.addAll(normal);
            allBooksList.addAll(future);

            DefaultTableModel model = (DefaultTableModel) view.getTblBooks().getModel();
            model.setRowCount(0);

            for (Book b : allBooksList) {
                model.addRow(new Object[]{
                    b.getId(),
                    b.getTitle(),
                    b.getAuthor(),
                    b.getGenre(),
                    b.getYear(),
                    b.getQuantity(),
                    b.getAvailableQuantity(),
                    b.getIsbn()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "Error loading books: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void resetForm() {
        view.getTxtBookTitle().setText("");
        view.getTxtAuthor().setText("");
        view.getCbGenre().setSelectedIndex(0);
        view.getTxtYear().setText("");
        view.getTxtQuantity().setText("5");
        view.getTxtIsbn().setText("");
        view.getTxtImagePath().setText("dune.png");
        view.getTxtDescription().setText("");
        view.getChkFuture().setSelected(false);
        
        selectedBookId = -1;
        view.getBtnSave().setText("Save Book");
        view.getTblBooks().clearSelection();
    }

    // Save/Update Book button
    class SaveBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String title = view.getTxtBookTitle().getText().trim();
            String author = view.getTxtAuthor().getText().trim();
            String genre = view.getCbGenre().getSelectedItem().toString();
            String yearStr = view.getTxtYear().getText().trim();
            String qtyStr = view.getTxtQuantity().getText().trim();
            String isbn = view.getTxtIsbn().getText().trim();
            String imagePath = view.getTxtImagePath().getText().trim();
            String desc = view.getTxtDescription().getText().trim();
            boolean isFuture = view.getChkFuture().isSelected();

            if (title.isEmpty() || author.isEmpty() || yearStr.isEmpty() || qtyStr.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Please fill in all required fields (Title, Author, Year, Copies, ISBN).");
                return;
            }

            int year;
            int quantity;
            try {
                year = Integer.parseInt(yearStr);
                quantity = Integer.parseInt(qtyStr);
                if (year <= 0 || quantity < 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(view, "Please enter valid positive integers for Year and Copies.", "Invalid Data", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (imagePath.isEmpty()) {
                imagePath = "dune.png";
            }
            if (desc.isEmpty()) {
                desc = "A book on " + genre + ".";
            }

            try {
                if (selectedBookId == -1) {
                    // Create New Book
                    Book newBook = new Book(0, title, author, genre, year, imagePath, isFuture, isbn, desc, quantity, quantity);
                    boolean success = bookDao.addBook(newBook);
                    if (success) {
                        adminLogDao.logAction(currentAdmin.getId(), "Added book: " + title);
                        JOptionPane.showMessageDialog(view, "Book added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadBooks();
                        resetForm();
                    } else {
                        JOptionPane.showMessageDialog(view, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Update Existing Book
                    Book oldBook = bookDao.getBookById(selectedBookId);
                    if (oldBook == null) {
                        JOptionPane.showMessageDialog(view, "Selected book no longer exists.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Keep available copies sync: availQty = newQty - (oldQty - oldAvailQty)
                    int borrowedCopies = oldBook.getQuantity() - oldBook.getAvailableQuantity();
                    int availQty = Math.max(0, quantity - borrowedCopies);

                    Book updatedBook = new Book(selectedBookId, title, author, genre, year, imagePath, isFuture, isbn, desc, quantity, availQty);
                    boolean success = bookDao.updateBook(updatedBook);
                    if (success) {
                        adminLogDao.logAction(currentAdmin.getId(), "Updated book ID " + selectedBookId + " (" + title + ")");
                        JOptionPane.showMessageDialog(view, "Book details updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadBooks();
                        resetForm();
                    } else {
                        JOptionPane.showMessageDialog(view, "Failed to update book.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }

    // Delete Book button
    class DeleteBookListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (selectedBookId == -1) {
                JOptionPane.showMessageDialog(view, "Please select a book from the table to delete.", "Select Book", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(view, "Are you sure you want to permanently delete this book?", "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    // Check if book has active borrows before deleting
                    Book oldBook = bookDao.getBookById(selectedBookId);
                    if (oldBook != null && oldBook.getAvailableQuantity() < oldBook.getQuantity()) {
                        JOptionPane.showMessageDialog(view, "Cannot delete! Some copies of this book are currently borrowed.", "Book In Use", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean success = bookDao.deleteBook(selectedBookId);
                    if (success) {
                        adminLogDao.logAction(currentAdmin.getId(), "Deleted book ID " + selectedBookId);
                        JOptionPane.showMessageDialog(view, "Book deleted successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                        loadBooks();
                        resetForm();
                    } else {
                        JOptionPane.showMessageDialog(view, "Failed to delete book.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "Database error: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }
    }

    // Reset button
    class ResetFormListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            resetForm();
        }
    }

    // Selection listener on JTable to fill form for edit
    class BookTableSelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = view.getTblBooks().getSelectedRow();
                if (selectedRow >= 0) {
                    int bookId = (int) view.getTblBooks().getValueAt(selectedRow, 0);
                    // Find matching book in local list
                    Book selectedBook = null;
                    for (Book b : allBooksList) {
                        if (b.getId() == bookId) {
                            selectedBook = b;
                            break;
                        }
                    }

                    if (selectedBook != null) {
                        selectedBookId = selectedBook.getId();
                        view.getTxtBookTitle().setText(selectedBook.getTitle());
                        view.getTxtAuthor().setText(selectedBook.getAuthor());
                        view.getCbGenre().setSelectedItem(selectedBook.getGenre());
                        view.getTxtYear().setText(String.valueOf(selectedBook.getYear()));
                        view.getTxtQuantity().setText(String.valueOf(selectedBook.getQuantity()));
                        view.getTxtIsbn().setText(selectedBook.getIsbn());
                        view.getTxtImagePath().setText(selectedBook.getImagePath());
                        view.getTxtDescription().setText(selectedBook.getDescription());
                        view.getChkFuture().setSelected(selectedBook.isFutureRelease());
                        
                        view.getBtnSave().setText("Update Book");
                    }
                }
            }
        }
    }

    // Navigation Listeners
    class DashboardNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.AdminDashboard ad = new View.AdminDashboard(currentAdmin);
            AdminDashboardController adc = new AdminDashboardController(ad, currentAdmin);
            adc.open();
            view.dispose();
        }
    }

    class UsersNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.AdminUsersView auv = new View.AdminUsersView(currentAdmin);
            AdminUsersController auc = new AdminUsersController(auv, currentAdmin);
            auc.open();
            view.dispose();
        }
    }

    class PaymentsNavListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            View.AdminPaymentsView apv = new View.AdminPaymentsView(currentAdmin);
            AdminPaymentsController apc = new AdminPaymentsController(apv, currentAdmin);
            apc.open();
            view.dispose();
        }
    }

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
