package org.example.app.task.logic;

import org.assertj.core.api.Assertions;
import org.example.app.task.common.TaskItemEto;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;

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
    // save without any change
    this.ucSaveTaskItem.save(item);
    TaskItemEto item2 = this.ucFindTaskItem.findById(11L);
    assertThat(item2.getVersion()).isEqualTo(version + 1);
    // test that relation from item to list is still present and not lost.
    assertThat(item2.getTaskListId()).isEqualTo(item.getTaskListId()).isEqualTo(1L);
  }

}
