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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // Launch UserBooksView directly in isolation mode
        java.awt.EventQueue.invokeLater(() -> {
            Model.userdata mockUser = new Model.userdata(1, "Rashmi (Books Isolation)", "rashmi@lms.com", "1234", "user", "active");
            View.UserBooksView ubv = new View.UserBooksView(mockUser);
            Controller.UserBooksController ubc = new Controller.UserBooksController(ubv, mockUser);
            ubc.open();
        });
    }
    
}
