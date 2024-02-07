package org.example.app.boredapi.dataaccess;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.example.app.boredapi.common.ActivityTo;

import java.time.temporal.ChronoUnit;

/**
 * Provides services for retrieving activity suggestions.
 */
@ApplicationScoped
public class ActivityService {

  @Inject
  @RestClient
  BoredApi boredApi;

  /**
   * Get a random activity suggestion.
   *
   * @return the activity
   */
  @Fallback(fallbackMethod = "fallbackActivity")
  @Timeout(unit = ChronoUnit.SECONDS, value = 5)
  public ActivityTo getRandomActivity() {

    return this.boredApi.getRandomActivity();
  }

  /**
   * Fallback activity in case the suggestion service is down.
   *
   * @return the activity description
   */
  public ActivityTo fallbackActivity() {

    return new ActivityTo("Write more tests");
  }
}
