package org.example;

public class Lps {
    private final String timestamp;
    private final String event;
    private final String userId;
    private final String action;
    private final String method;
    private final String expensiveCount;
    private final String totalExpensiveProducts;

    public Lps(String timestamp, String event, String userId, String action,
               String method, String expensiveCount, String totalExpensiveProducts) {
        this.timestamp = timestamp;
        this.event = event;
        this.userId = userId;
        this.action = action;
        this.method = method;
        this.expensiveCount = expensiveCount;
        this.totalExpensiveProducts = totalExpensiveProducts;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public String getEvent() {
        return event;
    }
    public String getUserId() {
        return userId;
    }
    public String getAction() {
        return action;
    }
    public String getMethod() {
        return method;
    }
    public String getExpensiveCount() {
        return expensiveCount;
    }
    public String getTotalExpensiveProducts() {
        return totalExpensiveProducts;
    }
}

