CREATE DATABASE oasisdb;

USE oasisdb;

-- Users Table
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    phone VARCHAR(15),
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    is_admin BOOLEAN DEFAULT FALSE
);

-- Books Table
CREATE TABLE Books (
    id INT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    ISBN VARCHAR(13) UNIQUE NOT NULL,
    qty INT NOT NULL
);

-- Transactions Table
CREATE TABLE Transactions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    book_id INT NOT NULL,
    checkout_date DATE NOT NULL,
    return_date DATE,
    completed BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES Users(id),
    FOREIGN KEY (book_id) REFERENCES Books(id)
);

-- Create a new database user
CREATE USER 'oasis'@'localhost' IDENTIFIED BY 'Password1!';

-- Grant all privileges on the oasisdb database to the new user
GRANT ALL PRIVILEGES ON oasisdb.* TO 'oasis'@'localhost';

-- Apply the privilege changes
FLUSH PRIVILEGES;