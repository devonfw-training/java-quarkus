package org.example.app.task.logic;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.example.app.task.common.TaskListEto;
import org.example.app.task.dataaccess.TaskListEntity;
import org.example.app.task.dataaccess.TaskListRepository;

/**
 * Use-Case to save {@link org.example.app.task.common.TaskList}s.
 */
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
  // @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_TASK_LIST)
  public Long save(TaskListEto list) {

    TaskListEntity entity = this.taskListMapper.toEntity(list);
    entity = this.taskListRepository.save(entity);
    return entity.getId();
  }

}