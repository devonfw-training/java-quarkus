package org.example.app.general.common.security;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Path("/auth")
public class AuthLogoutResource {


  @ConfigProperty(name = "quarkus.oidc.auth-server-url")
  String authServerUrl;

  @ConfigProperty(name = "app.frontend-client-id")
  String frontendClientId;

  @ConfigProperty(name = "app.logout-redirect")
  String appLogoutRedirect;

  @GET
  @Path("/logout")
  public Response logout(@QueryParam("idToken") String idToken) {
    if (idToken == null || idToken.isBlank()) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity("Missing idToken")
          .build();
    }

    String logoutUrl =
        authServerUrl +
            "/protocol/openid-connect/logout" +
            "?client_id=" + encode(frontendClientId) +
            "&id_token_hint=" + encode(idToken) +
            "&post_logout_redirect_uri=" + encode(appLogoutRedirect);

    return Response.status(Response.Status.FOUND)
        .header("Location", logoutUrl)
        .build();
  }

  private String encode(String value) {
    return URLEncoder.encode(value, StandardCharsets.UTF_8);
  }

}