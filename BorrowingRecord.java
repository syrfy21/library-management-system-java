public class BorrowingRecord {
    private User user;
    private Book book;
    private String borrowDate;
    private BorrowingRecord next;
    
    public BorrowingRecord(User user, Book book, String borrowDate) {
        this.user = user;
        this.book = book;
        this.borrowDate = borrowDate;
        this.next = null;
    }
    
    public User getUser() {
        return user;
    }
    
    public Book getBook() {
        return book;
    }
    
    public String getBorrowDate() {
        return borrowDate;
    }
    
    public BorrowingRecord getNext() {
        return next;
    }
    
    public void setNext(BorrowingRecord next) {
        this.next = next;
    }
    
    public String toString() {
        return user.getName() + " borrowed '" + book.getTitle() + "' on " + borrowDate;
    }
}
