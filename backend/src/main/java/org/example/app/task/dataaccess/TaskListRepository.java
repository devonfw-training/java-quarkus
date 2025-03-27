package org.example.app.task.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskListRepository extends JpaRepository<TaskListEntity, Long> {

    @Query("SELECT list FROM TaskListEntity list JOIN list.taskItemEntities items WHERE items.completed = true")
    List<TaskListEntity> findByCompleted();

}