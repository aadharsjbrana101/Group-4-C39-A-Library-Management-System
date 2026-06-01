/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package librarymanagementsystem;

import Model.userdata;
import View.AdminDashboard;
import Controller.AdminDashboardController;

public class LibraryManagementSystem {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        userdata mockUser = new userdata(1, "Ronish (Dashboard Isolation)", "ronish@lms.com", "1234", "admin", "active");
        AdminDashboard view = new AdminDashboard(mockUser);
        AdminDashboardController controller = new AdminDashboardController(view, mockUser);
        controller.open();
    }
    
}
