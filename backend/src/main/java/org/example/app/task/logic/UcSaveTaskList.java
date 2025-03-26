package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.example.app.task.dataaccess.TaskListEntity;
import org.example.app.task.dataaccess.TaskListRepository;

@ApplicationScoped
@Named
@Transactional
public class UcSaveTaskList {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskListMapper taskListMapper;

    public void save(TaskListEto taskListEto) {
        TaskListEntity taskListEntity = this.taskListMapper.toEntity(taskListEto);
        this.taskListRepository.save(taskListEntity);
    }
}
