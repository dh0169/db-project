import java.util.Date;


public class Transaction {
    private int id;
    private User user;
    private Book book;
    private Date checkoutDate;
    private Date returnDate;
    private Boolean completed;

    public Transaction(int id, User user, Book book, Date checkoutDate, Date returnDate, Boolean completed) {
        this.id = id;
        this.user = user;
        this.book = book;
        this.checkoutDate = checkoutDate;
        this.returnDate = returnDate;
        this.completed = completed;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Date getCheckoutDate() {
        return this.checkoutDate;
    }

    public void setCheckoutDate(Date checkoutDate) {
        this.checkoutDate = checkoutDate;
    }

    public Date getReturnDate() {
        return this.returnDate;
    }

    public Boolean getCompleted() {
        return this.completed;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}