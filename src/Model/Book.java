package Model;

/**
 * Model representing a Book in the Library Management System
 * @author Amanm
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private String genre;
    private int publishedYear;
    private String description;
    private String imagePath;
    private boolean isFuture;

    public Book() {}

    public Book(int id, String title, String author, String genre, int publishedYear, String description, String imagePath, boolean isFuture) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publishedYear = publishedYear;
        this.description = description;
        this.imagePath = imagePath;
        this.isFuture = isFuture;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getPublishedYear() { return publishedYear; }
    public void setPublishedYear(int publishedYear) { this.publishedYear = publishedYear; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public boolean isFuture() { return isFuture; }
    public void setFuture(boolean future) { isFuture = future; }
}
