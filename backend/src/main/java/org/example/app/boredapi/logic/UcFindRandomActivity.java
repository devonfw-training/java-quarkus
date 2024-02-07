package org.example.app.boredapi.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.example.app.boredapi.common.ActivityTo;
import org.example.app.boredapi.dataaccess.ActivityService;

/**
 * Get a random activity from <a href="https://www.boredapi.com/">The Bored API</a>.
 *
 * @see <a href="https://www.boredapi.com/">The Bored API</a>
 */
@ApplicationScoped
@Named
public class UcFindRandomActivity {

  @Inject
  ActivityService activityService;

  /**
   * @return A random activity
   */
  public ActivityTo findRandomActivity() {

    return this.activityService.getRandomActivity();
  }

}
