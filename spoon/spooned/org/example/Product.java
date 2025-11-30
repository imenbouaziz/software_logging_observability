package org.example;
public class Product {
    private Integer id;

    private Integer userId;

    // Ineger bcs of some errors in firebase
    private String name;

    private Double price;

    private String expiration_date;

    // for fb
    public Product() {
    }

    public Product(Integer id, Integer userId, String name, Double price, String expiration_date) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.price = price;
        this.expiration_date = expiration_date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getExpirationDate() {
        return expiration_date;
    }

    public void setExpirationDate(String expirationDate) {
        this.expiration_date = expirationDate;
    }
}