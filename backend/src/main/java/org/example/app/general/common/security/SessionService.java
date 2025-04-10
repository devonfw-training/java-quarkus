package org.example.app.general.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.keys.KeyCommands;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.Form;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.StringReader;
import java.util.Optional;

@ApplicationScoped
public class SessionService {

    private final ValueCommands<String, String> redis;
    private final ObjectMapper objectMapper; // Jackson ObjectMapper to serialize/deserialize

    @Inject
    public SessionService(RedisDataSource redisDataSource) {
        this.redis = redisDataSource.value(String.class);
        this.objectMapper = new ObjectMapper();
    }

    private static final long SESSION_EXPIRY_SECONDS = 600; // 10 min expiry

    // Store session containing JWT and original URL
    public void storeSession(String sessionId, String jwt, String originalUrl) {
        try {
            Session session = new Session(jwt, originalUrl);

            // Serialize session to JSON
            String sessionJson = objectMapper.writeValueAsString(session);

            // Use SetArgs to define expiration
            SetArgs setArgs = new SetArgs().ex(SESSION_EXPIRY_SECONDS);
            redis.set(sessionId, sessionJson, setArgs); // Store serialized session JSON in Redis
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get session containing JWT and original URL
    public Optional<Session> getSession(String sessionId) {
        try {
            String sessionJson = redis.get(sessionId);
            if (sessionJson != null) {
                // Deserialize session JSON back into Session object
                Session session = objectMapper.readValue(sessionJson, Session.class);
                return Optional.of(session);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
