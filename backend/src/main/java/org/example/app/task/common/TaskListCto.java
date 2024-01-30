package org.example.app.task.common;

import java.util.List;

/**
 * Composite transfer object for a {@link TaskListEto} with its containing {@link TaskItemEto}s.
 */
public class TaskListCto {

  private TaskListEto list;

  private List<TaskItemEto> items;

  /**
   * The constructor.
   */
  public TaskListCto() {

    super();
  }

  /**
   * @return the {@link TaskListEto}.
   */
  public TaskListEto getList() {

    return this.list;
  }

  /**
   * @param taskListEto new value of {@link #getList()}.
   */
  public void setList(TaskListEto taskListEto) {

    this.list = taskListEto;
  }

  /**
   * @return the {@link List} of {@link TaskItemEto items} contained in the #get.
   */
  public List<TaskItemEto> getItems() {

    return this.items;
  }

  /**
   * @param items new value of {@link #getItems()}.
   */
  public void setItems(List<TaskItemEto> items) {

    this.items = items;
  }

}