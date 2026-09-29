package org.example.app.task.common;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.example.app.general.common.AbstractEto;

/**
 * {@link TaskItem} implementation as {@link AbstractEto}.
 */
@Schema(name = "TaskItem", description = "Object that represents a task item")
public class TaskItemEto extends AbstractEto implements TaskItem {

  @NotBlank(message = "A task item must always have a title")
  @Schema(required = true, example = "Buy eggs", description = "The task title or description")
  private String title;

  @Schema(required = false, example = "false", description = "Whether or not the task is completed")
  private boolean completed;

  @Schema(required = false, example = "false", description = "Whether or not the task has been starred")
  private boolean starred;

  @Min(value = 1, message = "A task item must always be associated with a task list")
  @NotNull(message = "A task item must always be associated with a task list")
  @Schema(required = true, example = "1", description = "The id of the task list to which this item belongs")
  private Long taskListId;

  @Schema(required = false, description = "Until when the task must be completed")
  private LocalDateTime deadline;

  @Override
  public String getTitle() {

    return this.title;
  }

  @Override
  public void setTitle(String title) {

    this.title = title;
  }

  @Override
  public boolean isCompleted() {

    return this.completed;
  }

  @Override
  public void setCompleted(boolean completed) {

    this.completed = completed;
  }

  @Override
  public boolean isStarred() {

    return this.starred;
  }

  @Override
  public void setStarred(boolean starred) {

    this.starred = starred;
  }

  @Override
  public Long getTaskListId() {

    return this.taskListId;
  }

  @Override
  public void setTaskListId(Long taskListId) {

    this.taskListId = taskListId;
  }

  @Override
  public LocalDateTime getDeadline() {

    return this.deadline;
  }

  @Override
  public void setDeadline(LocalDateTime deadline) {

    this.deadline = deadline;
  }
}