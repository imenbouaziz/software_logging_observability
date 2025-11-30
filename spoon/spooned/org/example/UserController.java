package org.example;
import org.slf4j.Logger;
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
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "register");
        service.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestParam
    String email, @RequestParam
    String password) throws Exception {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "login");
        return service.authenticate(email, password);
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.UserController.class);
}