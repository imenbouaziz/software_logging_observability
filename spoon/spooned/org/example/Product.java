package org.example;
import java.time.LocalDate;
public class Product {
    private int id;

    private String name;

    private double price;

    private LocalDate expiration_date;

    public Product(int id, String name, double price, LocalDate expiration_date) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.expiration_date = expiration_date;
    }

    public int getId() {
        logger.info("ACTION | userId={} | action=READ | method=getId");
        return id;
    }

    public void setId(int id) {
        logger.info("ACTION | userId={} | action=WRITE | method=setId");
        this.id = id;
    }

    public String getName() {
        logger.info("ACTION | userId={} | action=READ | method=getName");
        return name;
    }

    public void setName(String name) {
        logger.info("ACTION | userId={} | action=WRITE | method=setName");
        this.name = name;
    }

    public double getPrice() {
        logger.info("ACTION | userId={} | action=READ | method=getPrice");
        return price;
    }

    public void setPrice(double price) {
        logger.info("ACTION | userId={} | action=WRITE | method=setPrice");
        this.price = price;
    }

    public LocalDate getExpiration_date() {
        logger.info("ACTION | userId={} | action=READ | method=getExpiration_date");
        return expiration_date;
    }

    public void setExpiration_date(LocalDate expiration_date) {
        logger.info("ACTION | userId={} | action=WRITE | method=setExpiration_date");
        this.expiration_date = expiration_date;
    }
}