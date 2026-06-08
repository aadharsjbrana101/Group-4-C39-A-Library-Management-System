package librarymanagementsystem;

import Controller.LoginController;
import View.UserLogin;
import dao.UserDao;
// import dao.BookDao;
// import dao.BorrowDao;
// import dao.FineDao;
// import dao.AdminLogDao;

    public static void main(String[] args) {
        // Set beautiful Swing Look and Feel
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(LibraryManagementSystem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        // Launch CatalogView directly in isolation mode
        java.awt.EventQueue.invokeLater(() -> {
            View.CatalogView cv = new View.CatalogView();
            Model.userdata testUser = new Model.userdata("Aman (Catalog Isolation)", "aman@lms.com", "user");
            Controller.CatalogController cc = new Controller.CatalogController(cv, testUser);
            cc.open();
        });
    }
}