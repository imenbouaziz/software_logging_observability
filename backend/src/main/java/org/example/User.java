package org.example;


import java.util.List;

public class User {

    private int ID;
    private String name;
    private int age;
    private String email;
    private String password;
    private List<Product> products;

    public User(int ID, String name, int age, String email, String password,  List<Product> products) {
        this.ID = ID;
        this.name = name;
        this.age = age;

        this.email = email;
        this.password = password;
        this.products = products;

    }

    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public List<Product> getProducts() {
        return products;
    }
    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
