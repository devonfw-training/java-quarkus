package org.example.app.boredapi.dataaccess;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.example.app.boredapi.common.ActivityTo;

/**
 * Client to access <a href="https://www.boredapi.com/">The Bored API</a>.
 */
@Path("/activity")
@RegisterRestClient(configKey = "bored-api")
public interface BoredApi {

  /**
   * Fetch a random activity.
   *
   * @return an activity
   */
  @GET
  ActivityTo getRandomActivity();
}
