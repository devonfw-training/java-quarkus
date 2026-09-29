package org.example.app.task.logic;

import static org.assertj.core.api.BDDAssertions.then;

import jakarta.inject.Inject;

import org.example.app.task.common.TaskItemEto;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

import java.util.List;

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

    String randomActivity = this.activityService.getRandomActivity();

    then(randomActivity).isEqualTo("Learn a new programming language");
  }

  @Test
  void shouldRetrieveMultipleRandomActivityOnSuccess() {

    List<TaskItemEto> randomActivities = this.activityService.getMultipleRandomActivities("Spare time");

    then(randomActivities).hasSize(2);
    then(randomActivities.get(0).getTitle()).isEqualTo("Read a new book");
    then(randomActivities.get(1).getTitle()).isEqualTo("Go for a walk");
  }

  @Test
  void shouldRetrieveListOfIngredientsOnSuccess() {

    List<TaskItemEto> randomActivities = this.activityService.getExtractedIngredients("Take flour, sugar and chocolate and mix everything.");

    then(randomActivities).hasSize(3);
    then(randomActivities.get(0).getTitle()).isEqualTo("flour");
    then(randomActivities.get(1).getTitle()).isEqualTo("sugar");
    then(randomActivities.get(2).getTitle()).isEqualTo("chocolate");
  }
}
