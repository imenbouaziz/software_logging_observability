package org.example.logs_tp_backend_spooned;
import java.time.LocalDate;
import org.slf4j.Logger;
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
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getId");
        return this.id;
    }

    public void setId(int id) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "setId");
        this.id = id;
    }

    public String getName() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getName");
        return this.name;
    }

    public void setName(String name) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "setName");
        this.name = name;
    }

    public double getPrice() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getPrice");
        return this.price;
    }

    public void setPrice(double price) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "setPrice");
        this.price = price;
    }

    public LocalDate getExpiration_date() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getExpiration_date");
        return this.expiration_date;
    }

    public void setExpiration_date(LocalDate expiration_date) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "setExpiration_date");
        this.expiration_date = expiration_date;
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.Product.class);
}