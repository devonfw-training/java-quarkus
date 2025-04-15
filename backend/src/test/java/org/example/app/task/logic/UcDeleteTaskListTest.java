package org.example.app.task.logic;

import org.example.app.task.common.TaskListEto;
import org.example.app.task.dataaccess.TaskListRepository;
import org.example.app.task.logic.UcDeleteTaskList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

public class UcDeleteTaskListTest {
    private TaskListRepository repository;
    private UcDeleteTaskList uc;
    private Logger testLogger;

    @BeforeEach
    void setUp() {
        repository = mock(TaskListRepository.class);
        uc = new UcDeleteTaskList();
        uc.taskListRepository = repository;

        // Inject a mocked logger to capture log calls
        testLogger = mock(Logger.class);
        uc.log = testLogger;
    }

    @Test
    void delete_byId_shouldCallRepository() {
        // Given
        Long id = 42L;

        // When
        uc.delete(id);

        // Then
        verify(repository).deleteById(id);
    }

    @Test
    void delete_byEto_shouldCallRepository_whenIdNotNull() {
        // Given
        TaskListEto eto = mock(TaskListEto.class);
        when(eto.getId()).thenReturn(1L);

        // When
        uc.delete(eto);

        // Then
        verify(repository).deleteById(1L);
        verifyNoInteractions(testLogger); // no logging expected
    }

    @Test
    void delete_byEto_shouldLog_whenIdIsNull() {
        // Given
        TaskListEto eto = mock(TaskListEto.class);
        when(eto.getId()).thenReturn(null);
        when(eto.getTitle()).thenReturn("Daily Tasks");

        // When
        uc.delete(eto);

        // Then
        verify(repository).deleteById(null);
        verify(testLogger).info(contains("TaskItem {} ist transient"), eq("Daily Tasks"));
    }
}
