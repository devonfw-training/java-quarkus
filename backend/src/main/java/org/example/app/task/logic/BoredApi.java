package org.example.app.task.logic;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

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
  Activity getRandomActivity();

  /**
   * A Bored API activity
   */
  static class Activity {

    public String activity;
  }
}
