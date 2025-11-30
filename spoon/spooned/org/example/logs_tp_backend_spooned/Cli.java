package org.example.logs_tp_backend_spooned;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import org.slf4j.Logger;
public class Cli {
    public static void main(String[] args) throws Exception {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "main");
        try {
            org.example.FirebaseInitializer.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        org.example.UserRepository userRepo = new org.example.UserRepository();
        org.example.UserService userService = new org.example.UserService(userRepo);
        org.example.UserController userController = new org.example.UserController(userService);
        org.example.ProductRepository productRepo = new org.example.ProductRepository();
        org.example.ProductService productService = new org.example.ProductService(productRepo);
        org.example.ProductController productController = new org.example.ProductController(productService);
        org.example.User currentUser = null;
        while (currentUser == null) {
            System.out.println("\nexercice 1 ---------------------------");
            System.out.println("1.Register");
            System.out.println("2.Login");
            System.out.print("your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 :
                    System.out.print("Enter name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter age: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter email: ");
                    String email = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String password = scanner.nextLine();
                    userController.register(new org.example.User(0, name, age, email, password));
                    System.out.println("Registration successful, please login.");
                    break;
                case 2 :
                    System.out.print("Enter email: ");
                    String loginEmail = scanner.nextLine();
                    System.out.print("Enter password: ");
                    String loginPassword = scanner.nextLine();
                    userController.login(loginEmail, loginPassword);
                    currentUser = new org.example.User(0, "Temp", 0, loginEmail, loginPassword);
                    System.out.println("Login attempted, check Firebase logs.");
                    break;
                default :
                    System.out.println("Invalid choice");
            }
        } 
        int choice = -1;
        while (choice != 0) {
            System.out.println("\nProd menu-----------");
            System.out.println("1. Display all products");
            System.out.println("2. Fetch product by id");
            System.out.println("3. Add product");
            System.out.println("4. Delete product");
            System.out.println("5. Update product");
            System.out.println("0. Exit");
            System.out.print("Your choice: ");
            choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 :
                    productController.getAllProducts();
                    break;
                case 2 :
                    System.out.print("Enter product id: ");
                    int pid = Integer.parseInt(scanner.nextLine());
                    productController.getProductById(pid);
                    break;
                case 3 :
                    System.out.print("Enter name: ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter price: ");
                    double price = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter expiration (yyyy-MM-dd): ");
                    LocalDate exp = LocalDate.parse(scanner.nextLine(), formatter);
                    productController.addProduct(new org.example.Product(0, newName, price, exp));
                    break;
                case 4 :
                    System.out.print("Enter product id to delete: ");
                    int delId = Integer.parseInt(scanner.nextLine());
                    productController.deleteProduct(delId);
                    break;
                case 5 :
                    System.out.print("Enter product ID to update: ");
                    int updId = Integer.parseInt(scanner.nextLine());
                    System.out.print("Enter new name: ");
                    String updName = scanner.nextLine();
                    System.out.print("Enter new price: ");
                    double updPrice = Double.parseDouble(scanner.nextLine());
                    System.out.print("Enter new expiration (yyyy-MM-dd): ");
                    LocalDate updExp = LocalDate.parse(scanner.nextLine(), formatter);
                    productController.updateProduct(updId, updName, updPrice, updExp);
                    break;
                case 0 :
                    System.out.println("goodbye....");
                    break;
                default :
                    System.out.println("Invalid choice!");
            }
        } 
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.Cli.class);
}