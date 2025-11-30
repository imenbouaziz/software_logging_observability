package org.example.logs_tp_backend_spooned;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
@Service
public class UserService {
    private org.example.UserRepository repo;

    private static final Logger log = LoggerFactory.getLogger(org.example.ProductService.class);

    public UserService(org.example.UserRepository repo) {
        this.repo = repo;
    }

    public void register(org.example.User user) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "register");
        this.repo.getUserByEmail(user.getEmail());
        this.repo.addUser(user);
    }

    public org.example.User authenticate(String email, String password) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "authenticate");
        this.repo.getUserByEmail(email);
        return null;
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.UserService.class);
}