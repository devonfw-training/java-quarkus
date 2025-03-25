package org.example.app.task.dataaccess;

import jakarta.persistence.*;
import org.example.app.general.dataaccess.ApplicationPersistenceEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "task_item")
public class TaskItemEntity extends ApplicationPersistenceEntity {

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
}
