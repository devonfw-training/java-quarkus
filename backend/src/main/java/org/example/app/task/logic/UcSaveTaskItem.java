package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.example.app.task.dataaccess.TaskItemEntity;
import org.example.app.task.dataaccess.TaskItemRepository;

@ApplicationScoped
@Named
@Transactional
public class UcSaveTaskItem {

    @Inject
    TaskItemRepository taskItemRepository;

    @Inject
    TaskItemMapper taskItemMapper;

    public void save(TaskItemEto taskItemEto) {
        TaskItemEntity taskItemEntity = this.taskItemMapper.toEntity(taskItemEto);
        this.taskItemRepository.save(taskItemEntity);
    }
}
