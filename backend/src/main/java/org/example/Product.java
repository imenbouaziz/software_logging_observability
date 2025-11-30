package org.example;

import java.time.LocalDate;
import java.util.Date;

public class Product {

    private int id;
    private String name;
    private double price;
    private LocalDate expiration_date;
    private int userID;

    public Product(int id, String name, double price, LocalDate expiration_date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expiration_date = expiration_date;

    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
    public LocalDate getExpiration_date() {
        return expiration_date;
    }
    public void setExpiration_date(LocalDate expiration_date) {
        this.expiration_date = expiration_date;

    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
}
