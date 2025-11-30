package org.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Parser {

    public static void main(String[] args) throws Exception {
        Path logPath = Paths.get("/home/imene/IdeaProjects/logs_tp_backend_spooned/logs/app.log");
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
                if (!line.contains("ACTION")) continue;

                String[] parts = line.split("\\|");
                Map<String, String> fields = new HashMap<>();

                for (String part : parts) {
                    part = part.trim();
                    if (part.contains("=")) {
                        String[] kv = part.split("=", 2);
                        fields.put(kv[0].trim(), kv[1].trim());
                    } else {
                        fields.put("event", part);
                    }
                }

                if (!fields.containsKey("userId")) continue;

                String userKey = "userId:" + fields.get("userId");
                String action = fields.getOrDefault("action", "").toLowerCase(Locale.ROOT);

                Stats stats = statsByUser.computeIfAbsent(userKey, k -> new Stats());
                stats.incrrement(action);

                String expCountStr = fields.get("expensiveCount");
                String totalExpStr = fields.get("totalExpensiveProducts");
                if (expCountStr != null && totalExpStr != null) {
                    int expCount = Integer.parseInt(expCountStr);
                    int totalExp = Integer.parseInt(totalExpStr);
                    stats.addExpensiveSearch(expCount, totalExp);
                }
            }
        }

        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

        for (Map.Entry<String, Stats> e : statsByUser.entrySet()) {
            Map<String, Object> profile = new LinkedHashMap<>();
            profile.put("userKey", e.getKey());
            profile.put("UserType", e.getValue().userType());

            Map<String, Integer> counters = new LinkedHashMap<>();
            counters.put("reads", e.getValue().getReads());
            counters.put("writes", e.getValue().getWrites());
            counters.put("searchedExpensive", e.getValue().getSearchedExpensive());
            counters.put("totalExpensiveProducts", e.getValue().getTotalExpensiveProducts());
            profile.put("stats", counters);

            Path out = outDir.resolve(e.getKey().replaceAll("[^a-zA-Z0-9]", "_") + ".json");
            try (Writer w = Files.newBufferedWriter(out)) {
                mapper.writeValue(w, profile);
            }
        }

        System.out.println("Profiles saved to: " + outDir.toAbsolutePath());
    }
}
