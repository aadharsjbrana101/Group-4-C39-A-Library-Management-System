/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

public class FineModel {

    private int fineId;
    private int userId;
    private String bookTitle;
    private String fineType;
    private double amount;
    private String status;
    private String dueDate;

    // Constructor
    public FineModel(int fineId, int userId, String bookTitle, String fineType,
                     double amount, String status, String dueDate) {
        this.fineId = fineId;
        this.userId = userId;
        this.bookTitle = bookTitle;
        this.fineType = fineType;
        this.amount = amount;
        this.status = status;
        this.dueDate = dueDate;
    }

    // Default constructor
    public FineModel() {}

    // Getters and Setters
    public int getFineId() {
        return fineId;
    }
    public void setFineId(int fineId) {
        this.fineId = fineId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getBookTitle() {
        return bookTitle;
    }
    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getFineType() {
        return fineType;
    }
    public void setFineType(String fineType) {
        this.fineType = fineType;
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

    public String getDueDate() {
        return dueDate;
    }
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }
}
