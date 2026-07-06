import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Stack;

public class Library {
    private ArrayList<Book> inventory;
    private ArrayList<User> users;
    private BorrowingRecord borrowingHistoryHead;
    private Queue<RequestNode> requestQueue;
    private Stack<Book> returnStack;
    
    private class RequestNode {
        User user;
        Book book;
        
        RequestNode(User user, Book book) {
            this.user = user;
            this.book = book;
        }
    }
    
    public Library() {
        inventory = new ArrayList<Book>();
        users = new ArrayList<User>();
        borrowingHistoryHead = null;
        requestQueue = new LinkedList<RequestNode>();
        returnStack = new Stack<Book>();
    }
    
    public void addBook(Book book) {
        boolean exists = false;
        for (Book b : inventory) {
            if (b.getIsbn().equals(book.getIsbn())) {
                exists = true;
                break;
            }
        }
        
        if (!exists) {
            inventory.add(book);
            System.out.println("Book added: " + book.getTitle());
        } else {
            System.out.println("Book already exists in inventory.");
        }
    }
    
    public boolean removeBook(String isbn) {
        Book bookToRemove = findBookByIsbn(isbn);
        if (bookToRemove != null) {
            inventory.remove(bookToRemove);
            System.out.println("Book removed: " + bookToRemove.getTitle());
            return true;
        }
        System.out.println("Book not found.");
        return false;
    }
    
