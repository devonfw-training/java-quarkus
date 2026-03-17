package org.example.app.task.dataaccess;

import jakarta.persistence.*;
import org.example.app.general.dataaccess.ApplicationPersistenceEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "TASK_ITEM")
public class TaskItemEntity extends ApplicationPersistenceEntity {

  @Column(name = "TITLE", nullable = false)
  private String title;
  @Column(name = "COMPLETED")
  private boolean completed;
  @Column(name = "STARRED")
  private boolean starred;

  @Column(name = "DEADLINE")
  LocalDateTime deadline;

  @ManyToOne
  @JoinColumn(name = "LIST_ID")
  private TaskListEntity taskListEntity;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public boolean isCompleted() {
    return completed;
  }

  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  public boolean isStarred() {
    return starred;
  }

  public void setStarred(boolean starred) {
    this.starred = starred;
  }

  public LocalDateTime getDeadline() {
    return deadline;
  }

  public void setDeadline(LocalDateTime deadline) {
    this.deadline = deadline;
  }

  public TaskListEntity getTaskListEntity() {
    return taskListEntity;
  }

  public void setTaskListEntity(TaskListEntity taskListEntity) {
    this.taskListEntity = taskListEntity;
  }

  @Override
  public String toString() {
    return "TaskItemEntity{" +
        "title='" + title + '\'' +
        ", completed=" + completed +
        ", starred=" + starred +
        ", deadline=" + deadline +
        ", taskListEntity=" + taskListEntity +
        '}';
  }
}
