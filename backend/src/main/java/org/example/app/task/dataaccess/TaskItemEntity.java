package org.example.app.task.dataaccess;

import java.time.LocalDateTime;

import org.example.app.general.dataaccess.ApplicationPersistenceEntity;
import org.example.app.task.common.TaskItem;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * {@link TaskItem} implementation as {@link ApplicationPersistenceEntity}.
 */
@Entity
@Table(name = "TASK_ITEM")
public class TaskItemEntity extends ApplicationPersistenceEntity implements TaskItem {

  @Column(name = "TITLE")
  private String title;

  @Column(name = "COMPLETED")
  private boolean completed;

  @Column(name = "STARRED")
  private boolean starred;

  @Column(name = "DEADLINE")
  private LocalDateTime deadline;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "LIST_ID")
  private TaskListEntity taskList;

  /**
   * @return the {@link TaskListEntity} owning this task-item.
   */
  public TaskListEntity getTaskList() {

    return this.taskList;
  }

  /**
   * @param taskList the new value of {@link #getTaskList()}.
   */
  public void setTaskList(TaskListEntity taskList) {

    this.taskList = taskList;
  }

  @Override
  public Long getTaskListId() {

    if (this.taskList == null) {
      return null;
    }
    return this.taskList.getId();
  }

  @Override
  public void setTaskListId(Long taskListId) {

    if (taskListId == null) {
      this.taskList = null;
    } else {
      TaskListEntity taskListEntity = new TaskListEntity();
      taskListEntity.setId(taskListId);
      taskListEntity.setVersion(Integer.valueOf(0));
      this.taskList = taskListEntity;
    }
  }

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
  public LocalDateTime getDeadline() {

    return this.deadline;
  }

  @Override
  public void setDeadline(LocalDateTime deadline) {

    this.deadline = deadline;
  }

  @Override
  public String toString() {

    String result = this.title;
    Long id = getId();
    if (id == null) {
      result = result + '[' + id + ']';
    }
    return result;
  }

}
