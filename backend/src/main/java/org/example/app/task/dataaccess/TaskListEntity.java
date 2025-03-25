package org.example.app.task.dataaccess;

import jakarta.persistence.*;
import org.example.app.general.dataaccess.ApplicationPersistenceEntity;

import java.util.List;

@Entity
@Table(name = "task_list")
public class TaskListEntity extends ApplicationPersistenceEntity {

    @Column
    private String title;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taskListEntity")
    List<TaskItemEntity> taskItemEntities;

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
}
