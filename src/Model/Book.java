package Model;

public class Book {
    // Sprint 1 (Ronish): Add inventory control states
    // Sprint 2: Book search properties
    private int id;
    private String title;
    private String author;
    private String genre;
    private int year;
    private String imagePath;
    private boolean isFutureRelease;
    private String isbn;
    private String description;
    private int quantity;
    private int availableQuantity;

    // Constructors
    public Book() {
        this.isbn = "N/A";
        this.description = "";
        this.quantity = 5;
        this.availableQuantity = 5;
    }

    public Book(int id, String title, String author, String genre, int year, String imagePath, boolean isFutureRelease) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.imagePath = imagePath;
        this.isFutureRelease = isFutureRelease;
        this.isbn = "N/A";
        this.description = "A wonderful book on " + genre + ".";
        this.quantity = 5;
        this.availableQuantity = 5;
    }

    public Book(int id, String title, String author, String genre, int year, String imagePath, boolean isFutureRelease,
                String isbn, String description, int quantity, int availableQuantity) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.imagePath = imagePath;
        this.isFutureRelease = isFutureRelease;
        this.isbn = isbn;
        this.description = description;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
    }

    public Book(String title, String author, String genre, int year, String imagePath, boolean isFutureRelease) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.imagePath = imagePath;
        this.isFutureRelease = isFutureRelease;
        this.isbn = "N/A";
        this.description = "A wonderful book on " + genre + ".";
        this.quantity = 5;
        this.availableQuantity = 5;
    }

    public Book(String title, String author, String genre, int year, String imagePath, boolean isFutureRelease,
                String isbn, String description, int quantity, int availableQuantity) {
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.year = year;
        this.imagePath = imagePath;
        this.isFutureRelease = isFutureRelease;
        this.isbn = isbn;
        this.description = description;
        this.quantity = quantity;
        this.availableQuantity = availableQuantity;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isFutureRelease() {
        return isFutureRelease;
    }

    public void setFutureRelease(boolean futureRelease) {
        isFutureRelease = futureRelease;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(int availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}
