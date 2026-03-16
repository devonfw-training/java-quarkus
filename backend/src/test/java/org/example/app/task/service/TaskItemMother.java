package org.example.app.task.service;

import org.example.app.task.common.TaskItemEto;

/**
 * Helper for easily creating pre-populated {@link TaskItemEto} instances following the Object Mother pattern.
 *
 */
final class TaskItemMother {

  public static TaskItemEto complete() {

    TaskItemEto taskItem = new TaskItemEto();
    taskItem.setId(42L);
    taskItem.setVersion(1);
    taskItem.setTitle("Buy Eggs");
    taskItem.setCompleted(false);
    taskItem.setStarred(false);
    taskItem.setTaskListId(123L);

    return taskItem;
  }

  public static TaskItemEto notYetSaved() {

    TaskItemEto taskItem = new TaskItemEto();
    taskItem.setTitle("Buy Milk");
    taskItem.setTaskListId(123L);

    return taskItem;
  }
}
