package org.example.app.task.resource;

import java.util.Map;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Fetches access tokens for the test users of the realm (see {@code keycloak/quarkus-realm.json}) from the Keycloak that is started by Quarkus Dev Services.
 * <p>
 * The tokens are obtained via the "password" grant of the confidential client {@code backend-service}. This is only meant for tests: the frontend uses the
 * authorization code flow with PKCE and never sees the password of a user.
 */
public class KeycloakTokenProvider {

  private static final String TOKEN_URL = "http://localhost:8180/realms/quarkus/protocol/openid-connect/token";

  private static final String CLIENT_ID = "backend-service";

  /** Demo secret of the client {@code backend-service} as configured in the realm file. */
  private static final String CLIENT_SECRET = "secret";

  /** @return an access token of user {@code reader} (may only read). */
  public static String getAccessTokenOfReader() {

    return getAccessToken("reader", "reader");
  }

  /** @return an access token of user {@code writer} (may read and create/update). */
  public static String getAccessTokenOfWriter() {

    return getAccessToken("writer", "writer");
  }

  /** @return an access token of user {@code manager} (may read, create/update and delete). */
  public static String getAccessTokenOfManager() {

    return getAccessToken("manager", "manager");
  }

  /**
   * @param username the name of the user.
   * @param password the password of the user.
   * @return the access token (JWT) of the given user.
   */
  public static String getAccessToken(String username, String password) {

    Response response = RestAssured.given().contentType(ContentType.URLENC)
        .formParams(Map.of("grant_type", "password", "client_id", CLIENT_ID, "client_secret", CLIENT_SECRET, "username", username, "password", password))
        .post(TOKEN_URL);

    if (response.getStatusCode() != 200) {
      throw new IllegalStateException("Failed to get token for user " + username + ": " + response.getBody().asString());
    }
    return response.jsonPath().getString("access_token");
  }

}