    public Book findBookByIsbn(String isbn) {
        for (Book book : inventory) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }
        return null;
    }
    
    public ArrayList<Book> searchByTitle(String title) {
        ArrayList<Book> results = new ArrayList<Book>();
        for (Book book : inventory) {
            if (book.getTitle().toLowerCase().contains(title.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }
   
    public ArrayList<Book> searchByAuthor(String author) {
        ArrayList<Book> results = new ArrayList<Book>();
        for (Book book : inventory) {
            if (book.getAuthor().toLowerCase().contains(author.toLowerCase())) {
                results.add(book);
            }
        }
        return results;
    }
    
    public User registerUser(String name, String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                System.out.println("User ID already exists.");
                return null;
            }
        }
        
        User newUser = new User(name, userId);
        users.add(newUser);
        System.out.println("User registered: " + name);
        return newUser;
    }
    
    public User findUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
    
    public boolean borrowBook(User user, String isbn) {
        Book book = findBookByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found.");
            return false;
        }
        
        if (user.hasBook(book)) {
            System.out.println("You already have this book checked out.");
            return false;
        }
        
        if (book.isAvailable()) {
            book.setAvailable(false);
            user.borrowBook(book);
            
            String date = java.time.LocalDate.now().toString();
            BorrowingRecord newRecord = new BorrowingRecord(user, book, date);
            newRecord.setNext(borrowingHistoryHead);
            borrowingHistoryHead = newRecord;
            
            System.out.println("Book borrowed: " + book.getTitle());
            System.out.println("Borrowing record added to history.");
            return true;
        } else {
            System.out.println("Book is checked out. Adding to request queue.");
            RequestNode newNode = new RequestNode(user, book);
            requestQueue.offer(newNode);
            System.out.println("You are position #" + getQueuePosition(user, book) + " in the queue.");
            System.out.println("You will be notified when the book becomes available.");
            return false;
        }
    }
    
    public boolean returnBook(User user, String isbn) {
        Book book = findBookByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found.");
            return false;
        }
        
        if (!user.hasBook(book)) {
            System.out.println("User does not have this book checked out.");
            return false;
        }
        
        user.returnBook(book);
        book.setAvailable(true);
        pushReturnStack(book);
        System.out.println("Book returned: " + book.getTitle());
        processRequestQueue(book);
        return true;
    }
    
    private void processRequestQueue(Book book) {
        RequestNode nodeToProcess = null;
        
        Iterator<RequestNode> iterator = requestQueue.iterator();
        while (iterator.hasNext()) {
            RequestNode node = iterator.next();
            if (node.book.equals(book)) {
                nodeToProcess = node;
                iterator.remove();
                break;
            }
        }
        
        if (nodeToProcess != null) {
            book.setAvailable(false);
            nodeToProcess.user.borrowBook(book);
            
            String date = java.time.LocalDate.now().toString();
            BorrowingRecord newRecord = new BorrowingRecord(nodeToProcess.user, book, date);
            newRecord.setNext(borrowingHistoryHead);
            borrowingHistoryHead = newRecord;
            
            System.out.println("\n*** NOTIFICATION ***");
            System.out.println("Book '" + book.getTitle() + "' is now available!");
            System.out.println("Automatically borrowed by: " + nodeToProcess.user.getName() + 
                             " (ID: " + nodeToProcess.user.getUserId() + ")");
            System.out.println("You were next in the queue.");
        }
    }
    
    private int getQueuePosition(User user, Book book) {
        int position = 0;
        for (RequestNode node : requestQueue) {
            if (node.book.equals(book)) {
                position++;
                if (node.user.equals(user)) {
                    return position;
                }
            }
        }
        return position + 1;
    }
    
    public ArrayList<User> getBookQueue(String isbn) {
        ArrayList<User> queue = new ArrayList<User>();
        Book book = findBookByIsbn(isbn);
        if (book == null) {
            return queue;
        }
        
        for (RequestNode node : requestQueue) {
            if (node.book.equals(book)) {
                queue.add(node.user);
            }
        }
        return queue;
    }
    
    public void displayBookQueue(String isbn) {
        ArrayList<User> queue = getBookQueue(isbn);
        Book book = findBookByIsbn(isbn);
        
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        
        System.out.println("\nQueue for: " + book.getTitle());
        if (queue.isEmpty()) {
            System.out.println("No users in queue.");
        } else {
            for (int i = 0; i < queue.size(); i++) {
                System.out.println((i + 1) + ". " + queue.get(i).getName() + 
                                 " (ID: " + queue.get(i).getUserId() + ")");
            }
        }
    }
    
    public void pushReturnStack(Book book) {
        returnStack.push(book);
    }
    
    public Book popReturnStack() {
        if (returnStack.isEmpty()) {
            System.out.println("Return stack is empty.");
            return null;
        }
        Book book = returnStack.pop();
        System.out.println("Popped from stack: " + book.getTitle());
        return book;
    }
    
    public Book peekReturnStack() {
        if (returnStack.isEmpty()) {
            return null;
        }
        return returnStack.peek();
    }
    
    public int getReturnStackSize() {
        return returnStack.size();
    }
    
    public boolean isReturnStackEmpty() {
        return returnStack.isEmpty();
    }
    
    public ArrayList<Book> getBookRecommendations(int count) {
        ArrayList<Book> recommendations = new ArrayList<Book>();
        if (returnStack.isEmpty()) {
            return recommendations;
        }
        
        int collected = 0;
        for (int i = returnStack.size() - 1; i >= 0 && collected < count; i--) {
            Book book = returnStack.get(i);
            if (book.isAvailable()) {
                recommendations.add(book);
                collected++;
            }
        }
        
        return recommendations;
    }
    
    public void displayRecentReturns(int count) {
        System.out.println("\nRecently Returned Books (Last " + count + "):");
        if (returnStack.isEmpty()) {
            System.out.println("No books returned yet.");
            return;
        }
        
        int displayCount = Math.min(count, returnStack.size());
        for (int i = returnStack.size() - 1; i >= returnStack.size() - displayCount; i--) {
            System.out.println(returnStack.get(i));
        }
    }
    
    public void displayBookRecommendations(int count) {
        System.out.println("\nBook Recommendations (Based on Recent Returns):");
        ArrayList<Book> recommendations = getBookRecommendations(count);
        if (recommendations.isEmpty()) {
            System.out.println("No recommendations available at this time.");
            return;
        }
        
        for (int i = 0; i < recommendations.size(); i++) {
            System.out.println((i + 1) + ". " + recommendations.get(i));
        }
    }
    
    public void displayAllBooks() {
        System.out.println("\nAll Books in Inventory:");
        if (inventory.isEmpty()) {
            System.out.println("No books in inventory.");
            return;
        }
        for (int i = 0; i < inventory.size(); i++) {
            System.out.println((i + 1) + ". " + inventory.get(i));
        }
    }
    
    public void displayAvailableBooks() {
        System.out.println("\nAvailable Books:");
        boolean found = false;
        for (Book book : inventory) {
            if (book.isAvailable()) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books available.");
        }
    }
    
    public void displayBooksOnLoan() {
        System.out.println("\nBooks Currently on Loan:");
        boolean found = false;
        for (Book book : inventory) {
            if (!book.isAvailable()) {
                System.out.println(book);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No books on loan.");
        }
    }
    
    public void displayRequestedBooks() {
        System.out.println("\nBooks with Pending Requests:");
        if (requestQueue.isEmpty()) {
            System.out.println("No pending requests.");
            return;
        }
        
        ArrayList<Book> requestedBooks = new ArrayList<Book>();
        for (RequestNode node : requestQueue) {
            if (!requestedBooks.contains(node.book)) {
                requestedBooks.add(node.book);
            }
        }
        
        for (Book book : requestedBooks) {
            int queueSize = getBookQueue(book.getIsbn()).size();
            System.out.println(book.getTitle() + " - " + queueSize + " user(s) in queue");
        }
    }
    
    public ArrayList<BorrowingRecord> getUserBorrowingHistory(User user) {
        ArrayList<BorrowingRecord> userHistory = new ArrayList<BorrowingRecord>();
        
        BorrowingRecord current = borrowingHistoryHead;
        while (current != null) {
            if (current.getUser().equals(user)) {
                userHistory.add(current);
            }
            current = current.getNext();
        }
        
        return userHistory;
    }
    
    public ArrayList<BorrowingRecord> getBookBorrowingHistory(Book book) {
        ArrayList<BorrowingRecord> bookHistory = new ArrayList<BorrowingRecord>();
        
        BorrowingRecord current = borrowingHistoryHead;
        while (current != null) {
            if (current.getBook().equals(book)) {
                bookHistory.add(current);
            }
            current = current.getNext();
        }
        
        return bookHistory;
    }
    
    public int getTotalBorrowingRecords() {
        int count = 0;
        
        BorrowingRecord current = borrowingHistoryHead;
        while (current != null) {
            count++;
            current = current.getNext();
        }
        
        return count;
    }
    
    public void displayBorrowingHistory() {
        System.out.println("\nBorrowing History (All Records):");
        if (borrowingHistoryHead == null) {
            System.out.println("No borrowing records.");
            return;
        }
        
        BorrowingRecord current = borrowingHistoryHead;
        int count = 1;
        while (current != null) {
            System.out.println(count + ". " + current);
            current = current.getNext();
            count++;
        }
        System.out.println("Total records: " + getTotalBorrowingRecords());
    }
    
    public void displayUserBorrowingHistory(String userId) {
        User user = findUserById(userId);
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        ArrayList<BorrowingRecord> history = getUserBorrowingHistory(user);
        System.out.println("\nBorrowing History for: " + user.getName() + " (ID: " + userId + ")");
        
        if (history.isEmpty()) {
            System.out.println("No borrowing records found.");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ". " + history.get(i));
            }
            System.out.println("Total records: " + history.size());
        }
    }
    
    public void displayBookBorrowingHistory(String isbn) {
        Book book = findBookByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }
        
        ArrayList<BorrowingRecord> history = getBookBorrowingHistory(book);
        System.out.println("\nBorrowing History for: " + book.getTitle());
        
        if (history.isEmpty()) {
            System.out.println("No borrowing records found.");
        } else {
            for (int i = 0; i < history.size(); i++) {
                System.out.println((i + 1) + ". " + history.get(i));
            }
            System.out.println("Total times borrowed: " + history.size());
        }
    }
    
    public void displayAllUsers() {
        System.out.println("\nRegistered Users:");
        if (users.isEmpty()) {
            System.out.println("No users registered.");
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            System.out.println((i + 1) + ". " + users.get(i));
        }
    }
}
