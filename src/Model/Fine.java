package Model;

import java.sql.Date;

public class Fine {
    // Sprint 2: Fine transaction status options
    // Sprint 3: Fine reminder properties
    // Sprint 4: Overdue alert configuration properties
    private int id;
    private int borrowId;
    private int userId;
    private double amount;
    private String status; // "unpaid", "paid"
    private Date paymentDate;

    // Constructors
    public Fine() {
    }

    public Fine(int id, int borrowId, int userId, double amount, String status, Date paymentDate) {
        this.id = id;
        this.borrowId = borrowId;
        this.userId = userId;
        this.amount = amount;
        this.status = status;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(int borrowId) {
        this.borrowId = borrowId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }
}
