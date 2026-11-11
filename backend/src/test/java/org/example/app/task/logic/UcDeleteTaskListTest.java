package org.example.app.task.logic;

import jakarta.inject.Inject;

import org.assertj.core.api.Assertions;
import org.example.app.general.common.security.ApplicationAccessControlConfig;
import org.example.app.task.common.TaskListEto;
import org.junit.jupiter.api.Test;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

/**
 * Test of {@link UcDeleteTaskList}.
 */
@QuarkusTest
class UcDeleteTaskListTest extends Assertions {

  @Inject
  UcSaveTaskList ucSaveTaskList;

  @Inject
  UcFindTaskList ucFindTaskList;

  @Inject
  UcDeleteTaskList ucDeleteTaskList;

  @Test
  @TestSecurity(user = "manager", roles = { ApplicationAccessControlConfig.PERMISSION_FIND_TASK_LIST, ApplicationAccessControlConfig.PERMISSION_SAVE_TASK_LIST,
  ApplicationAccessControlConfig.PERMISSION_DELETE_TASK_LIST })
  void deleteTaskList() {

    TaskListEto list = new TaskListEto();
    list.setTitle("To be deleted");
    Long id = this.ucSaveTaskList.save(list).getId();

    this.ucDeleteTaskList.delete(id);

    assertThat(this.ucFindTaskList.findById(id)).isNull();
  }

  @Test
  @TestSecurity(user = "item-manager", roles = { ApplicationAccessControlConfig.PERMISSION_DELETE_TASK_ITEM })
  void deleteTaskListIsForbiddenWithPermissionToDeleteItemsOnly() {

    assertThatThrownBy(() -> this.ucDeleteTaskList.delete(1L)).isInstanceOf(ForbiddenException.class);
  }

  @Test
  @TestSecurity(user = "writer", roles = { ApplicationAccessControlConfig.PERMISSION_FIND_TASK_LIST, ApplicationAccessControlConfig.PERMISSION_SAVE_TASK_LIST })
  void deleteTaskListIsForbiddenWithoutDeletePermission() {

    assertThatThrownBy(() -> this.ucDeleteTaskList.delete(1L)).isInstanceOf(ForbiddenException.class);
  }

  @Test
  void deleteTaskListIsRejectedForAnonymousUser() {

    assertThatThrownBy(() -> this.ucDeleteTaskList.delete(1L)).isInstanceOf(UnauthorizedException.class);
  }

}
