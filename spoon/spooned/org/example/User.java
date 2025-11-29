package org.example;
public class User {
    private int ID;

    private String name;

    private int age;

    private String email;

    private String password;

    public User(int ID, String name, int age, String email, String password) {
        this.ID = ID;
        this.name = name;
        this.age = age;
        this.email = email;
        this.password = password;
    }

    public int getID() {
        logger.info("ACTION | userId={} | action=READ | method=getID");
        return ID;
    }

    public void setID(int ID) {
        logger.info("ACTION | userId={} | action=WRITE | method=setID");
        this.ID = ID;
    }

    public String getName() {
        logger.info("ACTION | userId={} | action=READ | method=getName");
        return name;
    }

    public void setName(String name) {
        logger.info("ACTION | userId={} | action=WRITE | method=setName");
        this.name = name;
    }

    public int getAge() {
        logger.info("ACTION | userId={} | action=READ | method=getAge");
        return age;
    }

    public void setAge(int age) {
        logger.info("ACTION | userId={} | action=WRITE | method=setAge");
        this.age = age;
    }

    public String getEmail() {
        logger.info("ACTION | userId={} | action=READ | method=getEmail");
        return email;
    }

    public void setEmail(String email) {
        logger.info("ACTION | userId={} | action=WRITE | method=setEmail");
        this.email = email;
    }

    public String getPassword() {
        logger.info("ACTION | userId={} | action=READ | method=getPassword");
        return password;
    }

    public void setPassword(String password) {
        logger.info("ACTION | userId={} | action=WRITE | method=setPassword");
        this.password = password;
    }
}