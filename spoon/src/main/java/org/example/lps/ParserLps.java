package org.example.lps;

import java.util.HashMap;
import java.util.Map;

public class ParserLps {

    public static Lps parse(String line) {
        Map<String, String> fields = extractFields(line);

        return new LpsBuilder()
                .timestamp(fields.get("timestamp"))
                .event(fields.get("Event"))
                .userId(fields.get("userId"))
                .action(fields.get("action"))
                .method(fields.get("method"))
                .expensiveCount(fields.get("expensiveCount"))
                .totalExpensiveProducts(fields.get("totalExpensiveProducts"))
                .build();
    }

    private static Map<String, String> extractFields(String line) {
        Map<String, String> map = new HashMap<>();

        String[] parts = line.split("\\|");
        for (String p : parts) {
            p = p.trim();
            if (p.contains("=")) {
                String[] arr = p.split("=", 2);
                map.put(arr[0].trim(), arr[1].trim());
            }
        }
        return map;
    }
}

