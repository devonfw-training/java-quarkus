package org.example.app.task.dataaccess;

import static java.util.Arrays.asList;
import static org.assertj.core.api.BDDAssertions.then;

import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;

/**
 * Test of {@link TaskListRepository}
 *
 * <p>
 * Implementation note: we cannot used{@link Nested} tests because non-static inner classes are not CDI beans and thus
 * {@link TestTransaction} would not work. See <a href="https://github.com/quarkusio/quarkus/issues/20034">the issue in
 * Quarkus</a> and <a href="https://jakarta.ee/specifications/cdi/2.0/cdi-spec-2.0.html#what_classes_are_beans">the
 * relevant section in the CDI spec</a> .
 */
@QuarkusTest
@DisplayNameGeneration(ReplaceUnderscoresAndCamelCase.class)
@TestMethodOrder(MethodOrderer.MethodName.class) // only used for easier to rest test output
class TaskListRepositoryTest {

  @Inject
  TaskListRepository classUnderTest;

  @Inject
  TaskItemRepository taskItemRepository;

  @Inject
  EntityManager em;

  @Test
  @TestTransaction
  void GivenExistingTestData_WhenCounted_ThenSomeTaskListsAlreadyExist() {

    then(TaskListRepositoryTest.this.classUnderTest.count()).isGreaterThan(0);
  }

  @Test
  @TestTransaction
  void GivenANewEmptyTaskList_WhenItIsSaved_ThenNumberOfTestListShouldHaveIncreased() {

    long countBefore = TaskListRepositoryTest.this.classUnderTest.count();

    TaskListRepositoryTest.this.classUnderTest.saveAndFlush(aNewEmptyTaskList());

    then(TaskListRepositoryTest.this.classUnderTest.count()).isGreaterThan(countBefore);
  }

  @Test
  @TestTransaction
  void GivenANewEmptyTaskList_WhenItIsSaved_ThenListShouldBeFindableById() {

    TaskListEntity persistedTaskList = TaskListRepositoryTest.this.classUnderTest.saveAndFlush(aNewEmptyTaskList());

    then(TaskListRepositoryTest.this.classUnderTest.findById(persistedTaskList.getId())).isNotNull().isPresent();
  }

  @Test
  @TestTransaction
  void GivenANewTaskListWithItems_WhenItIsSaved_ThenNumberOfTestListShouldHaveIncreased() {

    long countBefore = TaskListRepositoryTest.this.classUnderTest.count();

    TaskListRepositoryTest.this.classUnderTest.saveAndFlush(aNewTaskListWithItems());

    then(TaskListRepositoryTest.this.classUnderTest.count()).isGreaterThan(countBefore);
  }

  @Test
  @TestTransaction
  void GivenANewTaskListWithItems_WhenItIsSaved_ThenListShouldBeFindableById() {

    TaskListEntity persistedTaskList = TaskListRepositoryTest.this.classUnderTest.saveAndFlush(aNewTaskListWithItems());

    then(TaskListRepositoryTest.this.classUnderTest.findById(persistedTaskList.getId())).isNotNull().isPresent();
  }

  @Test
  @TestTransaction
  void GivenANewTaskListWithItems_WhenItIsSaved_ThenListItemsShouldAllBeSavedAndReceivedIds() {

    TaskListEntity persistedTaskList = TaskListRepositoryTest.this.classUnderTest.saveAndFlush(aNewTaskListWithItems());

    Optional<TaskListEntity> foundTaskList = TaskListRepositoryTest.this.classUnderTest
        .findById(persistedTaskList.getId());
    then(foundTaskList).isNotNull().isPresent();
    then(foundTaskList.get().getItems()).hasSize(2).extracting(TaskItemEntity::getId).doesNotContainNull()
        .allMatch(id -> id > 0);
  }

  static final long EXISTING_TASK_LIST_ID = 1;

  @Test
  void GivenAnExistingTaskListWithItems_thenItShouldBeFindable() {

    then(TaskListRepositoryTest.this.classUnderTest.findById(EXISTING_TASK_LIST_ID)).isNotNull().isPresent();
  }

  @Test
  void GivenAnExistingTaskListWithItems_thenItShouldHaveTaskItems() {

    then(TaskListRepositoryTest.this.classUnderTest.findById(EXISTING_TASK_LIST_ID).get().getItems()).isNotEmpty();
  }

  @Test
  @TestTransaction
  void GivenAnExistingTaskListWithItems_WhenDeleted_ThenItShouldBeNotFindableById() {

    TaskListRepositoryTest.this.classUnderTest.deleteById(EXISTING_TASK_LIST_ID);

    flushAndClear();

    then(TaskListRepositoryTest.this.classUnderTest.findById(EXISTING_TASK_LIST_ID)).isEmpty();
  }

  @Test
  @TestTransaction
  void GivenAnExistingTaskListWithItems_WhenDeleted_TthenItsItemsShouldBeDeletedAsWell() {

    var idsOfTaskItems = TaskListRepositoryTest.this.classUnderTest.findById(EXISTING_TASK_LIST_ID).get().getItems()
        .stream().map(TaskItemEntity::getId).collect(Collectors.toList());

    TaskListRepositoryTest.this.classUnderTest.deleteById(EXISTING_TASK_LIST_ID);

    flushAndClear();

    idsOfTaskItems.forEach(id -> then(TaskListRepositoryTest.this.taskItemRepository.findById(id)).isEmpty());
  }

  TaskListEntity aNewEmptyTaskList() {

    TaskListEntity entity = new TaskListEntity();
    entity.setTitle("Test List");
    return entity;
  }

  TaskListEntity aNewTaskListWithItems() {

    TaskListEntity entity = new TaskListEntity();
    entity.setTitle("Test List");

    TaskItemEntity taskItem1 = new TaskItemEntity();
    taskItem1.setTitle("Task 1");
    taskItem1.setTaskList(entity);

    TaskItemEntity taskItem2 = new TaskItemEntity();
    taskItem2.setTitle("Task 2");
    taskItem2.setTaskList(entity);

    entity.setItems(asList(taskItem1, taskItem2));

    return entity;
  }

  /**
   * Used to make sure that any queries will be forced to reach the database and not use any caches.
   */
  private void flushAndClear() {

    TaskListRepositoryTest.this.classUnderTest.flush();
    TaskListRepositoryTest.this.em.clear();
  }

}
