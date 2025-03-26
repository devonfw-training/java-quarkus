package org.example.app.task.logic;

import org.example.app.general.common.AbstractEto;
import org.example.app.task.common.TaskItem;
import org.example.app.task.dataaccess.TaskListEntity;

import java.time.LocalDateTime;

public class TaskItemEto extends AbstractEto implements TaskItem {

    private String title;

    private boolean completed;

    private boolean starred;

    private TaskListEntity taskListEntity;

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
