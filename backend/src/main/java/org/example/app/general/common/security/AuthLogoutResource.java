package org.example.app.general.common.security;

import java.util.Map;

import io.quarkus.security.Authenticated;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;

/**
 * Rest service for the logout of the current user.
 * <p>
 * The backend is stateless: it only validates the bearer token of every request and does not hold a session. Logging out therefore means ending the session
 * of the user in Keycloak (single sign-out). This resource builds the URL of the Keycloak end-session endpoint (RP-initiated logout) to which the frontend has
 * to send the browser. The access token that has already been issued stays valid until it expires, which is why the realm configures a short lifespan for
 * access tokens.
 */
@Path("/auth")
public class AuthLogoutResource {

  @ConfigProperty(name = "app.keycloak.public-url")
  String keycloakPublicUrl;

  @ConfigProperty(name = "app.frontend-client-id")
  String frontendClientId;

  @ConfigProperty(name = "app.logout-redirect")
  String logoutRedirect;

  /**
   * @param request the JSON body with the optional {@code idToken} of the user. It is sent in the body instead of the URL so that it does not end up in
   *     access logs, the browser history or referrer headers.
   * @return a JSON object with the {@code logoutUrl} to which the browser has to be redirected.
   */
  @POST
  @Path("/logout")
  @Authenticated
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response logout(Map<String, String> request) {

    UriBuilder logoutUrl = UriBuilder.fromUri(this.keycloakPublicUrl).path("protocol/openid-connect/logout")
        .queryParam("client_id", this.frontendClientId).queryParam("post_logout_redirect_uri", this.logoutRedirect);
    String idToken = (request == null) ? null : request.get("idToken");
    if ((idToken != null) && !idToken.isBlank()) {
      logoutUrl.queryParam("id_token_hint", idToken);
    }
    return Response.ok(Map.of("logoutUrl", logoutUrl.build().toString())).build();
  }

}
