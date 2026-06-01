package Model;

import java.sql.Timestamp;

public class AdminLog {
    private int id;
    private int adminId;
    private String action;
    private Timestamp actionDate;

    // Constructors
    public AdminLog() {
    }

    public AdminLog(int id, int adminId, String action, Timestamp actionDate) {
        this.id = id;
        this.adminId = adminId;
        this.action = action;
        this.actionDate = actionDate;
    }

    public AdminLog(int adminId, String action) {
        this.adminId = adminId;
        this.action = action;
        this.actionDate = new Timestamp(System.currentTimeMillis());
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Timestamp getActionDate() {
        return actionDate;
    }

    public void setActionDate(Timestamp actionDate) {
        this.actionDate = actionDate;
    }
}
