package org.example.app.task.logic;

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

}
