package io.github.victorleitecosta10.taskApi.service;

import io.github.victorleitecosta10.taskApi.enums.TaskStatusEnum;
import io.github.victorleitecosta10.taskApi.exceptions.TaskAlreadyDoneException;
import io.github.victorleitecosta10.taskApi.exceptions.TaskAlreadyExistsException;
import io.github.victorleitecosta10.taskApi.model.entity.Task;
import io.github.victorleitecosta10.taskApi.model.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
public class TaskService {

    @Autowired
    private TaskRepository repository;

    public List<Task> findAll() {
        return repository.findAll();
    }

    public Task findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found!"));
    }

    public List<Task> findByStatus(TaskStatusEnum status) {
        return repository.findByStatus(status);
    }

    public Task save(@Valid Task task) throws TaskAlreadyExistsException {
        boolean exists = repository.existsByName(task.getName());
        if (exists) {
            throw new TaskAlreadyExistsException("This task already exists");
        }
        if (LocalDateTime.now().isBefore(task.getDeadline())) task.setLate(false);
        else task.setLate(true);
        return repository.save(task);
    }

    public Task update(Task updatedTask, Long id) {
        repository.findById(id).map(task -> {
            if (task.getStatus() != TaskStatusEnum.DONE) {
                updatedTask.setId(task.getId());
                task.setName(updatedTask.getName());
                task.setDescription(updatedTask.getDescription());
                task.setDeadline(updatedTask.getDeadline());
                task.setStatus(updatedTask.getStatus());
                if (now().isBefore(task.getDeadline())) task.setLate(false);
                else task.setLate(true);
                return repository.save(updatedTask);
            } else {
                throw new TaskAlreadyDoneException(CONFLICT, "This task already done, you cannot modify it!");
            }
        }).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found!"));
        return updatedTask;
    }

    public void delete(Long id) {
        Task task = repository.findById(id).orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Task not found!"));
        if (task.getStatus().equals(TaskStatusEnum.DONE)) {
            throw new TaskAlreadyDoneException(CONFLICT, "This task already done, you cannot delete it!");
        }
        repository.delete(task);

    }
}
