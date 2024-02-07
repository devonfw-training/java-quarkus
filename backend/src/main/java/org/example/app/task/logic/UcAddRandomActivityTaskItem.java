package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.example.app.boredapi.common.ActivityTo;
import org.example.app.boredapi.logic.UcFindRandomActivity;
import org.example.app.task.common.TaskItemEto;

/**
 * Use-Case to create a {@link org.example.app.task.common.TaskItem} with a random activity grabbed from
 * <a href="https://www.boredapi.com/">The Bored API</a>.
 *
 * @see <a href="https://www.boredapi.com/">The Bored API</a>
 */
@ApplicationScoped
@Named
@Transactional
public class UcAddRandomActivityTaskItem {

  @Inject
  UcFindRandomActivity ucFindRandomActivity;
  @Inject
  UcSaveTaskItem ucSaveTaskItem;

  /**
   * @param taskListId id the {@link org.example.app.task.common.TaskList#getId() primary key} of the
   *        {@link org.example.app.task.common.TaskList} for which to add a random task.
   * @return the {@link TaskItemEto#getId() primary key} of the newly added {@link TaskItemEto}.
   */
  // @RolesAllowed(ApplicationAccessControlConfig.PERMISSION_SAVE_TASK_ITEM)
  public Long addRandom(Long taskListId) {
    final ActivityTo randomActivity = ucFindRandomActivity.findRandomActivity();

    final TaskItemEto item = new TaskItemEto();
    item.setTaskListId(taskListId);
    item.setTitle(randomActivity.getActivity());
    item.setStarred(randomActivity.getAccessibility() > 0);

    return this.ucSaveTaskItem.save(item);
  }

}
