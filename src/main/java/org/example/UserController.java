package org.example;

public class UserController {
    private UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    public void register(User user) throws Exception {
        service.register(user);
    }

    public User login(String email, String password) throws Exception {
        return service.authenticate(email, password);
    }
}
