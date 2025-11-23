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
        repo.getUserByEmail(user.getEmail());
        repo.addUser(user);
    }

    public User authenticate(String email, String password) {
        repo.getUserByEmail(email);
        return null;
    }
}
