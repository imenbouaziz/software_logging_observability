package org.example.logs_tp_backend_spooned;
import org.example.PostMapping;
import org.example.RequestBody;
import org.example.RequestMapping;
import org.example.RequestParam;
import org.example.RestController;
import org.slf4j.Logger;
@RestController
@RequestMapping("/users")
public class UserController {
    private final org.example.UserService service;

    public UserController(org.example.UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public void register(@RequestBody
    org.example.User user) throws Exception {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "register");
        this.service.register(user);
    }

    @PostMapping("/login")
    public org.example.User login(@RequestParam
    String email, @RequestParam
    String password) throws Exception {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "login");
        return this.service.authenticate(email, password);
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.UserController.class);
}