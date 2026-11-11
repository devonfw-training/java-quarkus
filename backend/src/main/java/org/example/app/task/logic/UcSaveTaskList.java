package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import org.example.app.task.common.TaskListEto;
import org.example.app.task.dataaccess.TaskListEntity;
import org.example.app.task.dataaccess.TaskListRepository;

/**
 * Use-Case to save {@link org.example.app.task.common.TaskList}s.
 */
@ApplicationScoped
@Named
@Transactional
public class UcSaveTaskList {

  @Inject
  TaskListRepository taskListRepository;

  @Inject
  TaskListMapper taskListMapper;

  /**
   * @param list the {@link TaskListEto} to save.
   * @return the {@link TaskListEntity#getId() primary key} of the saved {@link TaskListEntity}.
   */
  public TaskListEto save(TaskListEto list) {

    TaskListEntity entity = this.taskListMapper.toEntity(list);
    // flush so that the version is incremented before it is returned (the client needs it to save the list again)
    entity = this.taskListRepository.saveAndFlush(entity);
    return this.taskListMapper.toEto(entity);
  }

}
