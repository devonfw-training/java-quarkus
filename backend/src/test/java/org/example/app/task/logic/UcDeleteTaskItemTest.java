package org.example.app.task.logic;

import jakarta.inject.Inject;

import org.assertj.core.api.Assertions;
import org.example.app.general.common.security.ApplicationAccessControlConfig;
import org.example.app.task.common.TaskItemEto;
import org.junit.jupiter.api.Test;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

/**
 * Test of {@link UcDeleteTaskItem}.
 */
@QuarkusTest
class UcDeleteTaskItemTest extends Assertions {

  @Inject
  UcSaveTaskItem ucSaveTaskItem;

  @Inject
  UcFindTaskItem ucFindTaskItem;

  @Inject
  UcDeleteTaskItem ucDeleteTaskItem;

  @Test
  @TestSecurity(user = "manager", roles = { ApplicationAccessControlConfig.PERMISSION_FIND_TASK_ITEM, ApplicationAccessControlConfig.PERMISSION_SAVE_TASK_ITEM,
  ApplicationAccessControlConfig.PERMISSION_DELETE_TASK_ITEM })
  void deleteTaskItem() {

    TaskItemEto item = new TaskItemEto();
    item.setTitle("To be deleted");
    item.setTaskListId(1L);
    Long id = this.ucSaveTaskItem.save(item).getId();

    this.ucDeleteTaskItem.delete(id);

    assertThat(this.ucFindTaskItem.findById(id)).isNull();
  }

  @Test
  @TestSecurity(user = "writer", roles = { ApplicationAccessControlConfig.PERMISSION_FIND_TASK_ITEM, ApplicationAccessControlConfig.PERMISSION_SAVE_TASK_ITEM })
  void deleteTaskItemIsForbiddenWithoutDeletePermission() {

    assertThatThrownBy(() -> this.ucDeleteTaskItem.delete(11L)).isInstanceOf(ForbiddenException.class);
  }

  @Test
  void deleteTaskItemIsRejectedForAnonymousUser() {

    assertThatThrownBy(() -> this.ucDeleteTaskItem.delete(11L)).isInstanceOf(UnauthorizedException.class);
  }

}
