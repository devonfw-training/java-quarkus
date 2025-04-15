package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;

import org.example.app.task.common.TaskItemEto;
import org.example.app.task.dataaccess.TaskItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Use-Case to delete {@link org.example.app.task.common.TaskItem}s.
 */
@ApplicationScoped
@Named
@Transactional
public class UcDeleteTaskItem {

  private static final Logger STATIC_LOG = LoggerFactory.getLogger(UcDeleteTaskItem.class);

  Logger log = STATIC_LOG;

  @Inject
  TaskItemRepository taskItemRepository;

  /**
   * @param id the {@link org.example.app.task.dataaccess.TaskListEntity#getId() primary key} of the
   *        {@link org.example.app.task.dataaccess.TaskListEntity} to delete.
   */
  // @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_TASK_ITEM)
  public void delete(Long id) {

    this.taskItemRepository.deleteById(id);
  }

  /**
   * @param item the {@link TaskItemEto} to delete.
   */
  // @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_DELETE_TASK_ITEM)
  public void delete(TaskItemEto item) {

    Long id = item.getId();
    if (id == null) {
      log.info("TaskItem {} ist transient und kann nicht gelöscht werden", item.getTitle());
    }
    this.taskItemRepository.deleteById(id);
  }

}
