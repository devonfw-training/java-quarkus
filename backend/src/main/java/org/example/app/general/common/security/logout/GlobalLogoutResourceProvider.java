package org.example.app.general.common.security.logout;

import org.keycloak.models.KeycloakSession;
import org.keycloak.services.resource.RealmResourceProvider;

public class GlobalLogoutResourceProvider implements RealmResourceProvider {
  private final KeycloakSession session;

  public GlobalLogoutResourceProvider(KeycloakSession session) {
    this.session = session;
  }

  @Override
  public Object getResource() {
    return new GlobalLogoutEndpoint(session);
  }

  @Override
  public void close() {
    // NOOP
  }
}
