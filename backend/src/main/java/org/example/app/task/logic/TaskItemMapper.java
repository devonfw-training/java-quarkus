package org.example.app.task.logic;

import org.example.app.task.dataaccess.TaskItemEntity;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "cdi")
public interface TaskItemMapper {

    default List<TaskItemEto> toEtos(List<TaskItemEntity> items) {
        List<TaskItemEto> result = new ArrayList<>();
        for (TaskItemEntity item : items) {
            result.add(toEto(item));
        }

        return result;
    }

    TaskItemEto toEto(TaskItemEntity item);

    TaskItemEntity toEntity(TaskItemEto item);
}
