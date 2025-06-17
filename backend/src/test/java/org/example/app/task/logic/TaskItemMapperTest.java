package org.example.app.task.logic;

import org.example.app.task.common.TaskItemEto;
import org.example.app.task.dataaccess.TaskItemEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

public class TaskItemMapperTest {
    @Test
    void toEtos_shouldReturnMappedList() {
        // Given
        TaskItemEntity entity1 = new TaskItemEntity();
        TaskItemEntity entity2 = new TaskItemEntity();
        TaskItemEto eto1 = new TaskItemEto();
        TaskItemEto eto2 = new TaskItemEto();

        TaskItemMapper mapper = Mockito.spy(TaskItemMapper.class);

        doReturn(eto1).when(mapper).toEto(entity1);
        doReturn(eto2).when(mapper).toEto(entity2);

        // When
        List<TaskItemEto> result = mapper.toEtos(List.of(entity1, entity2));

        // Then
        assertEquals(List.of(eto1, eto2), result);
        verify(mapper).toEto(entity1);
        verify(mapper).toEto(entity2);
    }
}
