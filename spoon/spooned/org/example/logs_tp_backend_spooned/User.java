package org.example.logs_tp_backend_spooned;
import org.slf4j.Logger;
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
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getID");
        return this.ID;
    }

    public void setID(int ID) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "setID");
        this.ID = ID;
    }

    public String getName() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getName");
        return this.name;
    }

    public void setName(String name) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "setName");
        this.name = name;
    }

    public int getAge() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getAge");
        return this.age;
    }

    public void setAge(int age) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "setAge");
        this.age = age;
    }

    public String getEmail() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getEmail");
        return this.email;
    }

    public void setEmail(String email) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "setEmail");
        this.email = email;
    }

    public String getPassword() {
        logger.info("ACTION | userId={} | action={} | method={}", id, "READ", "getPassword");
        return this.password;
    }

    public void setPassword(String password) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "WRITE", "setPassword");
        this.password = password;
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.User.class);
}