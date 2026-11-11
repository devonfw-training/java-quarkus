package org.example.app.task.logic;

import jakarta.inject.Inject;

import org.assertj.core.api.Assertions;
import org.example.app.general.common.security.ApplicationAccessControlConfig;
import org.junit.jupiter.api.Test;

import io.quarkus.security.ForbiddenException;
import io.quarkus.security.UnauthorizedException;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;

/**
 * Test of {@link UcFindTaskList}.
 */
@QuarkusTest
class UcFindTaskListTest extends Assertions {

  @Inject
  UcFindTaskList ucFindTaskList;

  @Test
  @TestSecurity(user = "reader", roles = { ApplicationAccessControlConfig.PERMISSION_FIND_TASK_LIST })
  void findAll() {

    assertThat(this.ucFindTaskList.findAll()).isNotEmpty();
  }

  @Test
  @TestSecurity(user = "reader", roles = { ApplicationAccessControlConfig.PERMISSION_FIND_TASK_LIST })
  void findWithItems() {

    assertThat(this.ucFindTaskList.findWithItems(1L).getItems()).isNotEmpty();
  }

  @Test
  @TestSecurity(user = "nobody", roles = { ApplicationAccessControlConfig.PERMISSION_FIND_TASK_ITEM })
  void findAllIsForbiddenWithoutFindTaskListPermission() {

    assertThatThrownBy(() -> this.ucFindTaskList.findAll()).isInstanceOf(ForbiddenException.class);
  }

  @Test
  void findAllIsRejectedForAnonymousUser() {

    assertThatThrownBy(() -> this.ucFindTaskList.findAll()).isInstanceOf(UnauthorizedException.class);
  }

}
