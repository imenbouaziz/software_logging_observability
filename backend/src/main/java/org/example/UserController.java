package org.example;

import jakarta.servlet.http.HttpSession;
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
        Integer userId = user.getId();
    }

    @PostMapping("/login")
    public User login(@RequestParam String email,
                      @RequestParam String password,
                      HttpSession session) throws Exception {
        User user = service.login(email, password);
        Integer userId = (user != null) ? user.getId() : null;
        if (user != null) {
            System.out.println("Logged in user id = " + user.getId());
            session.setAttribute("userId", user.getId());
        }
        return user;
    }


}
