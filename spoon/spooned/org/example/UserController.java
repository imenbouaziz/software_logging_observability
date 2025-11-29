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
    public void register(@RequestBody
    User user) throws Exception {
        logger.info("ACTION | userId={} | action=UNKNOWN | method=register");
        service.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestParam
    String email, @RequestParam
    String password) throws Exception {
        logger.info("ACTION | userId={} | action=UNKNOWN | method=login");
        return service.authenticate(email, password);
    }
}