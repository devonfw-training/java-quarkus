package org.example.app.task.dataaccess;

import org.example.app.task.common.TaskItemSearchCriteria;
import org.springframework.data.domain.Page;

/**
 * Interface for dynamic query methods to add to the repository via QueryDSL.
 */
public interface TaskItemQueryFragment {

    /**
     * @param criteria the {@link TaskItemSearchCriteria} specifying the search.
     * @return the {@link Page} with the found {@link TaskItemEntity}-hits.
     */
    Page<TaskItemEntity> find(TaskItemSearchCriteria criteria);

}