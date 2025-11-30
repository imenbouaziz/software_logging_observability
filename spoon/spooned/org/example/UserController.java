package org.example;
import org.slf4j.Logger;
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
    public void register(@RequestBody
    User user) throws Exception {
        logger.info("ACTION | user={} | action={} | method={}", user, "WRITE", "register");
        service.register(user);
    }

    @PostMapping("/login")
    public User login(@RequestParam
    String email, @RequestParam
    String password, HttpSession session) throws Exception {
        logger.info("ACTION | userEmail={} | action={} | method={}", email, "READ", "login");
        User user = service.login(email, password);
        if (user != null) {
            System.out.println("Logged in user id = " + user.getId());
            session.setAttribute("userId", user.getId());
        }
        return user;
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.UserController.class);
}