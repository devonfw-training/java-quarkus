package org.example.app.task.logic;

import java.util.List;
import java.util.Optional;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import org.example.app.task.common.TaskListCto;
import org.example.app.task.common.TaskListEto;
import org.example.app.task.dataaccess.TaskListEntity;
import org.example.app.task.dataaccess.TaskListRepository;

/**
 * Use-Case to find {@link TaskListEntity task-lists}.
 */
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

  /**
   * @param listId the {@link TaskListEntity#getId() primary key} of the {@link TaskListEntity} to find.
   * @return the {@link TaskListEto} for the given {@link TaskListEto#getId() primary key} or {@code null} if not found.
   */
  // @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_FIND_TASK_LIST)
  public TaskListEto findById(Long listId) {

    Optional<TaskListEntity> taskList = this.taskListRepository.findById(listId);
    return taskList.map(taskListEntity -> this.taskListMapper.toEto(taskListEntity)).orElse(null);
  }

  /**
   * @param listId the {@link TaskListEntity#getId() primary key} of the {@link TaskListEntity} to find.
   * @return the {@link TaskListCto} for the given {@link TaskListEto#getId() primary key} or {@code null} if not found.
   */
  public TaskListCto findWithItems(Long listId) {

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

  public List<TaskListEto> findAll() {

    List<TaskListEntity> taskList = this.taskListRepository.findAll();
    return taskList.stream().map(taskListEntity -> this.taskListMapper.toEto(taskListEntity)).toList();
  }

}
