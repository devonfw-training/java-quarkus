package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.example.app.task.dataaccess.TaskListRepository;

@ApplicationScoped
@Named
@Transactional
public class UcDeleteTaskList {

    @Inject
    TaskListRepository taskListRepository;


    public void deleteById(Long itemId) {
        this.taskListRepository.deleteById(itemId);
    }
}
