package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import org.example.app.task.common.TaskItemEto;
import org.example.app.task.dataaccess.TaskItemEntity;
import org.example.app.task.dataaccess.TaskItemRepository;
import org.example.app.task.dataaccess.TaskListRepository;

/**
 * Use-Case to save {@link org.example.app.task.common.TaskItem}s.
 */
@ApplicationScoped
@Named
@Transactional
public class UcSaveTaskItem {

  @Inject
  TaskItemRepository taskItemRepository;

  @Inject
  TaskItemMapper taskItemMapper;

  @Inject
  TaskListRepository taskListRepository;

  /**
   * @param item the {@link TaskItemEto} to save.
   * @return the {@link TaskItemEntity#getId() primary key} of the saved {@link TaskItemEntity}.
   */
  // @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_TASK_ITEM)
  public TaskItemEntity save(TaskItemEto item) {

    TaskItemEntity entity = this.taskItemMapper.toEntity(item);
    entity = this.taskItemRepository.save(entity);
    return entity;
  }

}
