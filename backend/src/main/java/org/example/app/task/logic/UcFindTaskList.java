package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.example.app.task.dataaccess.TaskListEntity;
import org.example.app.task.dataaccess.TaskListRepository;

import java.util.Optional;

@ApplicationScoped
@Named
@Transactional
public class UcFindTaskList {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskListMapper taskListMapper;

    public TaskListEto findById(Long itemId) {
        Optional<TaskListEntity> item = this.taskListRepository.findById(itemId);
        return item.map(ety -> this.taskListMapper.toEto(ety)).orElse(null);
    }
}
