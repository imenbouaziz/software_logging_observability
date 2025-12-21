package org.example.lps;

public class LpsBuilder {
    private String timestamp;
    private String event;
    private String userId;
    private String action;
    private String method;
    private String expensiveCount;
    private String totalExpensiveProducts;

    public LpsBuilder timestamp(String ts) {
        this.timestamp = ts;
        return this;
    }
    public LpsBuilder event(String e) {
        this.event = e;
        return this;
    }
    public LpsBuilder userId(String id) {
        this.userId = id;
        return this;
    }
    public LpsBuilder action(String a) {
        this.action = a;
        return this;
    }
    public LpsBuilder method(String m) {
        this.method = m;
        return this;
    }
    public LpsBuilder expensiveCount(String c) {
        this.expensiveCount = c;
        return this;
    }
    public LpsBuilder totalExpensiveProducts(String t) {
        this.totalExpensiveProducts = t;
        return this;
    }

    public Lps build() {
        return new Lps(timestamp, event, userId, action, method, expensiveCount, totalExpensiveProducts);
    }
}

