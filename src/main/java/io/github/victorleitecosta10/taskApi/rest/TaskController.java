package io.github.victorleitecosta10.taskApi.rest;

import io.github.victorleitecosta10.taskApi.enums.TaskStatusEnum;
import io.github.victorleitecosta10.taskApi.exceptions.TaskAlreadyExistsException;
import io.github.victorleitecosta10.taskApi.model.entity.Task;
import io.github.victorleitecosta10.taskApi.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService service;

    @GetMapping
    public List<Task> findAll(@RequestParam(required = false) TaskStatusEnum status) {
        if (status != null) return service.findByStatus(status);
        else return service.findAll();
    }

    @GetMapping("/{id}")
    public Task findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task save(@RequestBody @Valid Task task) throws TaskAlreadyExistsException {
        return service.save(task);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Task update(@RequestBody @Valid Task task, @PathVariable Long id) {
        return service.update(task, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
