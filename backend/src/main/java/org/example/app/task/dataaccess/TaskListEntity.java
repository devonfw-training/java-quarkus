package org.example.app.task.dataaccess;

import jakarta.persistence.*;
import org.example.app.general.dataaccess.ApplicationPersistenceEntity;

import java.util.List;

@Entity
@Table(name = "TASK_LIST")
public class TaskListEntity extends ApplicationPersistenceEntity {

  @Column(name = "TITLE", nullable = false)
  private String title;

  @OneToMany(mappedBy = "taskListEntity", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private List<TaskItemEntity> taskItemEntities;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public List<TaskItemEntity> getTaskItemEntities() {
    return taskItemEntities;
  }

  public void setTaskItemEntities(List<TaskItemEntity> taskItemEntities) {
    this.taskItemEntities = taskItemEntities;
  }

  @Override
  public String toString() {
    return "TaskListEntity{" +
        "title='" + title + '\'' +
        ", taskItemEntities=" + taskItemEntities +
        '}';
  }

}
