package org.example;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public void register(@RequestBody User user) throws Exception {
        service.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestParam String email,
                      @RequestParam String password) throws Exception {
        return service.authenticate(email, password);
    }
}
