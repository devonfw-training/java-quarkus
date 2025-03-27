package org.example.app.general.common.security;

import io.quarkus.runtime.configuration.ConfigUtils;
import io.smallrye.jwt.auth.principal.ParseException;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class JwtService {

    private static final String KEYCLOAK_TOKEN_URL = "http://localhost:8180/realms/quarkus/protocol/openid-connect/token";
    private static final String CLIENT_ID = "backend-service";
    private static final String REDIRECT_URI = "http://localhost:8080/auth/callback"; // This must be the same as the one used in the authorization request
    private static final String KEYCLOAK_AUTH_URL = "http://localhost:8180/realms/quarkus/protocol/openid-connect/auth";
    private static final String RESPONSE_TYPE = "code";
    private static final String SCOPE = "openid";

    public List<String> getRoles(String jwtString) throws ParseException {
        // Split JWT into its parts: header, payload, and signature
        String[] chunks = jwtString.split("\\.");

        // Base64 decode the payload (second part of the JWT)
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String payload = new String(decoder.decode(chunks[1]));

        // Parse the payload as JSONObject
        JSONObject jsonPayload = new JSONObject(payload);

        if (ConfigUtils.getProfiles().contains("test")) {
            return jsonPayload.optJSONArray("groups").toList().stream()
                    .map(role -> role.toString().trim().toUpperCase())  // Convert each role to uppercase
                    .collect(Collectors.toList());
        }
        JSONObject realmAccess = jsonPayload.optJSONObject("realm_access");

        // Check if the realm_access is found and contains the "roles" array
        if (realmAccess != null) {
            List<String> roles = realmAccess.optJSONArray("roles").toList().stream()
                    .map(role -> role.toString().trim().toUpperCase())  // Convert each role to uppercase
                    .collect(Collectors.toList());

            return roles;
        } else {
            // If realm_access or roles are not found, return an empty list or handle the case
            return List.of();
        }
    }

    public String exchangeCodeForToken(String code) {
        Client client = ClientBuilder.newClient();

        Form form = new Form();
        form.param("client_id", CLIENT_ID);
        form.param("code", code);
        form.param("redirect_uri", REDIRECT_URI);
        form.param("grant_type", "authorization_code");

        Response response = client.target(KEYCLOAK_TOKEN_URL)
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.form(form));

        if (response.getStatus() == 200) {
            String responseJson = response.readEntity(String.class); // Get the response as a JSON string

            // Parse the JSON response to extract the access token
            JsonObject jsonObject = Json.createReader(new StringReader(responseJson)).readObject();
            String accessToken = jsonObject.getString("access_token");

            response.close();
            return accessToken;
        } else {
            response.close();
            return null;
        }
    }

    public String buildKeycloakAuthUrl() {
        // Include the original URL as a query parameter for the callback
        return UriBuilder.fromUri(KEYCLOAK_AUTH_URL)
                .queryParam("client_id", CLIENT_ID)
                .queryParam("redirect_uri", REDIRECT_URI)
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("scope", SCOPE)
                .queryParam("state", "state")
                .queryParam("nonce", "nonce")
                .build()
                .toString();
    }
}