package org.example.app.boredapi.dataaccess;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.example.app.boredapi.common.ActivityTo;
import org.example.app.task.logic.UcAddRandomActivityTaskItem;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * Test of {@link UcAddRandomActivityTaskItem}
 */
@QuarkusTest
@QuarkusTestResource(WireMockTestResource.class)
class ActivityServiceTest {

  @Inject
  ActivityService activityService;

  @Test
  void shouldRetrieveRandomActivityOnSuccess() {

    ActivityTo randomActivity = this.activityService.getRandomActivity();

    then(randomActivity.getActivity()).isEqualTo("Learn a new programming language");
  }
}
