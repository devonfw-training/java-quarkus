package org.example.app.task.logic;

import org.example.app.task.common.TaskListEto;
import org.example.app.task.dataaccess.TaskListEntity;
import org.example.app.task.dataaccess.TaskListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UcSaveTaskListTest {

    private UcSaveTaskList uc;
    private TaskListRepository repository;
    private TaskListMapper mapper;

    @BeforeEach
    void setUp() {
        repository = mock(TaskListRepository.class);
        mapper = mock(TaskListMapper.class);

        uc = new UcSaveTaskList();
        uc.taskListRepository = repository;
        uc.taskListMapper = mapper;
    }

    @Test
    void save_shouldConvertAndReturnGeneratedId() {
        // Given
        TaskListEto eto = new TaskListEto();
        TaskListEntity entity = new TaskListEntity();
        TaskListEntity savedEntity = new TaskListEntity();
        savedEntity.setId(123L);

        when(mapper.toEntity(eto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(savedEntity);

        // When
        Long result = uc.save(eto);

        // Then
        assertThat(result).isEqualTo(123L);
        verify(mapper).toEntity(eto);
        verify(repository).save(entity);
    }
}