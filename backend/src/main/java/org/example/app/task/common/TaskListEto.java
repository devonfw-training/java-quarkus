package org.example.app.task.common;

import jakarta.validation.constraints.NotBlank;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.example.app.general.common.AbstractEto;

/**
 * {@link TaskList} implementation as {@link AbstractEto}.
 */
@Schema(name = "TaskList", description = "Object that represents a task list")
public class TaskListEto extends AbstractEto implements TaskList {

  @NotBlank(message = "A task list must always have a title")
  @Schema(required = true, example = "Shopping list", description = "Title of the task list")
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