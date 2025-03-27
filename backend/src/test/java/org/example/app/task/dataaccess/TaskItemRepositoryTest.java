package org.example.app.task.dataaccess;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class TaskItemRepositoryTest extends Assertions {

    @Inject
    private TaskItemRepository taskItemRepository;

    @Test
    public void testFindById() {

        // given
        Long itemId = 11L;

        // when
        TaskItemEntity item = this.taskItemRepository.findById(itemId).get();

        // then
        assertThat(item.getTitle()).isEqualTo("Milk");
    }

    @Test
    public void testSave() {

        // given
        TaskItemEntity entity = new TaskItemEntity();
        entity.setTitle("title");
        entity.setVersion(0);

        // when
        TaskItemEntity savedItem = this.taskItemRepository.save(entity);

        // then
        assertThat(savedItem.getId()).isEqualTo(1000000L);
        assertThat(savedItem.getVersion()).isNotNull();
    }

}