package org.example.app.task.service;

import org.example.app.task.common.TaskListEto;

/**
 * Helper for easily creating pre-populated {@link TaskListEto} instances following the Object Mother pattern.
 *
 */
final class TaskListMother {

  public static TaskListEto complete() {

    TaskListEto taskList = new TaskListEto();
    taskList.setId(123L);
    taskList.setVersion(1);
    taskList.setTitle("Shopping List");

    return taskList;
  }

  public static TaskListEto notYetSaved() {

    TaskListEto taskList = new TaskListEto();
    taskList.setTitle("Shopping List");

    return taskList;
  }
}
