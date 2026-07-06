import java.util.ArrayList;

public class User {
    private String name;
    private String userId;
    private ArrayList<Book> borrowedBooks;
    
    public User(String name, String userId) {
        this.name = name;
        this.userId = userId;
        this.borrowedBooks = new ArrayList<Book>();
    }
    
    public String getName() {
        return name;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public ArrayList<Book> getBorrowedBooks() {
        return borrowedBooks;
    }
    
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }
    
    public boolean returnBook(Book book) {
        return borrowedBooks.remove(book);
    }
    
    public boolean hasBook(Book book) {
        return borrowedBooks.contains(book);
    }
    
    public String toString() {
        return name + " (ID: " + userId + ") - " + borrowedBooks.size() + " book(s) borrowed";
    }
    
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof User)) {
            return false;
        }   
        User other = (User) obj;    
        return this.userId.equals(other.userId);
    }               
}
