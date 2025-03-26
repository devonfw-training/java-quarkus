package org.example.app.task.logic;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import org.example.app.task.dataaccess.TaskItemEntity;
import org.example.app.task.dataaccess.TaskItemRepository;

import java.util.Optional;

@ApplicationScoped
@Named
@Transactional
public class UcFindTaskItem {

    @Inject
    TaskItemRepository taskItemRepository;

    @Inject
    TaskItemMapper taskItemMapper;

    public TaskItemEto findById(Long itemId) {
        Optional<TaskItemEntity> item = this.taskItemRepository.findById(itemId);
        return item.map(ety -> this.taskItemMapper.toEto(ety)).orElse(null);
    }
}
