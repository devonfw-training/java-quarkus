package org.example.app.task.logic;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import io.quarkus.test.InjectMock;
import jakarta.inject.Inject;

import org.assertj.core.api.BDDAssertions;
import org.example.app.task.common.TaskItemEto;
import org.example.app.task.dataaccess.TaskItemEntity;
import org.example.app.task.dataaccess.TaskItemRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import io.quarkus.test.junit.QuarkusTest;

import java.util.List;

/**
 * Test of {@link UcAddRandomActivityTaskItem}
 */
@QuarkusTest
class UcAddRandomTaskItemTest {

  @Inject
  UcAddRandomActivityTaskItem ucAddRandomTaskItem;

  @InjectMock
  private ActivityService activityServiceMock;

  @InjectMock
  private TaskItemRepository taskItemRepositoryMock;

  @Test
  void testAddRandomTaskItemUseCase() {

    given(this.activityServiceMock.getRandomActivity()).willReturn("Water my plants");
    given(this.taskItemRepositoryMock.save(any())).willReturn(new TaskItemEntity());

    this.ucAddRandomTaskItem.addRandom(1L);

    ArgumentCaptor<TaskItemEntity> taskItemCaptor = ArgumentCaptor.forClass(TaskItemEntity.class);
    then(this.taskItemRepositoryMock).should().save(taskItemCaptor.capture());

    BDDAssertions.then(taskItemCaptor.getValue().getTitle()).isEqualTo("Water my plants");
  }

  @Test
  void testAddMultipleRandomTaskItemUseCase() {
    // Given
    TaskItemEto task1 = new TaskItemEto();
    task1.setTitle("Buy milk after work");
    TaskItemEto task2 = new TaskItemEto();
    task2.setTitle("Ride bike in the morning");
    List<TaskItemEto> taskItems = List.of(task1, task2);

    given(this.activityServiceMock.getMultipleRandomActivities("Spare time")).willReturn(taskItems);

    // When
    this.ucAddRandomTaskItem.addMultipleRandom(2L, "Spare time");

    // Then
    ArgumentCaptor<List<TaskItemEntity>> captor = ArgumentCaptor.forClass(List.class);
    then(this.taskItemRepositoryMock).should().saveAll(captor.capture());

    List<TaskItemEntity> capturedEntities = captor.getValue();
    BDDAssertions.then(capturedEntities).hasSize(2);
    BDDAssertions.then(capturedEntities.get(0).getTitle()).isEqualTo("Buy milk after work");
    BDDAssertions.then(capturedEntities.get(1).getTitle()).isEqualTo("Ride bike in the morning");
  }

  @Test
  void testAddExtractedIngredientsUseCase() {
    // Given
    String recipe = "Add eggs, flour and butter and mix everything.";
    TaskItemEto ingredient1 = new TaskItemEto();
    ingredient1.setTitle("eggs");
    TaskItemEto ingredient2 = new TaskItemEto();
    ingredient2.setTitle("flour");
    TaskItemEto ingredient3 = new TaskItemEto();
    ingredient3.setTitle("butter");
    List<TaskItemEto> ingredients = List.of(ingredient1, ingredient2, ingredient3);

    given(activityServiceMock.getExtractedIngredients(recipe)).willReturn(ingredients);

    // When
    ucAddRandomTaskItem.addExtractedIngredients(3L, recipe);

    // Then
    ArgumentCaptor<List<TaskItemEntity>> captor = ArgumentCaptor.forClass(List.class);
    then(taskItemRepositoryMock).should().saveAll(captor.capture());

    List<TaskItemEntity> capturedEntities = captor.getValue();
    BDDAssertions.then(capturedEntities).hasSize(3);
    BDDAssertions.then(capturedEntities.get(0).getTitle()).isEqualTo("eggs");
    BDDAssertions.then(capturedEntities.get(1).getTitle()).isEqualTo("flour");
    BDDAssertions.then(capturedEntities.get(2).getTitle()).isEqualTo("butter");
  }
}
