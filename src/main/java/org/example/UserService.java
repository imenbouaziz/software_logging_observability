package org.example;

public class UserService {
    private UserRepository repo;

    public UserService(UserRepository repo) {
        this.repo = repo;
    }
    public void register(User user) throws Exception {
        if (repo.getUserByEmail(user.getEmail()) != null) {
            throw new Exception("Email already registered");
        }
        repo.addUser(user);
    }

    public User authenticate(String email, String password) throws Exception {
        User user = repo.getUserByEmail(email);
        if (user == null || !user.getPassword().equals(password)) {
            throw new Exception("invalid email or password");
        }
        return user;
    }
}
