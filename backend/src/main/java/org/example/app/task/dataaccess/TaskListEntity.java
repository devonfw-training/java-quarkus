package org.example.app.task.dataaccess;

import jakarta.persistence.*;
import org.example.app.general.dataaccess.ApplicationPersistenceEntity;
import org.example.app.task.common.TaskList;

import java.util.List;

@Entity
@Table(name = "task_list")
public class TaskListEntity extends ApplicationPersistenceEntity implements TaskList {

    @Column
    private String title;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "taskListEntity")
    List<TaskItemEntity> taskItemEntities;
    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the {@link List} of {@link TaskItemEntity task-items} in this task-list.
     */
    public List<TaskItemEntity> getItems() {

        return this.taskItemEntities;
    }

    /**
     * @param taskItems the new value of {@link #getItems()}.
     */
    public void setItems(List<TaskItemEntity> taskItems) {

        this.taskItemEntities = taskItems;
    }

    public List<TaskItemEntity> getTaskItemEntities() {
        return taskItemEntities;
    }

    public void setTaskItemEntities(List<TaskItemEntity> taskItemEntities) {
        this.taskItemEntities = taskItemEntities;
    }
}
