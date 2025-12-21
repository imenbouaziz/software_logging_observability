package org.example.logs_tp_backend_spooned;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Component
@Order(1)
public class PostmanCollectionGenerator implements CommandLineRunner {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Generating Postman collection...");

        ObjectNode collection = mapper.createObjectNode();

        ObjectNode info = collection.putObject("info");
        info.put("name", "Product API Test Scenarios - All 10 Users");
        info.put("schema", "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        ArrayNode items = collection.putArray("item");

        ObjectNode preregFolder = mapper.createObjectNode();
        preregFolder.put("name", "0. Pre-Registration - All Users");
        ArrayNode preregItems = preregFolder.putArray("item");
        for (int i = 1; i <= 10; i++) {
            String email = "user" + i + "@example.com";
            preregItems.add(createRegisterRequest(i, email, "password"));
            preregItems.add(createLoginRequest(i, email, "password"));
            for (int j = 1; j <= 5; j++) {
                preregItems.add(createAddProductRequest(i, "BaseProduct-" + i + "-" + j, 10.0 * j + i));
            }
        }
        items.add(preregFolder);


        items.add(createUser1Scenario());

        items.add(createUser2Scenario());

        items.add(createUser3Scenario());

        items.add(createUser4Scenario());

        items.add(createUser5Scenario());

        items.add(createUser6Scenario());

        items.add(createUser7Scenario());

        items.add(createUser8Scenario());

        items.add(createUser9Scenario());

        items.add(createUser10Scenario());

        ArrayNode variables = collection.putArray("variable");
        ObjectNode baseUrlVar = variables.addObject();
        baseUrlVar.put("key", "baseUrl");
        baseUrlVar.put("value", "localhost:8080");
        baseUrlVar.put("type", "string");

        File outputFile = new File("src/main/resources/postman-collection.json");
        mapper.writerWithDefaultPrettyPrinter().writeValue(outputFile, collection);

        System.out.println("postman collection generated: " + outputFile.getAbsolutePath());
        System.out.println("\nTo run with Newman, use one of these commands:");
        System.out.println("   newman run " + outputFile.getAbsolutePath());
        System.out.println("   newman run " + outputFile.getAbsolutePath() + " --cookie-jar cookies.json");
        System.out.println("\n Or import the collection into Postman GUI\n");
    }

