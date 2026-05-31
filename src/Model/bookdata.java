package Model;

public class bookdata {
    private int bookId;
    private String title;
    private String author;
    private String genre;
    private int totalCopies;
    private int availableCopies;
    private String status; // "Available", "Issued", "Lost"

    // Constructor
    public bookdata(int bookId, String title, String author,
                    String genre, int totalCopies, int availableCopies, String status) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.status = status;
    }

    // Empty constructor (for inserts)
    public bookdata() {}

    // Getters and Setters
    public int getBookId() {
        return bookId;
    }
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }
    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }
    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getTotalCopies() {
        return totalCopies;
    }
    public void setTotalCopies(int totalCopies) {
        this.totalCopies = totalCopies;
    }

    public int getAvailableCopies() {
        return availableCopies;
    }
    public void setAvailableCopies(int availableCopies) {
        this.availableCopies = availableCopies;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}