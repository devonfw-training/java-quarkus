package org.example.app.general.common.security;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Cookie;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;

@Path("/auth/callback")
public class AuthCallbackResource {

    @Inject
    SessionService sessionService;

    @Inject
    JwtService jwtService;

    @Inject
    ContainerRequestContext requestContext;

    @GET
    public Response callback(@QueryParam("code") String code,
                             @Context HttpHeaders headers) {
        if (code == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Authorization code missing").build();
        }

        Cookie sessionCookie = headers.getCookies().get("SESSION_ID");
        if (sessionCookie == null) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Missing session ID cookie").build();
        }
        String sessionId = sessionCookie.getValue();
        String originalUrl = sessionService.getSession(sessionId).orElseThrow().originalUrl();

        String jwt = jwtService.exchangeCodeForToken(code);
        sessionService.storeSession(sessionId, jwt, "");

        // Redirect to the original URL the user wanted to access
        if (originalUrl != null && !originalUrl.isEmpty()) {
            return Response.status(Response.Status.FOUND)
                    .header("Location", originalUrl)
                    .build();
        } else {
            return Response.ok("Authenticated").build();
        }
    }
}
