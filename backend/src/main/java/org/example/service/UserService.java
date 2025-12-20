package org.example.service;

import org.example.repository.UserRepository;
import org.example.model.User;
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
        repo.registerUser(user);
    }

    public User login(String email, String password) {
        User user = repo.getUserByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
}
