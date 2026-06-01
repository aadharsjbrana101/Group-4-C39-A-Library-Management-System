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
        // Launch UserPaymentsView directly in isolation mode
        java.awt.EventQueue.invokeLater(() -> {
            Model.userdata mockUser = new Model.userdata(1, "Kushal (Payments Isolation)", "kushal@lms.com", "1234", "user", "active");
            View.UserPaymentsView upv = new View.UserPaymentsView(mockUser);
            Controller.UserPaymentsController upc = new Controller.UserPaymentsController(upv, mockUser);
            upc.open();
        });
    }
    
}
