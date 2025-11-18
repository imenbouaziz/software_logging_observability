package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class Cli {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        UserRepository userRepo = new UserRepository();
        UserService userService = new UserService(userRepo);
        UserController userController = new UserController(userService);

        ProductRepository productRepo = new ProductRepository();
        ProductService productService = new ProductService(productRepo);
        ProductController productController = new ProductController(productService);

        User currentUser = null;

        //auth
        while (currentUser == null) {
            System.out.println("\nexercice 1 ---------------------------");
            System.out.println("1.Register");
            System.out.println("2.Login");
            System.out.print("your choice: ");
            int choice = Integer.parseInt(scanner.nextLine());

            try {
                switch (choice) {
                    case 1:
                        System.out.print("Enter id: ");
                        int id = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter age: ");
                        int age = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter email: ");
                        String email = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String password = scanner.nextLine();
                        userController.register(new User(id, name, age, email, password));
                        System.out.println("Registration successful, please login.");
                        break;
                    case 2:
                        System.out.print("Enter email: ");
                        String loginEmail = scanner.nextLine();
                        System.out.print("Enter password: ");
                        String loginPassword = scanner.nextLine();
                        currentUser = userController.login(loginEmail, loginPassword);
                        System.out.println("Login successful, hello , " + currentUser.getName());
                        break;
                    default:
                        System.out.println("Invalid choice");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        // Product menu
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

            try {
                switch (choice) {
                    case 1:
                        List<Product> products = productController.getAllProducts();
                        products.forEach(System.out::println);
                        break;
                    case 2:
                        System.out.print("Enter product id: ");
                        int pid = Integer.parseInt(scanner.nextLine());
                        Product p = productController.getProductById(pid);
                        System.out.println(p);
                        break;
                    case 3:
                        System.out.print("Enter product id: ");
                        int newId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter name: ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter price: ");
                        double price = Double.parseDouble(scanner.nextLine());
                        System.out.print("Enter expiration (yyyy-MM-dd)(please insert it exactly like this format): ");
                        LocalDate exp = LocalDate.parse(scanner.nextLine(), formatter);
                        productController.addProduct(new Product(newId, newName, price, exp));
                        System.out.println("Product added youpi");
                        break;
                    case 4:
                        System.out.print("Enter product id to delete: ");
                        int delId = Integer.parseInt(scanner.nextLine());
                        productController.deleteProduct(delId);
                        System.out.println("Product deleted");
                        break;
                    case 5:
                        System.out.print("Enter product ID to update: ");
                        int updId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter new name: ");
                        String updName = scanner.nextLine();
                        System.out.print("Enter new price: ");
                        double updPrice = Double.parseDouble(scanner.nextLine());
                        System.out.print("Enter new expiration (yyyy-MM-dd): ");
                        LocalDate updExp = LocalDate.parse(scanner.nextLine(), formatter);
                        productController.updateProduct(updId, updName, updPrice, updExp);
                        System.out.println("Product updated");
                        break;
                    case 0:
                        System.out.println("goodbye....");
                        break;
                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
