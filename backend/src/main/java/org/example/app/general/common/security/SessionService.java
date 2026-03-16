package org.example.app.general.common.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.value.SetArgs;
import io.quarkus.redis.datasource.value.ValueCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
            Log.error(e);
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
            Log.error(e);
            return Optional.empty();
        }
    }
}
