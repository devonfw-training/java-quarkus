package org.example.app.task.logic;

import java.time.LocalDateTime;

import jakarta.inject.Inject;

import org.assertj.core.api.Assertions;
import org.example.app.task.common.TaskItemEto;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Test of {@link UcSaveTaskItem}.
 */
@QuarkusTest
class UcSaveTaskItemTest extends Assertions {

  @Inject
  UcFindTaskItem ucFindTaskItem;

  @Inject
  UcSaveTaskItem ucSaveTaskItem;

  @Test
  void saveTaskItem() {

    TaskItemEto item = this.ucFindTaskItem.findById(11L);
    int version = item.getVersion();
    boolean starred = item.isStarred();
    // save with a change (the version is only incremented if the entity has been modified)
    item.setStarred(!starred);
    this.ucSaveTaskItem.save(item);
    TaskItemEto item2 = this.ucFindTaskItem.findById(11L);
    assertThat(item2.getVersion()).isEqualTo(version + 1);
    assertThat(item2.isStarred()).isEqualTo(!starred);
    // test that relation from item to list is still present and not lost.
    assertThat(item2.getTaskListId()).isEqualTo(item.getTaskListId()).isEqualTo(1L);
    // restore the original state for the other tests
    item2.setStarred(starred);
    this.ucSaveTaskItem.save(item2);
  }

  /**
   * The client continues with the result of {@link UcSaveTaskItem#save(TaskItemEto)}, so it has to contain the new version. Otherwise, saving the same item
   * again fails with an optimistic locking exception.
   */
  @Test
  void saveTaskItemReturnsNewVersionAndCanBeSavedAgain() {

    TaskItemEto item = this.ucFindTaskItem.findById(12L);
    int version = item.getVersion();
    boolean starred = item.isStarred();
    item.setStarred(!starred);

    TaskItemEto saved = this.ucSaveTaskItem.save(item);
    assertThat(saved.getVersion()).isEqualTo(version + 1);

    // second change of the same item with the result of the first save (and restore of the original state)
    saved.setStarred(starred);
    TaskItemEto saved2 = this.ucSaveTaskItem.save(saved);
    assertThat(saved2.getVersion()).isEqualTo(version + 2);
  }

  /**
   * A deadline of {@code 00:00:00.001} marks a date without a time (see {@link org.example.app.task.common.TaskItem#getDeadline()}), so the millisecond has to
   * survive the database, and it has to be different from a real time at midnight.
   */
  @Test
  void saveTaskItemKeepsMillisecondOfDeadline() {

    LocalDateTime dateWithoutTime = LocalDateTime.of(2026, 11, 23, 0, 0, 0, 1_000_000);
    LocalDateTime midnight = LocalDateTime.of(2026, 11, 23, 0, 0);
    TaskItemEto item = this.ucFindTaskItem.findById(13L);
    assertThat(item.getDeadline()).isNull();

    item.setDeadline(dateWithoutTime);
    this.ucSaveTaskItem.save(item);
    TaskItemEto item2 = this.ucFindTaskItem.findById(13L);
    assertThat(item2.getDeadline()).isEqualTo(dateWithoutTime);

    item2.setDeadline(midnight);
    this.ucSaveTaskItem.save(item2);
    TaskItemEto item3 = this.ucFindTaskItem.findById(13L);
    assertThat(item3.getDeadline()).isEqualTo(midnight).isNotEqualTo(dateWithoutTime);

    // restore the original state for the other tests
    item3.setDeadline(null);
    this.ucSaveTaskItem.save(item3);
  }

}
