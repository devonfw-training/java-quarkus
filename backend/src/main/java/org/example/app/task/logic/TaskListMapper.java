package org.example.app.task.logic;

import org.example.app.task.dataaccess.TaskListEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "cdi")
public interface TaskListMapper {

    TaskListEto toEto(TaskListEntity item);

    TaskListEntity toEntity(TaskListEto item);
}
