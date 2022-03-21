package io.github.victorleitecosta10.taskApi.model.repository;

import io.github.victorleitecosta10.taskApi.enums.TaskStatusEnum;
import io.github.victorleitecosta10.taskApi.model.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByStatus(TaskStatusEnum status);

    boolean existsByName(String name);
}
