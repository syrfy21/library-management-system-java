import java.util.ArrayList;
import java.util.Scanner;

public class App {
    private static Library library;
    private static Scanner scanner;
    
    public static void main(String[] args) {
        library = new Library();
        scanner = new Scanner(System.in);
        
        initializeSampleData();
        
        System.out.println("========================================");
        System.out.println("   Library Management System");
        System.out.println("========================================");
        System.out.println();
        
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    manageBooks();
                    break;
                case 2:
                    manageUsers();
                    break;
                case 3:
                    borrowBook();
                    break;
                case 4:
                    returnBook();
                    break;
                case 5:
                    searchBooks();
                    break;
                case 6:
                    displayInformation();
                    break;
                case 7:
                    manageRequestQueue();
                    break;
                case 8:
                    manageReturnStack();
                    break;
                case 9:
                    System.out.println("\nThank you for using the Library Management System!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        
        scanner.close();
    }
    
    private static void initializeSampleData() {
        library.addBook(new Book("The Great Gatsby", "F. Scott Fitzgerald", "978-0-7432-7356-5"));
        library.addBook(new Book("To Kill a Mockingbird", "Harper Lee", "978-0-06-112008-4"));
        library.addBook(new Book("1984", "George Orwell", "978-0-452-28423-4"));
        library.addBook(new Book("Pride and Prejudice", "Jane Austen", "978-0-14-143951-8"));
        library.addBook(new Book("The Catcher in the Rye", "J.D. Salinger", "978-0-316-76948-0"));
        
        library.registerUser("John Doe", "U001");
        library.registerUser("Jane Smith", "U002");
        library.registerUser("Bob Johnson", "U003");
    }
    
    private static void displayMainMenu() {
        System.out.println();
        System.out.println("========== MAIN MENU ==========");
        System.out.println("1. Manage Books");
        System.out.println("2. Manage Users");
        System.out.println("3. Borrow a Book");
        System.out.println("4. Return a Book");
        System.out.println("5. Search Books");
        System.out.println("6. Display Information");
        System.out.println("7. Manage Request Queue");
        System.out.println("8. Manage Return Stack");
        System.out.println("9. Exit");
        System.out.println("===============================");
    }
    
    private static void manageBooks() {
        System.out.println();
        System.out.println("=== Book Management ===");
        System.out.println("1. Add a Book");
        System.out.println("2. Remove a Book");
        System.out.println("3. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                System.out.print("Enter book title: ");
                String title = scanner.nextLine();
                System.out.print("Enter author: ");
                String author = scanner.nextLine();
                System.out.print("Enter ISBN: ");
                String isbn = scanner.nextLine();
                library.addBook(new Book(title, author, isbn));
                break;
            case 2:
                System.out.print("Enter ISBN of book to remove: ");
                String isbnToRemove = scanner.nextLine();
                library.removeBook(isbnToRemove);
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private static void manageUsers() {
        System.out.println();
        System.out.println("=== User Management ===");
        System.out.println("1. Register New User");
        System.out.println("2. View All Users");
        System.out.println("3. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                System.out.print("Enter user name: ");
                String name = scanner.nextLine();
                System.out.print("Enter user ID: ");
                String userId = scanner.nextLine();
                library.registerUser(name, userId);
                break;
            case 2:
                library.displayAllUsers();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private static void borrowBook() {
        System.out.println();
        System.out.println("=== Borrow a Book ===");
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        User user = library.findUserById(userId);
        
        if (user == null) {
            System.out.println("User not found. Please register first.");
            return;
        }
        
        System.out.print("Enter ISBN of book to borrow: ");
        String isbn = scanner.nextLine();
        library.borrowBook(user, isbn);
    }
    
    private static void returnBook() {
        System.out.println();
        System.out.println("=== Return a Book ===");
        System.out.print("Enter user ID: ");
        String userId = scanner.nextLine();
        User user = library.findUserById(userId);
        
        if (user == null) {
            System.out.println("User not found.");
            return;
        }
        
        System.out.print("Enter ISBN of book to return: ");
        String isbn = scanner.nextLine();
        library.returnBook(user, isbn);
    }
    
    private static void searchBooks() {
        System.out.println();
        System.out.println("=== Search Books ===");
        System.out.println("1. Search by Title");
        System.out.println("2. Search by Author");
        System.out.println("3. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                System.out.print("Enter title to search: ");
                String title = scanner.nextLine();
                ArrayList<Book> titleResults = library.searchByTitle(title);
                displaySearchResults(titleResults, "Title");
                break;
            case 2:
                System.out.print("Enter author to search: ");
                String author = scanner.nextLine();
                ArrayList<Book> authorResults = library.searchByAuthor(author);
                displaySearchResults(authorResults, "Author");
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private static void displaySearchResults(ArrayList<Book> results, String searchType) {
        System.out.println();
        System.out.println("=== Search Results (" + searchType + ") ===");
        if (results.isEmpty()) {
            System.out.println("No books found.");
        } else {
            for (int i = 0; i < results.size(); i++) {
                System.out.println((i + 1) + ". " + results.get(i));
            }
        }
    }
    
    private static void displayInformation() {
        System.out.println();
        System.out.println("=== Display Information ===");
        System.out.println("1. Display All Books");
        System.out.println("2. Display Available Books");
        System.out.println("3. Display Books on Loan");
        System.out.println("4. Display Requested Books");
        System.out.println("5. Display All Borrowing History");
        System.out.println("6. Display User's Borrowing History");
        System.out.println("7. Display Book's Borrowing History");
        System.out.println("8. Display All Users");
        System.out.println("9. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                library.displayAllBooks();
                break;
            case 2:
                library.displayAvailableBooks();
                break;
            case 3:
                library.displayBooksOnLoan();
                break;
            case 4:
                library.displayRequestedBooks();
                break;
            case 5:
                library.displayBorrowingHistory();
                break;
            case 6:
                System.out.print("Enter user ID: ");
                String userId = scanner.nextLine();
                library.displayUserBorrowingHistory(userId);
                break;
            case 7:
                System.out.print("Enter ISBN of book: ");
                String isbn = scanner.nextLine();
                library.displayBookBorrowingHistory(isbn);
                break;
            case 8:
                library.displayAllUsers();
                break;
            case 9:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private static void manageRequestQueue() {
        System.out.println();
        System.out.println("=== Request Queue Management ===");
        System.out.println("1. View Queue for a Book");
        System.out.println("2. View All Requested Books");
        System.out.println("3. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                System.out.print("Enter ISBN of book: ");
                String isbn = scanner.nextLine();
                library.displayBookQueue(isbn);
                break;
            case 2:
                library.displayRequestedBooks();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private static void manageReturnStack() {
        System.out.println();
        System.out.println("=== Return Stack Management ===");
        System.out.println("1. View Recent Returns");
        System.out.println("2. Pop Most Recent Return");
        System.out.println("3. Peek at Most Recent Return");
        System.out.println("4. Get Book Recommendations");
        System.out.println("5. View Stack Size");
        System.out.println("6. Back to Main Menu");
        
        int choice = getIntInput("Enter your choice: ");
        
        switch (choice) {
            case 1:
                int count = getIntInput("How many recent returns to display? ");
                library.displayRecentReturns(count);
                break;
            case 2:
                library.popReturnStack();
                break;
            case 3:
                Book book = library.peekReturnStack();
                if (book != null) {
                    System.out.println("Most recent return: " + book);
                } else {
                    System.out.println("Return stack is empty.");
                }
                break;
            case 4:
                int recCount = getIntInput("How many recommendations to display? ");
                library.displayBookRecommendations(recCount);
                break;
            case 5:
                int size = library.getReturnStackSize();
                System.out.println("Return stack contains " + size + " book(s).");
                break;
            case 6:
                return;
            default:
                System.out.println("Invalid choice.");
        }
    }
    
    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("Invalid input. Please enter a number.");
            return -1;
        }
    }
}
