package org.example.app.task.service;

import jakarta.annotation.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;
import jakarta.inject.Inject;

/**
 * Example REST service demonstrating OIDC authentication and role-based authorization
 * in a Quarkus application.
 *
 * The @Authenticated annotation requires users to be authenticated with a valid JWT token.
 * The @RolesAllowed annotation restricts access to users with specific roles from Keycloak.
 */
@Path("/example")
@Authenticated
public class OidcAuthenticationExampleResource {

    /**
     * Inject the current JWT to access user information
     */
    @Inject
    JsonWebToken jwt;

    /**
     * Endpoint accessible to any authenticated user (any role)
     *
     * @return Current user information
     */
    @GET
    @Path("/me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentUser() {
        return Response.ok()
                .entity(new UserInfo(
                        jwt.getSubject(),
                        jwt.getClaim("email"),
                        jwt.getClaim("name"),
                        jwt.getClaim("preferred_username")
                ))
                .build();
    }

    /**
     * Endpoint accessible only to users with 'admin' role
     *
     * Usage: curl -H "Authorization: Bearer $TOKEN" http://localhost:8080/example/admin-only
     *
     * @return Admin information
     */
    @GET
    @Path("/admin-only")
    @RolesAllowed({"admin"})
    @Produces(MediaType.TEXT_PLAIN)
    public Response adminOperation() {
        return Response.ok("Hello admin user: " + jwt.getSubject()).build();
    }

    /**
     * Endpoint accessible to users with 'admin' OR 'user' role
     *
     * @return User-level information
     */
    @GET
    @Path("/user-level")
    @RolesAllowed({"user", "admin"})
    @Produces(MediaType.TEXT_PLAIN)
    public Response userLevelOperation() {
        return Response.ok("Hello user: " + jwt.getSubject()).build();
    }

    /**
     * Endpoint that shows all claims from the JWT token
     * Useful for debugging
     *
     * @return All JWT claims
     */
    @GET
    @Path("/token-claims")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTokenClaims() {
        return Response.ok()
                .entity(new TokenInfo(
                        jwt.getSubject(),
                        jwt.getIssuer(),
                        jwt.getTokenID(),
                        jwt.getClaim("roles"),
                        jwt.getExpirationTime(),
                        jwt.getIssuedAtTime()
                ))
                .build();
    }

    /**
     * DTO for user information
     */
    public static class UserInfo {
        public String subject;
        public Object email;
        public Object name;
        public Object username;

        public UserInfo(String subject, Object email, Object name, Object username) {
            this.subject = subject;
            this.email = email;
            this.name = name;
            this.username = username;
        }
    }

    /**
     * DTO for token information (for debugging)
     */
    public static class TokenInfo {
        public String subject;
        public String issuer;
        public String tokenId;
        public Object roles;
        public Long expirationTime;
        public Long issuedAtTime;

        public TokenInfo(String subject, String issuer, String tokenId, Object roles,
                         Long expirationTime, Long issuedAtTime) {
            this.subject = subject;
            this.issuer = issuer;
            this.tokenId = tokenId;
            this.roles = roles;
            this.expirationTime = expirationTime;
            this.issuedAtTime = issuedAtTime;
        }
    }
}

