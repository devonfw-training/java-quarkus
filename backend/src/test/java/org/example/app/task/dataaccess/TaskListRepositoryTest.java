package org.example.app.task.dataaccess;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class TaskListRepositoryTest extends Assertions {

    @Inject
    private TaskListRepository taskListRepository;

    @Test
    public void testLoadAllTasklists() {
        // when
        List<TaskListEntity> items = this.taskListRepository.findAll();

        // then
        assertThat(items).isNotEmpty().hasSize(4);
    }

    @Test
    public void testFindCompletedLists() {
        // when
        List<TaskListEntity> items = this.taskListRepository.findByCompleted();

        // then
        assertThat(items).isNotEmpty();
    }




}