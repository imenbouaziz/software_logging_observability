package org.example;

import java.util.Date;

public class Product {

    private int id;
    private String name;
    private double price;
    private Date expiration_date;

    public Product(int id, String name, double price, Date expiration_date) {
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
    public Date getExpiration_date() {
        return expiration_date;
    }
    public void setExpiration_date(Date expiration_date) {
        this.expiration_date = expiration_date;
    }
}
