import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO implements DAO<Transaction> {

    @Override
    public Transaction get(int id) {
        String query = "SELECT * FROM Transactions WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Transaction(
                    rs.getInt("id"),
                    new UserDAO().get(rs.getInt("user_id")), // Assuming UserDAO provides the get method
                    new BookDAO().get(rs.getInt("book_id")), // Assuming BookDAO provides the get method
                    rs.getDate("checkout_date"),
                    rs.getDate("return_date"),
                    rs.getBoolean("completed")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Transaction get(int userId, int bookId) {
        String query = "SELECT * FROM Transactions WHERE user_id = ? AND book_id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            stmt.setInt(2, bookId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Transaction(
                    rs.getInt("id"),
                    new UserDAO().get(rs.getInt("user_id")),
                    new BookDAO().get(rs.getInt("book_id")),
                    rs.getDate("checkout_date"),
                    rs.getDate("return_date"),
                    rs.getBoolean("completed")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public List<Transaction> getAll() {
        String query = "SELECT * FROM Transactions";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(
                    rs.getInt("id"),
                    new UserDAO().get(rs.getInt("user_id")),
                    new BookDAO().get(rs.getInt("book_id")),
                    rs.getDate("checkout_date"),
                    rs.getDate("return_date"),
                    rs.getBoolean("completed")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> getAll(int user_id) {
        String query = "SELECT * FROM Transactions WHERE user_id = ?";
        List<Transaction> transactions = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                transactions.add(new Transaction(
                    rs.getInt("id"),
                    new UserDAO().get(rs.getInt("user_id")),
                    new BookDAO().get(rs.getInt("book_id")),
                    rs.getDate("checkout_date"),
                    rs.getDate("return_date"),
                    rs.getBoolean("completed")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public List<Transaction> getAllByUserId(int user_id) {
        String query = "SELECT * FROM Transactions WHERE user_id = ?";
        List<Transaction> transactions = new ArrayList<>();

            try (Connection conn = DBConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, user_id);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    transactions.add(new Transaction(
                        rs.getInt("id"),
                        new UserDAO().get(rs.getInt("user_id")),
                        new BookDAO().get(rs.getInt("book_id")),
                        rs.getDate("checkout_date"),
                        rs.getDate("return_date"),
                        rs.getBoolean("completed")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return transactions;
    }

    

    public List<Transaction> getAllPending(int user_id) {
        String query = "SELECT * FROM Transactions WHERE completed is false and user_id = ?";
        List<Transaction> transactions = new ArrayList<>();

            try (Connection conn = DBConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(query)) {
                stmt.setInt(1, user_id);
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    transactions.add(new Transaction(
                        rs.getInt("id"),
                        new UserDAO().get(rs.getInt("user_id")),
                        new BookDAO().get(rs.getInt("book_id")),
                        rs.getDate("checkout_date"),
                        rs.getDate("return_date"),
                        rs.getBoolean("completed")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return transactions;
    }

    public List<Transaction> getAllPending() {
        String query = "SELECT * FROM Transactions WHERE completed is false";
        List<Transaction> transactions = new ArrayList<>();

            try (Connection conn = DBConnection.getConnection();
                    PreparedStatement stmt = conn.prepareStatement(query)) {
                ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    transactions.add(new Transaction(
                        rs.getInt("id"),
                        new UserDAO().get(rs.getInt("user_id")),
                        new BookDAO().get(rs.getInt("book_id")),
                        rs.getDate("checkout_date"),
                        rs.getDate("return_date"),
                        rs.getBoolean("completed")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return transactions;
    }

public List<Transaction> getDueTransactions(int userId, int days) {
    String query = "SELECT * FROM Transactions WHERE user_id = ? AND return_date <= NOW() + INTERVAL ? DAY AND completed = false";
    List<Transaction> transactions = new ArrayList<>();

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
        stmt.setInt(1, userId); // Set the user ID
        stmt.setInt(2, days);   // Set the number of days
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            transactions.add(new Transaction(
                rs.getInt("id"),
                new UserDAO().get(rs.getInt("user_id")),
                new BookDAO().get(rs.getInt("book_id")),
                rs.getDate("checkout_date"),
                rs.getDate("return_date"),
                rs.getBoolean("completed")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return transactions;
}






    @Override
    public void save(Transaction transaction) {
        String query = "INSERT INTO Transactions (user_id, book_id, checkout_date, return_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transaction.getUser().getId());
            stmt.setInt(2, transaction.getBook().getId());
            stmt.setDate(3, new java.sql.Date(transaction.getCheckoutDate().getTime()));
            stmt.setDate(4, transaction.getReturnDate() != null ? new java.sql.Date(transaction.getReturnDate().getTime()) : null);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Transaction transaction) {
        String query = "UPDATE Transactions SET user_id = ?, book_id = ?, checkout_date = ?, return_date = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, transaction.getUser().getId());
            stmt.setInt(2, transaction.getBook().getId());
            stmt.setDate(3, new java.sql.Date(transaction.getCheckoutDate().getTime()));
            stmt.setDate(4, transaction.getReturnDate() != null ? new java.sql.Date(transaction.getReturnDate().getTime()) : null);
            stmt.setInt(5, transaction.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM Transactions WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