    private void runNewman(String collectionPath) {
        try {
            File cookieJar = new File("newman-cookies.json");
            if (!cookieJar.exists()) {
                mapper.writeValue(cookieJar, mapper.createObjectNode());
            }

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "newman", "run", collectionPath,
                    "--reporters", "cli",
                    "--color", "on",
                    "--delay-request", "50",
                    "--timeout-request", "10000",
                    "--cookie-jar", cookieJar.getAbsolutePath()
            );

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();

            System.out.println("\n" + "=".repeat(70));
            if (exitCode == 0) {
                System.out.println("ALL SCENARIOS EXECUTED SUCCESSFULLY");
            } else {
                System.err.println("❌ Newman execution failed with exit code: " + exitCode);
            }
            System.out.println("=".repeat(70) + "\n");

        } catch (Exception e) {
            System.err.println(" Newman not found or failed to run.");
            System.err.println("Install Newman with: npm install -g newman");
            System.err.println("Or manually run: newman run " + collectionPath + " --cookie-jar cookies.json");
            e.printStackTrace();
        }
    }

    private ObjectNode createUser1Scenario() {
        ObjectNode folder = mapper.createObjectNode();
        folder.put("name", "1. User 1 - Read Heavy + Global Queries");
        ArrayNode items = folder.putArray("item");

        items.add(createLoginRequest(1, "user1@example.com", "password"));
        items.add(createFetchAllRequest(1));
        items.add(createFetchAllRequest(1));
        items.add(createFetchAllRequest(1));
        items.add(createFetchByIdRequest(1, 1));
        items.add(createFetchByIdRequest(1, 2));
        items.add(createFetchByIdRequest(1, 3));
        items.add(createFetchByIdRequest(1, 4));
        items.add(createFetchByIdRequest(1, 5));
        items.add(createFetchAllGlobalRequest(1));
        items.add(createFetchAllGlobalRequest(1));
        items.add(createFetchByNameGlobalRequest(1, "ProductA"));
        items.add(createFetchByNameGlobalRequest(1, "ProductB"));
        items.add(createFetchAllGlobalRequest(1));
        items.add(createFetchAllGlobalRequest(1));

        return folder;
    }

    // User 2 Scenario: CRUD operations
    private ObjectNode createUser2Scenario() {
        ObjectNode folder = mapper.createObjectNode();
        folder.put("name", "2. User 2 - Full CRUD Operations");
        ArrayNode items = folder.putArray("item");

        items.add(createLoginRequest(2, "user2@example.com", "password"));
        items.add(createAddProductRequest(2, "U2-Prod1", 5.0));
        items.add(createAddProductRequest(2, "U2-Prod2", 10.0));
        items.add(createAddProductRequest(2, "U2-Prod3", 15.0));
        items.add(createAddProductRequest(2, "U2-Prod4", 20.0));
        items.add(createAddProductRequest(2, "U2-Prod5", 25.0));

        items.add(createUpdateProductRequest(2, 1, "U2-Prod1-Updated", 6.0));
        items.add(createUpdateProductRequest(2, 2, "U2-Prod2-Updated", 11.0));
        items.add(createUpdateProductRequest(2, 3, "U2-Prod3-Updated", 16.0));
        items.add(createUpdateProductRequest(2, 4, "U2-Prod4-Updated", 21.0));
        items.add(createUpdateProductRequest(2, 5, "U2-Prod5-Updated", 26.0));

        items.add(createDeleteProductRequest(2, 1));
        items.add(createDeleteProductRequest(2, 2));
        items.add(createDeleteProductRequest(2, 3));

        items.add(createFetchAllRequest(2));
        items.add(createFetchAllRequest(2));
        items.add(createFetchAllRequest(2));
        items.add(createFetchByIdRequest(2, 4));
        items.add(createFetchByIdRequest(2, 5));

        return folder;
    }

    // User 3 Scenario: Heavy global searches
    private ObjectNode createUser3Scenario() {
        ObjectNode folder = mapper.createObjectNode();
        folder.put("name", "3. User 3 - Heavy Global Searches");
        ArrayNode items = folder.putArray("item");

        items.add(createLoginRequest(3, "user3@example.com", "password"));
        items.add(createFetchAllRequest(3));
        items.add(createFetchAllRequest(3));
        items.add(createFetchAllRequest(3));
        items.add(createFetchByIdRequest(3, 1));
        items.add(createFetchByIdRequest(3, 2));
        items.add(createFetchByIdRequest(3, 3));
        items.add(createFetchByIdRequest(3, 4));
        items.add(createFetchByIdRequest(3, 5));
        items.add(createFetchAllGlobalRequest(3));
        items.add(createFetchAllGlobalRequest(3));
        items.add(createFetchAllGlobalRequest(3));
        items.add(createFetchByNameGlobalRequest(3, "U3-Expensive1"));
        items.add(createFetchByNameGlobalRequest(3, "U3-Expensive2"));
        items.add(createFetchByNameGlobalRequest(3, "U3-Expensive3"));
        items.add(createFetchByNameGlobalRequest(3, "U3-Normal"));
        items.add(createFetchByNameGlobalRequest(3, "U3-Another"));

        return folder;
    }

    // User 4 Scenario: Error cases (invalid operations)
    private ObjectNode createUser4Scenario() {
        ObjectNode folder = mapper.createObjectNode();
        folder.put("name", "4. User 4 - Error Cases & Invalid Operations");
        ArrayNode items = folder.putArray("item");

        items.add(createLoginRequest(4, "user4@example.com", "password"));
        items.add(createFetchByIdRequest(4, 999));
        items.add(createFetchByIdRequest(4, 1000));
        items.add(createFetchByIdRequest(4, 1001));
        items.add(createFetchByIdRequest(4, 1002));
        items.add(createDeleteProductRequest(4, 999));
        items.add(createDeleteProductRequest(4, 1000));
        items.add(createDeleteProductRequest(4, 1001));
        items.add(createUpdateProductRequest(4, 999, "", -10.0));
        items.add(createUpdateProductRequest(4, 1000, " ", -5.0));
        items.add(createUpdateProductRequest(4, 1001, "Invalid", -1.0));
        items.add(createFetchAllRequest(4));
        items.add(createFetchAllRequest(4));
        items.add(createFetchAllRequest(4));
        items.add(createFetchByIdRequest(4, 1));
        items.add(createFetchByIdRequest(4, 2));
        items.add(createFetchByIdRequest(4, 3));

        return folder;
    }

    // User 5 Scenario: Unauthorized access then normal operations
    private ObjectNode createUser5Scenario() {
        ObjectNode folder = mapper.createObjectNode();
        folder.put("name", "5. User 5 - Unauthorized Then Authorized");
        ArrayNode items = folder.putArray("item");

        items.add(createLoginRequest(5, "user5@example.com", "password"));
        items.add(createFetchAllRequest(5));
        items.add(createFetchAllRequest(5));
        items.add(createFetchAllRequest(5));
        items.add(createFetchByIdRequest(5, 1));
        items.add(createFetchByIdRequest(5, 2));
        items.add(createFetchByIdRequest(5, 3));
        items.add(createAddProductRequest(5, "U5-Prod1", 10.0));
        items.add(createAddProductRequest(5, "U5-Prod2", 20.0));
        items.add(createUpdateProductRequest(5, 1, "U5-Prod1-Updated", 11.0));
        items.add(createUpdateProductRequest(5, 2, "U5-Prod2-Updated", 21.0));

        return folder;
    }

    // User 6 Scenario: Global search heavy
    private ObjectNode createUser6Scenario() {
        ObjectNode folder = mapper.createObjectNode();
        folder.put("name", "6. User 6 - Intensive Global Searches");
        ArrayNode items = folder.putArray("item");

        items.add(createLoginRequest(6, "user6@example.com", "password"));
        items.add(createFetchAllGlobalRequest(6));
        items.add(createFetchAllGlobalRequest(6));
        items.add(createFetchAllGlobalRequest(6));
        items.add(createFetchAllGlobalRequest(6));
        items.add(createFetchAllGlobalRequest(6));
        items.add(createFetchByNameGlobalRequest(6, "Global6-1"));
        items.add(createFetchByNameGlobalRequest(6, "Global6-2"));
        items.add(createFetchByNameGlobalRequest(6, "Global6-3"));
        items.add(createFetchByNameGlobalRequest(6, "Global6-4"));
        items.add(createFetchByNameGlobalRequest(6, "Global6-5"));
        items.add(createFetchByNameGlobalRequest(6, "Global6-6"));
        items.add(createFetchByNameGlobalRequest(6, "Global6-7"));
        items.add(createFetchByNameGlobalRequest(6, "Global6-8"));
        items.add(createFetchByNameGlobalRequest(6, "Global6-9"));
        items.add(createFetchByNameGlobalRequest(6, "Global6-10"));
        items.add(createFetchByIdRequest(6, 1));
        items.add(createFetchByIdRequest(6, 2));
        items.add(createFetchByIdRequest(6, 3));

        return folder;
    }

    // User 7 Scenario: Mixed operations
    private ObjectNode createUser7Scenario() {
        ObjectNode folder = mapper.createObjectNode();
        folder.put("name", "7. User 7 - Mixed CRUD + Global");
        ArrayNode items = folder.putArray("item");

        items.add(createLoginRequest(7, "user7@example.com", "password"));
        items.add(createFetchAllRequest(7));
        items.add(createFetchAllRequest(7));
        items.add(createFetchAllRequest(7));
        items.add(createAddProductRequest(7, "U7-Prod1", 5.0));
        items.add(createAddProductRequest(7, "U7-Prod2", 6.0));
        items.add(createAddProductRequest(7, "U7-Prod3", 7.0));
        items.add(createUpdateProductRequest(7, 1, "U7-Prod1-Upd", 5.5));
        items.add(createUpdateProductRequest(7, 2, "U7-Prod2-Upd", 6.5));
        items.add(createUpdateProductRequest(7, 3, "U7-Prod3-Upd", 7.5));
        items.add(createDeleteProductRequest(7, 1));
        items.add(createDeleteProductRequest(7, 2));
        items.add(createFetchAllGlobalRequest(7));
        items.add(createFetchAllGlobalRequest(7));
        items.add(createFetchAllGlobalRequest(7));
        items.add(createFetchByNameGlobalRequest(7, "U7-Prod3-Upd"));
        items.add(createFetchByNameGlobalRequest(7, "U7-Other"));
        items.add(createFetchByNameGlobalRequest(7, "U7-Another"));

        return folder;
    }

    // User 8 Scenario: Stress test
    private ObjectNode createUser8Scenario() {
        ObjectNode folder = mapper.createObjectNode();
        folder.put("name", "8. User 8 - Stress Test");
        ArrayNode items = folder.putArray("item");

        items.add(createLoginRequest(8, "user8@example.com", "password"));
        for (int i = 0; i < 10; i++) {
            items.add(createFetchAllRequest(8));
        }
        for (int i = 0; i < 5; i++) {
            items.add(createUpdateProductRequest(8, 1, "U8-Stress-" + i, 10.0 + i));
        }
        items.add(createDeleteProductRequest(8, 2));
        items.add(createDeleteProductRequest(8, 3));
        items.add(createDeleteProductRequest(8, 4));
        items.add(createFetchAllGlobalRequest(8));
        items.add(createFetchAllGlobalRequest(8));

        return folder;
    }

    // User 9 Scenario: Read-only inactive user
    private ObjectNode createUser9Scenario() {
        ObjectNode folder = mapper.createObjectNode();
        folder.put("name", "9. User 9 - Read-Only Inactive");
        ArrayNode items = folder.putArray("item");

        items.add(createLoginRequest(9, "user9@example.com", "password"));
        items.add(createFetchByIdRequest(9, 1));
        items.add(createFetchByIdRequest(9, 2));
        items.add(createFetchByIdRequest(9, 3));
        items.add(createFetchByIdRequest(9, 4));
        items.add(createFetchByIdRequest(9, 5));
        for (int i = 0; i < 5; i++) {
            items.add(createFetchAllRequest(9));
        }
        for (int i = 0; i < 5; i++) {
            items.add(createFetchAllGlobalRequest(9));
        }

        return folder;
    }

    // User 10 Scenario: New user full workflow
    private ObjectNode createUser10Scenario() {
        ObjectNode folder = mapper.createObjectNode();
        folder.put("name", "10. User 10 - New User Full Workflow");
        ArrayNode items = folder.putArray("item");

        items.add(createLoginRequest(10, "user10@example.com", "password"));
        items.add(createAddProductRequest(10, "U10-Prod1", 10.0));
        items.add(createAddProductRequest(10, "U10-Prod2", 20.0));
        items.add(createAddProductRequest(10, "U10-Prod3", 30.0));
        items.add(createUpdateProductRequest(10, 1, "U10-Prod1-Upd", 11.0));
        items.add(createUpdateProductRequest(10, 2, "U10-Prod2-Upd", 21.0));
        items.add(createUpdateProductRequest(10, 3, "U10-Prod3-Upd", 31.0));
        items.add(createDeleteProductRequest(10, 1));
        items.add(createDeleteProductRequest(10, 2));
        items.add(createFetchAllRequest(10));
        items.add(createFetchAllRequest(10));
        items.add(createFetchAllRequest(10));
        items.add(createFetchAllGlobalRequest(10));
        items.add(createFetchAllGlobalRequest(10));
        items.add(createFetchAllGlobalRequest(10));
        items.add(createFetchByNameGlobalRequest(10, "U10-Prod3-Upd"));
        items.add(createFetchByNameGlobalRequest(10, "U10-Expensive"));
        items.add(createFetchByNameGlobalRequest(10, "U10-Another"));

        return folder;
    }

    // Helper methods to create requests

    private ObjectNode createRegisterRequest(int userId, String email, String password) {
        ObjectNode request = mapper.createObjectNode();
        request.put("name", "Register User " + userId);

        ObjectNode req = request.putObject("request");
        req.put("method", "POST");

        ObjectNode url = req.putObject("url");
        url.put("raw", "{{baseUrl}}/users/register");
        url.put("protocol", "http");
        ArrayNode host = url.putArray("host");
        host.add("{{baseUrl}}");
        ArrayNode path = url.putArray("path");
        path.add("users");
        path.add("register");

        ObjectNode body = req.putObject("body");
        body.put("mode", "raw");

        body.put("raw", String.format(
                "{\n" +
                        "  \"id\": null,\n" +
                        "  \"name\": \"User%d\",\n" +
                        "  \"age\": %d,\n" +
                        "  \"email\": \"%s\",\n" +
                        "  \"password\": \"%s\",\n" +
                        "  \"products\": null\n" +
                        "}",
                userId, 20 + userId, email, password
        ));

        ArrayNode header = req.putArray("header");
        ObjectNode contentType = header.addObject();
        contentType.put("key", "Content-Type");
        contentType.put("value", "application/json");

        return request;
    }


    private ObjectNode createLoginRequest(int userId, String email, String password) {
        ObjectNode request = mapper.createObjectNode();
        request.put("name", "Login User " + userId);

        ObjectNode req = request.putObject("request");
        req.put("method", "POST");

        ObjectNode url = req.putObject("url");
        url.put("raw", "{{baseUrl}}/users/login?email=" + email + "&password=" + password);
        url.put("protocol", "http");
        ArrayNode host = url.putArray("host");
        host.add("{{baseUrl}}");
        ArrayNode path = url.putArray("path");
        path.add("users");
        path.add("login");

        ArrayNode query = url.putArray("query");
        ObjectNode emailParam = query.addObject();
        emailParam.put("key", "email");
        emailParam.put("value", email);
        ObjectNode passwordParam = query.addObject();
        passwordParam.put("key", "password");
        passwordParam.put("value", password);

        return request;
    }


    private ObjectNode createAddProductRequest(int userId, String name, double price) {
        ObjectNode request = mapper.createObjectNode();
        request.put("name", "Add: " + name);

        ObjectNode req = request.putObject("request");
        req.put("method", "POST");

        ObjectNode url = req.putObject("url");
        url.put("raw", "{{baseUrl}}/users/" + userId + "/products");
        url.put("protocol", "http");
        ArrayNode host = url.putArray("host");
        host.add("{{baseUrl}}");
        ArrayNode path = url.putArray("path");
        path.add("users");
        path.add(String.valueOf(userId));
        path.add("products");

        ObjectNode body = req.putObject("body");
        body.put("mode", "raw");
        body.put("raw", String.format(
                "{\n  \"name\": \"%s\",\n  \"price\": %.2f,\n  \"expirationDate\": \"2025-12-31\"\n}",
                name, price
        ));

        ArrayNode header = req.putArray("header");
        ObjectNode contentType = header.addObject();
        contentType.put("key", "Content-Type");
        contentType.put("value", "application/json");

        return request;
    }


    private ObjectNode createFetchAllRequest(int userId) {
        ObjectNode request = mapper.createObjectNode();
        request.put("name", "Fetch All Products");

        ObjectNode req = request.putObject("request");
        req.put("method", "GET");

        ObjectNode url = req.putObject("url");
        url.put("raw", "{{baseUrl}}/users/" + userId + "/products");
        url.put("protocol", "http");
        ArrayNode host = url.putArray("host");
        host.add("{{baseUrl}}");
        ArrayNode path = url.putArray("path");
        path.add("users");
        path.add(String.valueOf(userId));
        path.add("products");

        return request;
    }

    private ObjectNode createFetchByIdRequest(int userId, int productId) {
        ObjectNode request = mapper.createObjectNode();
        request.put("name", "Fetch Product ID: " + productId);

        ObjectNode req = request.putObject("request");
        req.put("method", "GET");

        ObjectNode url = req.putObject("url");
        url.put("raw", "{{baseUrl}}/users/" + userId + "/products/" + productId);
        url.put("protocol", "http");
        ArrayNode host = url.putArray("host");
        host.add("{{baseUrl}}");
        ArrayNode path = url.putArray("path");
        path.add("users");
        path.add(String.valueOf(userId));
        path.add("products");
        path.add(String.valueOf(productId));

        return request;
    }

    private ObjectNode createUpdateProductRequest(int userId, int productId, String name, double price) {
        ObjectNode request = mapper.createObjectNode();
        request.put("name", "Update Product " + productId + " to " + name);

        ObjectNode req = request.putObject("request");
        req.put("method", "PUT");

        ObjectNode url = req.putObject("url");
        url.put("raw", "{{baseUrl}}/users/" + userId + "/products/" + productId +
                "?name=" + name + "&price=" + price + "&expiration_date=2025-12-31");
        url.put("protocol", "http");
        ArrayNode host = url.putArray("host");
        host.add("{{baseUrl}}");
        ArrayNode path = url.putArray("path");
        path.add("users");
        path.add(String.valueOf(userId));
        path.add("products");
        path.add(String.valueOf(productId));
        ArrayNode query = url.putArray("query");
        ObjectNode nameParam = query.addObject();
        nameParam.put("key", "name");
        nameParam.put("value", name);
        ObjectNode priceParam = query.addObject();
        priceParam.put("key", "price");
        priceParam.put("value", String.valueOf(price));
        ObjectNode dateParam = query.addObject();
        dateParam.put("key", "expiration_date");
        dateParam.put("value", "2025-12-31");

        return request;
    }

    private ObjectNode createDeleteProductRequest(int userId, int productId) {
        ObjectNode request = mapper.createObjectNode();
        request.put("name", "Delete Product: " + productId);

        ObjectNode req = request.putObject("request");
        req.put("method", "DELETE");

        ObjectNode url = req.putObject("url");
        url.put("raw", "{{baseUrl}}/users/" + userId + "/products/" + productId);
        url.put("protocol", "http");
        ArrayNode host = url.putArray("host");
        host.add("{{baseUrl}}");
        ArrayNode path = url.putArray("path");
        path.add("users");
        path.add(String.valueOf(userId));
        path.add("products");
        path.add(String.valueOf(productId));

        return request;
    }

    private ObjectNode createFetchAllGlobalRequest(int userId) {
        ObjectNode request = mapper.createObjectNode();
        request.put("name", "Fetch All Global Products");

        ObjectNode req = request.putObject("request");
        req.put("method", "GET");

        ObjectNode url = req.putObject("url");
        url.put("raw", "{{baseUrl}}/users/" + userId + "/products/global");
        url.put("protocol", "http");
        ArrayNode host = url.putArray("host");
        host.add("{{baseUrl}}");
        ArrayNode path = url.putArray("path");
        path.add("users");
        path.add(String.valueOf(userId));
        path.add("products");
        path.add("global");

        return request;
    }

    private ObjectNode createFetchByNameGlobalRequest(int userId, String name) {
        ObjectNode request = mapper.createObjectNode();
        request.put("name", "Fetch Global by Name: " + name);

        ObjectNode req = request.putObject("request");
        req.put("method", "GET");

        ObjectNode url = req.putObject("url");
        url.put("raw", "{{baseUrl}}/users/" + userId + "/products/global/name/" + name);
        url.put("protocol", "http");
        ArrayNode host = url.putArray("host");
        host.add("{{baseUrl}}");
        ArrayNode path = url.putArray("path");
        path.add("users");
        path.add(String.valueOf(userId));
        path.add("products");
        path.add("global");
        path.add("name");
        path.add(name);

        return request;
    }
}