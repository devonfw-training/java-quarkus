package org.example.app.task.logic;

import jakarta.inject.Inject;

import org.assertj.core.api.Assertions;
import org.example.app.task.common.TaskListEto;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

/**
 * Test of {@link UcSaveTaskList}.
 */
@QuarkusTest
class UcSaveTaskListTest extends Assertions {

  @Inject
  UcFindTaskList ucFindTaskList;

  @Inject
  UcSaveTaskList ucSaveTaskList;

  /**
   * The client continues with the result of {@link UcSaveTaskList#save(TaskListEto)}, so it has to contain the new version. Otherwise, saving the same list
   * again fails with an optimistic locking exception.
   */
  @Test
  void saveTaskListReturnsNewVersionAndCanBeSavedAgain() {

    TaskListEto list = this.ucFindTaskList.findById(2L);
    int version = list.getVersion();
    String title = list.getTitle();
    list.setTitle(title + " changed");

    TaskListEto saved = this.ucSaveTaskList.save(list);
    assertThat(saved.getVersion()).isEqualTo(version + 1);

    // second change of the same list with the result of the first save (and restore of the original state)
    saved.setTitle(title);
    TaskListEto saved2 = this.ucSaveTaskList.save(saved);
    assertThat(saved2.getVersion()).isEqualTo(version + 2);
    assertThat(this.ucFindTaskList.findById(2L).getTitle()).isEqualTo(title);
  }

}
