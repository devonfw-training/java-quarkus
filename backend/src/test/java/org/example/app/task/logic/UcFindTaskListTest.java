package org.example.app.task.logic;

import org.example.app.task.common.TaskListCto;
import org.example.app.task.common.TaskListEto;
import org.example.app.task.dataaccess.TaskListEntity;
import org.example.app.task.dataaccess.TaskListRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class UcFindTaskListTest {
    private UcFindTaskList uc;
    private TaskListRepository repository;
    private TaskListMapper listMapper;
    private TaskItemMapper itemMapper;

    @BeforeEach
    void setUp() {
        repository = mock(TaskListRepository.class);
        listMapper = mock(TaskListMapper.class);
        itemMapper = mock(TaskItemMapper.class);

        uc = new UcFindTaskList();
        uc.taskListRepository = repository;
        uc.taskListMapper = listMapper;
        uc.taskItemMapper = itemMapper;
    }

    @Test
    void findById_shouldReturnMappedEto_whenEntityExists() {
        // Given
        Long id = 1L;
        TaskListEntity entity = new TaskListEntity();
        TaskListEto eto = new TaskListEto();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(listMapper.toEto(entity)).thenReturn(eto);

        // When
        TaskListEto result = uc.findById(id);

        // Then
        assertThat(result).isEqualTo(eto);
        verify(repository).findById(id);
        verify(listMapper).toEto(entity);
    }

    @Test
    void findById_shouldReturnNull_whenEntityDoesNotExist() {
        // Given
        Long id = 42L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        TaskListEto result = uc.findById(id);

        // Then
        assertThat(result).isNull();
        verify(repository).findById(id);
        verifyNoInteractions(listMapper);
    }

    @Test
    void findWithItems_shouldReturnMappedCto_whenEntityExists() {
        // Given
        Long id = 100L;
        TaskListEntity entity = new TaskListEntity();
        entity.setItems(Collections.emptyList());

        TaskListEto eto = new TaskListEto();

        when(repository.findById(id)).thenReturn(Optional.of(entity));
        when(listMapper.toEto(entity)).thenReturn(eto);
        when(itemMapper.toEtos(entity.getItems())).thenReturn(Collections.emptyList());

        // When
        TaskListCto result = uc.findWithItems(id);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getList()).isEqualTo(eto);
        assertThat(result.getItems()).isEmpty();

        verify(repository).findById(id);
        verify(listMapper).toEto(entity);
        verify(itemMapper).toEtos(entity.getItems());
    }

    @Test
    void findWithItems_shouldReturnNull_whenEntityDoesNotExist() {
        // Given
        Long id = 99L;
        when(repository.findById(id)).thenReturn(Optional.empty());

        // When
        TaskListCto result = uc.findWithItems(id);

        // Then
        assertThat(result).isNull();
        verify(repository).findById(id);
        verifyNoInteractions(listMapper, itemMapper);
    }
}
