package org.example.logs_tp_backend_spooned;
import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        logger.info("ACTION | userId={} | action={} | method={}", id, "UNKNOWN", "main");
        SpringApplication.run(org.example.Main.class, args);
    }

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(org.example.logs_tp_backend_spooned.logs_tp_backend_spooned.Main.class);
}