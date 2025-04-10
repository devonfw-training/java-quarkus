package org.example.app.general.common.security;

import io.quarkus.runtime.configuration.ConfigUtils;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.util.Optional;

@Provider
public class AuthCookieFilter implements ContainerRequestFilter {

    @Inject
    SessionService sessionService;

    @Inject
    JwtService jwtService;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        if (requestContext.getUriInfo().getPath().equals("/auth/callback") || ConfigUtils.getProfiles().contains("test")) {
            return; // Skip filter for the callback or test mode
        }

        // Save the original URL (the endpoint the user originally requested)
        String originalUrl = requestContext.getHeaderString("Referer");

        String authRedirectUrl = jwtService.buildKeycloakAuthUrl();
        Cookie sessionCookie = requestContext.getCookies().get("SESSION_ID");

        if (!isValidSession(sessionCookie.getValue())) {
            sessionService.storeSession(sessionCookie.getValue(), "", originalUrl);
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{\"redirectUrl\": \"" + authRedirectUrl + "\"}")
                            .type(MediaType.APPLICATION_JSON)
                            .build()
            );
        }
    }

    private boolean isValidSession(String sessionId) {
        Optional<Session> session = sessionService.getSession(sessionId);
        return session.isPresent() && !session.get().getJwt().isEmpty();
    }
}
