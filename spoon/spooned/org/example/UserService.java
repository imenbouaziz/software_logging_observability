package org.example;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    private UserRepository repo;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    public void register(User user) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "register");
        repo.getUserByEmail(user.getEmail());
        repo.addUser(user);
    }

    public User authenticate(String email, String password) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "authenticate");
        repo.getUserByEmail(email);
        return null;
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.UserService.class);
}