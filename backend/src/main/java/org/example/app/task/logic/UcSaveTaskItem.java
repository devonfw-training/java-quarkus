package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

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
  public TaskItemEto save(TaskItemEto item) {

    TaskItemEntity entity = this.taskItemMapper.toEntity(item);
    // flush so that the version is incremented before it is returned (the client needs it to save the item again)
    entity = this.taskItemRepository.saveAndFlush(entity);
    return this.taskItemMapper.toEto(entity);
  }

}
