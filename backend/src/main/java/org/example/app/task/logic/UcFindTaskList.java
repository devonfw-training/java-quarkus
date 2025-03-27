package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.example.app.task.dataaccess.TaskListEntity;
import org.example.app.task.dataaccess.TaskListRepository;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
@Named
@Transactional
public class UcFindTaskList {

    @Inject
    TaskListRepository taskListRepository;

    @Inject
    TaskListMapper taskListMapper;
    @Inject
    TaskItemMapper taskItemMapper;


    public TaskListEto findById(Long itemId) {
        Optional<TaskListEntity> item = this.taskListRepository.findById(itemId);
        return item.map(ety -> this.taskListMapper.toEto(ety)).orElse(null);
    }

    public List<TaskListEto> findAll() {
        List<TaskListEntity> item = this.taskListRepository.findAll();
        return this.taskListMapper.toEtos(item);
    }



    public TaskListCto findTaskListWithItems(Long listId) {
        Optional<TaskListEntity> taskList = this.taskListRepository.findById(listId);
        if (taskList.isEmpty()) {
            return null;
        }
        TaskListCto cto = new TaskListCto();
        TaskListEntity taskListEntity = taskList.get();
        cto.setList(this.taskListMapper.toEto(taskListEntity));
        cto.setItems(this.taskItemMapper.toEtos(taskListEntity.getItems()));
        return cto;
    }
}
