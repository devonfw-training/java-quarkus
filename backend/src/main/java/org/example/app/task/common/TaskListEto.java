package org.example.app.task.common;

import org.example.app.general.common.AbstractEto;

/**
 * {@link TaskList} implementation as {@link AbstractEto}.
 */
public class TaskListEto extends AbstractEto implements TaskList {

  private String title;

  @Override
  public String getTitle() {

    return this.title;
  }

  @Override
  public void setTitle(String title) {

    this.title = title;
  }

}
