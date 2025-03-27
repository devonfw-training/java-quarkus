package org.example.app.task.logic;

import org.example.app.task.dataaccess.TaskListEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "cdi")
public interface TaskListMapper {

    List<TaskListEto> toEtos(List<TaskListEntity> item);

    TaskListEto toEto(TaskListEntity item);

    TaskListEntity toEntity(TaskListEto item);
}
