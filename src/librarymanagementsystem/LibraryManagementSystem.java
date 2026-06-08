package librarymanagementsystem;

import Model.userdata;
import View.AdminDashboard;
import Controller.AdminDashboardController;

public class LibraryManagementSystem {

    public static void main(String[] args) {
        userdata mockUser = new userdata(1, "Ronish (Dashboard Isolation)", "ronish@lms.com", "1234", "admin", "active");
        AdminDashboard view = new AdminDashboard(mockUser);
        AdminDashboardController controller = new AdminDashboardController(view, mockUser);
        controller.open();
    }
}