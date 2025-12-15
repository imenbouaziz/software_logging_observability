package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.*;

public class MainLps {

    public static void main(String[] args) throws IOException {

        Path logPath = Paths.get("/home/imene/IdeaProjects/logs_tp_backend_spooned/logs/app.log");

        if (!Files.exists(logPath)) {
            System.err.println("Log file not found: " + logPath.toAbsolutePath());
            return;
        }

        try (BufferedReader br = Files.newBufferedReader(logPath)) {
            String line;

            while ((line = br.readLine()) != null) {
                if (!line.contains("ACTION")) continue;
                Lps lps = ParserLps.parse(line);

                System.out.println("timestamp: " + lps.getTimestamp());
                System.out.println("Event: " + lps.getEvent());
                System.out.println("UserId: " + lps.getUserId());
                System.out.println("Action: " + lps.getAction());
                System.out.println("Method: " + lps.getMethod());
                System.out.println("ExpensiveCount: " + lps.getExpensiveCount());
                System.out.println("TotalExpensiveProducts: " + lps.getTotalExpensiveProducts());
                System.out.println();
            }
        }
    }
}

