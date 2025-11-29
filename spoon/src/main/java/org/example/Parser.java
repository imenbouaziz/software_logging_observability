package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.regex.*;

public class Parser {
    //capt from log lines
    private static final Pattern ACTION = Pattern.compile(
            "ACTION\\s*\\|\\s*userId=([^\\s|]+)\\s*\\|\\s*action=([^\\s|]+)",
            Pattern.CASE_INSENSITIVE
    );

    public static void main(String[] args) throws Exception {
        Path logPath = Paths.get("logs", "app.log");
        Path outDir = Paths.get("profiles");

        if (!Files.exists(logPath)) {
            System.err.println("Log file not found: " + logPath.toAbsolutePath());
            return;
        }
        if (!Files.exists(outDir)) {
            Files.createDirectories(outDir);
        }

        Map<String, Stats> statsByUser = new HashMap<>();

        try (BufferedReader br = Files.newBufferedReader(logPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                Matcher m = ACTION.matcher(line);
                if (!m.find()) continue;
                String userId = m.group(1);
                String action = m.group(2).toLowerCase(Locale.ROOT);

                statsByUser.computeIfAbsent(userId, k -> new Stats()).incrrement(action);
            }
        }

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        for (Map.Entry<String, Stats> e : statsByUser.entrySet()) {
            Map<String, Object> profile = new LinkedHashMap<>();
            profile.put("userId", e.getKey());
            profile.put("UserType", e.getValue().userType());

            Map<String, Integer> counters = new LinkedHashMap<>();
            counters.put("reads", e.getValue().getReads());
            counters.put("writes", e.getValue().getWrites());
            profile.put("stats", counters);

            Path out = outDir.resolve(e.getKey() + ".json");
            try (Writer w = Files.newBufferedWriter(out)) {
                mapper.writeValue(w, profile);
            }
        }

        System.out.println("sauv to: " + outDir.toAbsolutePath());
    }
}
