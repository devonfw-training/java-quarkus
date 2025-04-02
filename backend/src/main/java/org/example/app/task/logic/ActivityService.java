package org.example.app.task.logic;

import java.time.temporal.ChronoUnit;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

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
   * @return the activity description
   */
  @Fallback(fallbackMethod = "fallbackActivity")
  @Timeout(unit = ChronoUnit.SECONDS, value = 5)
  public String getRandomActivity() {

    return this.boredApi.getRandomActivity().activity;
  }

  /**
   * Fallback activity in case the suggestion service is down.
   *
   * @return the activity description
   */
  public String fallbackActivity() {

    return "Write more tests";
  }
}
