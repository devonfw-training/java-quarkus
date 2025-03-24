package org.example.app.task.dataaccess;

import org.example.app.general.dataaccess.ApplicationQueryFragment;
import org.example.app.task.common.TaskItemSearchCriteria;
import org.springframework.data.domain.Page;

import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * Implementation of {@link TaskItemQueryFragment}.
 */
public class TaskItemQueryFragmentImpl extends ApplicationQueryFragment implements TaskItemQueryFragment {

    @Override
    public Page<TaskItemEntity> find(TaskItemSearchCriteria criteria) {

        QTaskItemEntity taskItem = QTaskItemEntity.taskItemEntity;
        JPAQuery<TaskItemEntity> query = new JPAQuery<TaskItemEntity>(this.em).from(taskItem);
        where(query, taskItem.title, criteria.getTitle(), criteria.getTitleOptions());
        where(query, taskItem.completed, criteria.getCompleted());
        where(query, taskItem.starred, criteria.getStarred());
        orderBy(query, criteria.getSort(), name -> mapProperty(taskItem, name));
        return findPaginated(criteria, query);
    }

    private ComparableExpressionBase<?> mapProperty(QTaskItemEntity alias, String name) {

        switch (name) {
            case "title":
                return alias.title;
            case "completed":
                return alias.completed;
            case "starred":
                return alias.starred;
            default:
                return null;
        }
    }
}