package org.example.app.task.dataaccess;

import jakarta.persistence.*;
import org.example.app.general.dataaccess.ApplicationPersistenceEntity;
import org.example.app.task.common.TaskItem;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_item")
public class TaskItemEntity extends ApplicationPersistenceEntity implements TaskItem {

    @Column
    private String title;

    @Column
    private boolean completed;

    @Column
    private boolean starred;

    @JoinColumn(name = "LIST_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private TaskListEntity taskListEntity;

    @Column
    private LocalDateTime deadline;
    @Override
    public String getTitle() {
        return title;
    }
    @Override
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public boolean isCompleted() {
        return completed;
    }
    @Override
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    @Override
    public boolean isStarred() {
        return starred;
    }
    @Override
    public void setStarred(boolean starred) {
        this.starred = starred;
    }
    @Override
    public LocalDateTime getDeadline() {
        return deadline;
    }
    @Override
    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }

    @Override
    public Long getTaskListId() {

        if (this.taskListEntity == null) {
            return null;
        }
        return this.taskListEntity.getId();
    }

    @Override
    public void setTaskListId(Long taskListId) {

        if (taskListId == null) {
            this.taskListEntity = null;
        } else {
            TaskListEntity taskListEntity = new TaskListEntity();
            taskListEntity.setId(taskListId);
            taskListEntity.setVersion(Integer.valueOf(0));
            this.taskListEntity = taskListEntity;
        }
    }

    public TaskListEntity getTaskListEntity() {
        return taskListEntity;
    }

    public void setTaskListEntity(TaskListEntity taskListEntity) {
        this.taskListEntity = taskListEntity;
    }
}
