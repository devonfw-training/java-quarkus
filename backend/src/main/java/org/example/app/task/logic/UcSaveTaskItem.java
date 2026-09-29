package org.example.app.task.logic;

import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import org.example.app.general.common.security.ApplicationAccessControlConfig;
import org.example.app.task.common.TaskItemEto;
import org.example.app.task.dataaccess.TaskItemEntity;
import org.example.app.task.dataaccess.TaskItemRepository;

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

  /**
   * @param item the {@link TaskItemEto} to save.
   * @return the {@link TaskItemEntity#getId() primary key} of the saved {@link TaskItemEntity}.
   */
  @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_TASK_ITEM)
  public TaskItemEto save(TaskItemEto item) {

    TaskItemEntity entity = this.taskItemMapper.toEntity(item);
    entity = this.taskItemRepository.save(entity);
    return this.taskItemMapper.toEto(entity);
  }

}
