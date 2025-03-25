package org.example.app.task.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskItemRepository extends JpaRepository<TaskItemEntity, Long> {

}