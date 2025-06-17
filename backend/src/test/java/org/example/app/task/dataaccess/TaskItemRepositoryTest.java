package org.example.app.task.dataaccess;

import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.example.app.general.common.search.LikePatternSyntax;
import org.example.app.general.common.search.SortOrderBy;
import org.example.app.general.common.search.StringSearchOptions;
import org.example.app.task.common.TaskItemSearchCriteria;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

/**
 * Test of {@link TaskItemRepository}.
 */
@QuarkusTest
class TaskItemRepositoryTest extends Assertions {

  @Inject
  TaskItemRepository taskItemRepository;

  @Test
  void testFindById() {

    // given
    Long itemId = 11L;

    // when
    TaskItemEntity item = this.taskItemRepository.findById(itemId).get();

    // then
    assertThat(item.getTitle()).isEqualTo("Milk");
  }

  @Test
  void testFindByFlags() {

    // given
    boolean completed = false;
    boolean starred = true;

    // when
    List<TaskItemEntity> hits = this.taskItemRepository.findByFlags(completed, starred);

    assertThat(hits.stream().map(i -> i.getTitle())).contains("Milk", "Butter", "Bread", "Jigsaw", "Sunscreen",
        "Wetsuit", "Swimsuit", "Surfboard", "Flip-flops", "Diamond ring");
  }

  @Test
  void testFindByDeadline() {

    // given
    LocalDateTime deadline = LocalDateTime.of(2022, 12, 22, 23, 59);

    // when
    List<TaskItemEntity> hits = this.taskItemRepository.findByDeadline(deadline);

    // then
    assertThat(hits.stream().map(i -> i.getTitle())).contains("Diamond ring", "Lingerie");
  }

  @Test
  void testFindByCriteria() {

    // given
    TaskItemSearchCriteria criteria = new TaskItemSearchCriteria();
    criteria.setCompleted(Boolean.TRUE);
    criteria.setTitle("*e*");
    criteria.setTitleOptions(StringSearchOptions.of(LikePatternSyntax.GLOB));
    criteria.getSort().add(SortOrderBy.ofAsc("title"));

    // when
    Page<TaskItemEntity> page = this.taskItemRepository.find(criteria);

    // then
    assertThat(page).isNotNull();
    assertThat(page.getContent().stream().map(i -> i.getTitle())).containsExactly("Lingerie", "Super-Glue");
  }

}
