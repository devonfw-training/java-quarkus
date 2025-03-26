package org.example.app.task.logic;

import org.example.app.general.common.AbstractEto;
import org.example.app.task.common.TaskList;
import org.example.app.task.dataaccess.TaskItemEntity;

import java.util.List;

public class TaskListEto extends AbstractEto implements TaskList {

    private String title;

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
