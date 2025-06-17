package org.example.app.task.logic;

import org.example.app.task.common.TaskItemEto;
import org.example.app.task.dataaccess.TaskItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;

import static org.mockito.Mockito.*;

public class UcDeleteTaskItemTest {
    private TaskItemRepository repository;
    private UcDeleteTaskItem uc;
    private Logger testLogger;


    @BeforeEach
    void setup() {
        repository = mock(TaskItemRepository.class);
        uc = new UcDeleteTaskItem();
        uc.taskItemRepository = repository;

        // Inject mocked logger
        testLogger = mock(Logger.class);
        uc.log = testLogger;
    }

    @Test
    void delete_byId_shouldCallRepository() {
        Long id = 42L;

        uc.delete(id);

        verify(repository).deleteById(id);
    }

    @Test
    void delete_byEto_shouldCallRepository_whenIdIsPresent() {
        Long id = 77L;
        TaskItemEto eto = mock(TaskItemEto.class);
        when(eto.getId()).thenReturn(id);

        uc.delete(eto);

        verify(repository).deleteById(id);
    }

    @Test
    void delete_byEto_shouldLogButStillCallDelete_whenIdIsNull() {
        // Given
        TaskItemEto eto = mock(TaskItemEto.class);
        when(eto.getId()).thenReturn(null);
        when(eto.getTitle()).thenReturn("Untitled");

        // When
        uc.delete(eto);

        // Then
        verify(repository).deleteById(null);

        // Capture the logged message
        verify(testLogger).info(contains("TaskItem {} ist transient"), eq("Untitled"));
    }
}
