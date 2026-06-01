/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package librarymanagementsystem;

/**
 *
 * @author aadha
 */
public class LibraryManagementSystem {

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
