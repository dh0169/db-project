# 🌴 Oasis: Library Inventory System

## Project Overview

**Oasis** is a 3-tier web application designed for medium to small-sized libraries. The system provides an easy-to-use interface for library patrons to browse and interact with the library's catalog independently, without requiring direct assistance from librarians. The intended use case includes deployment on a kiosk within the library for patrons to access book information, check out, and return books digitally.

### Key Features
- **User Login**: Patrons can log in to their library accounts to access personalized features.
- **Book Search**: Browse available books by title, author, or genre.
- **Book Checkout/Return**: Check out books or return them using the kiosk system.
- **View Checked-Out Books**: Patrons can see a list of their currently checked-out books and due dates.

---

## Technology Stack

- **Frontend**:
  - **JSP (JavaServer Pages)**: Dynamic content rendering.
  - **HTML/CSS**: Structure and styling.
  - **JavaScript**: Client-side interactions.

- **Backend**:
  - **Java Servlets**: Handle business logic and request/response processing.
  - **Apache Tomcat**: Serves as the application server.

- **Database**:
  - **MySQL**: Stores all system data, including books, users, and transaction records.
  - **JDBC**: Provides a bridge between the application and the database.

---

## Database Schema

The database consists of the following core tables:

1. **Users**:
   - Fields: `UserID`, `Name`, `Email`, `Password`, `AccountCreatedDate`
   - Description: Stores user account details.

2. **Books**:
   - Fields: `BookID`, `Title`, `Author`, `Genre`, `AvailableCopies`, `TotalCopies`
   - Description: Stores details about books in the library.

3. **Transactions**:
   - Fields: `TransactionID`, `UserID`, `BookID`, `CheckoutDate`, `ReturnDate`
   - Description: Tracks checkout and return history for each user.

### Relationships:
- **Users → Transactions**: One-to-many (a user can have multiple transactions).
- **Books → Transactions**: One-to-many (a book can appear in multiple transactions).

---

## Functional Requirements

### User Description:
- **Patrons**:
  - Access the kiosk to log in, browse the catalog, and manage their borrowed books.

### Functional Processes:
1. **Login**:
   - **Input**: User email and password.
   - **Process**: Authenticate user via database query.
   - **Output**: Redirect to user dashboard or display an error message.

2. **Browse/Search Books**:
   - **Input**: Search keywords (title, author, or genre).
   - **Process**: Query database for matching books.
   - **Output**: Display search results with book details.

3. **Checkout Book**:
   - **Input**: BookID.
   - **Process**: Validate availability, update the `Transactions` and `Books` tables.
   - **Output**: Confirmation of successful checkout.

4. **Return Book**:
   - **Input**: BookID.
   - **Process**: Update `Transactions` table and increment available copies in the `Books` table.
   - **Output**: Confirmation of successful return.

5. **View Checked-Out Books**:
   - **Input**: UserID.
   - **Process**: Query `Transactions` for active checkouts.
   - **Output**: List of checked-out books with due dates.

---

## Non-Functional Requirements

- **Performance**: The system should handle up to 20 concurrent users with minimal latency.
- **Scalability**: Designed to scale with additional libraries or larger inventories.
- **Accessibility**: User-friendly interface accessible from kiosks, supporting touchscreen input.
- **Security**: Implements secure user authentication (passwords are hashed and stored).

---

## Installation and Setup

### Prerequisites
1. [Java JDK](https://www.oracle.com/java/technologies/javase-downloads.html) (version 8 or later)
2. [Apache Tomcat](https://tomcat.apache.org/) (version 9 or later)
3. [MySQL](https://www.mysql.com/) (version 5.7 or later)
4. IDE (e.g., IntelliJ IDEA, Eclipse)

