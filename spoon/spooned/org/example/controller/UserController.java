package org.example.controller;
import org.example.model.User;
import org.example.service.UserService;
import org.slf4j.Logger;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true", maxAge = 3600)
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
        service.register(user);
        Integer userId = user.getId();
        logger.info("ACTION | userId={} | action={} | method={} ", userId, "WRITE", "register");
    }

    @PostMapping("/login")
    public User login(@RequestParam
    String email, @RequestParam
    String password, HttpSession session) throws Exception {
        User user = service.login(email, password);
        Integer userId = (user != null) ? user.getId() : null;
        if (user != null) {
            System.out.println("Logged in user id = " + user.getId());
            session.setAttribute("userId", user.getId());
        }
        logger.info("ACTION | userId={} | action={} | method={} ", userId, "READ", "login");
        return user;
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.controller.UserController.class);
}