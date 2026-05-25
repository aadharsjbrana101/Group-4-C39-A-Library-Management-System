-- LMS Database Schema
-- Run this script to set up the library database tables

CREATE DATABASE IF NOT EXISTS lms;
USE lms;

-- Create users table
DROP TABLE IF EXISTS borrowed_books;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Create books table
CREATE TABLE books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    genre VARCHAR(100) NOT NULL,
    published_year INT NOT NULL,
    description TEXT,
    image_path VARCHAR(255) NOT NULL,
    is_future BOOLEAN DEFAULT FALSE
);

-- Create borrowed_books table
CREATE TABLE borrowed_books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    borrow_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (book_id) REFERENCES books(id) ON DELETE CASCADE
);

-- Insert a default user
INSERT INTO users (id, username, password, email) VALUES (1, 'Aman', 'password', 'aman@lms.com');

-- Insert future releases books
INSERT INTO books (title, author, genre, published_year, description, image_path, is_future) VALUES 
('The Great Gatsby', 'F. Scott Fitzgerald', 'Classic', 1925, 'The story of the mysteriously wealthy Jay Gatsby and his love for the beautiful Daisy Buchanan.', 'gatsby.png', TRUE),
('Ghostbloods', 'Brandon Sanderson', 'Fantasy', 2025, 'The thrilling continuation of the Cosmere saga featuring Mistborn and Stormlight crossovers.', 'ghostbloods.png', TRUE),
('Diary of a Wimpy Kid', 'Jeff Kinney', 'Comedy', 2007, 'The struggles and diary entries of middle-schooler Greg Heffley as he navigates the trials of school life.', 'wimpy_kid.png', TRUE),
('Percy Jackson', 'Rick Riordan', 'Adventure', 2005, 'Percy Jackson discovers he is a demigod, the son of Poseidon, and embarks on an epic quest to prevent a catastrophic war.', 'percy_jackson.png', TRUE);

-- Insert regular catalog books
INSERT INTO books (title, author, genre, published_year, description, image_path, is_future) VALUES 
('Dune', 'Frank Herbert', 'Sci-Fi', 1965, 'Set in the far future amidst a sprawling feudal interstellar empire, Dune tells the story of Paul Atreides on the desert planet Arrakis.', 'dune.png', FALSE),
('To Kill a Mockingbird', 'Harper Lee', 'Classic', 1960, 'The story of an innocent man accused of a crime and his idealistic lawyer Atticus Finch, as seen through the eyes of Scout.', 'mockingbird.png', FALSE),
('Harry Potter', 'J.K. Rowling', 'Fantasy', 1997, 'A young wizard discovers his magical heritage, makes lifelong friends at Hogwarts, and faces the dark wizard Lord Voldemort.', 'harry_potter.png', FALSE),
('Sherlock Holmes', 'Arthur Conan Doyle', 'Mystery', 1887, 'The adventures of the legendary detective Sherlock Holmes and his loyal companion Dr. John Watson as they solve London\'s finest mysteries.', 'sherlock_holmes.png', FALSE);
