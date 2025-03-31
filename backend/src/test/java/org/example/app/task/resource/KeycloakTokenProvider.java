package org.example.app.task.resource;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

public class KeycloakTokenProvider {

    private static final String KEYCLOAK_URL = "http://localhost:8180/realms/quarkus/protocol/openid-connect/token";
    private static final String CLIENT_ID = "backend-service";
    private static final String USERNAME_ADMIN = "alice";
    private static final String PASSWORD_ADMIN = "alice";
    private static final String USERNAME_USER = "bob";
    private static final String PASSWORD_USER = "bob";
    private static final String SECRET = "secret";

    public static String getAccessTokenWithAdmin() {
        return getAccessToken(USERNAME_ADMIN, PASSWORD_ADMIN);
    }

    public static String getAccessTokenWithUser() {
        return getAccessToken(USERNAME_USER, PASSWORD_USER);
    }

    public static String getAccessToken(String username, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("grant_type", "password");
        params.put("client_id", CLIENT_ID);
        params.put("username", username);
        params.put("password", password);
        params.put("client_secret", SECRET);


        Response response = RestAssured.given()
                .contentType(ContentType.URLENC)
                .formParams(params)
                .post(KEYCLOAK_URL);

        if (response.getStatusCode() != 200) {
            throw new RuntimeException("Failed to get token: " + response.getBody().asString());
        }

        return response.jsonPath().getString("access_token");
    }
}