package org.example.app.task.logic;

import io.quarkus.test.Mock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.assertj.core.api.BDDAssertions;
import org.example.app.boredapi.common.ActivityTo;
import org.example.app.boredapi.logic.UcFindRandomActivity;
import org.example.app.task.common.TaskItemEto;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

/**
 * Test of {@link UcAddRandomActivityTaskItem}
 */
@QuarkusTest
class UcAddRandomTaskItemTest {

  @Inject
  UcAddRandomActivityTaskItem ucAddRandomTaskItem;

  @Mock
  UcFindRandomActivity ucFindRandomActivity;

  @Mock
  UcSaveTaskItem ucSaveTaskItem;

  @Test
  void testAddRandomTaskItemUseCase() {

    given(this.ucFindRandomActivity.findRandomActivity()).willReturn(new ActivityTo("Water my plants"));
    given(this.ucSaveTaskItem.save(any())).willReturn(42L);

    this.ucAddRandomTaskItem.addRandom(1L);

    ArgumentCaptor<TaskItemEto> taskItemCaptor = ArgumentCaptor.forClass(TaskItemEto.class);
    then(this.ucSaveTaskItem).should().save(taskItemCaptor.capture());

    BDDAssertions.then(taskItemCaptor.getValue().getTitle()).isEqualTo("Water my plants");
  }
}
