package org.example.app.task.logic;

import org.example.app.task.common.TaskListEto;
import org.example.app.task.dataaccess.TaskListEntity;
import org.mapstruct.Mapper;

/**
 * {@link Mapper} for {@link org.example.app.task.common.TaskList}.
 */
@Mapper(componentModel = "jakarta")
public interface TaskListMapper {

  /**
   * @param task the {@link TaskListEntity} to map.
   * @return the mapped {@link TaskListEto}.
   */
  TaskListEto toEto(TaskListEntity task);

  /**
   * @param task the {@link TaskListEto} to map.
   * @return the mapped {@link TaskListEntity}.
   */
  TaskListEntity toEntity(TaskListEto task);
}