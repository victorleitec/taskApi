package io.github.victorleitecosta10.taskApi.rest;

import com.google.gson.Gson;
import io.github.victorleitecosta10.taskApi.config.TaskToJson;
import io.github.victorleitecosta10.taskApi.enums.TaskStatusEnum;
import io.github.victorleitecosta10.taskApi.exceptions.TaskAlreadyDoneException;
import io.github.victorleitecosta10.taskApi.exceptions.TaskAlreadyExistsException;
import io.github.victorleitecosta10.taskApi.model.entity.Task;
import io.github.victorleitecosta10.taskApi.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(TaskController.class)
@Import(TaskToJson.class)
class TaskControllerTest {

    @MockBean
    private TaskService service;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @Test
    public void findAll() throws Exception {
        // Setup our mocked service
        LocalDateTime dateTime = LocalDateTime.now();
        Task task1 = Task.builder().id(1l).name("Task 1").description("This is task 1").deadline(dateTime).status(TaskStatusEnum.DONE).build();
        Task task2 = Task.builder().id(2l).name("Task 2").description("This is task 2").deadline(dateTime).status(TaskStatusEnum.DOING).build();
        when(service.findAll()).thenReturn(asList(task1, task2));

        // Execute the GET request
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[*]", is(hasSize(2))))
                .andExpect(jsonPath("[0].id", is(task1.getId().intValue())))
                .andExpect(jsonPath("[0].name", is(task1.getName())))
                .andExpect(jsonPath("[0].description", is(task1.getDescription())))
                .andExpect(jsonPath("[0].deadline", is(task1.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("[0].status", is(task1.getStatus().toString())))
                .andExpect(jsonPath("[1].id", is(task2.getId().intValue())))
                .andExpect(jsonPath("[1].name", is(task2.getName())))
                .andExpect(jsonPath("[1].description", is(task2.getDescription())))
                .andExpect(jsonPath("[1].deadline", is(task2.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("[1].status", is(task2.getStatus().toString())));
    }

    @Test
    public void findById() throws Exception {
        // Setup our mocked service
        LocalDateTime firstDateTime = LocalDateTime.now();
        Task task1 = Task.builder().id(1l).name("Task 1").description("This is task 1").deadline(firstDateTime).status(TaskStatusEnum.DONE).build();
        when(service.findById(anyLong())).thenReturn(task1);

        // Execute the GET request
        mockMvc.perform(get("/tasks/{id}", 1l))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is(task1.getId().intValue())))
                .andExpect(jsonPath("name", is(task1.getName())))
                .andExpect(jsonPath("description", is(task1.getDescription())))
                .andExpect(jsonPath("deadline", is(task1.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("status", is(task1.getStatus().toString())));
    }

    @Test
    public void findByIdNotFound() throws Exception {
        // Setup our mocked service
        when(service.findById(anyLong())).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Execute the GET request
        mockMvc.perform(get("/tasks/{id}", 1l))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByStatus() throws Exception {
        // Setup our mocked service
        LocalDateTime firstDateTime = LocalDateTime.now();
        Task task1 = Task.builder().id(1l).name("Task 1").description("This is task 1").deadline(firstDateTime).status(TaskStatusEnum.DONE).build();
        when(service.findByStatus(TaskStatusEnum.DONE)).thenReturn(asList(task1));

        // Execute the GET request
        mockMvc.perform(get("/tasks?status=DONE"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("[0].id", is(task1.getId().intValue())))
                .andExpect(jsonPath("[0].name", is(task1.getName())))
                .andExpect(jsonPath("[0].description", is(task1.getDescription())))
                .andExpect(jsonPath("[0].deadline", is(task1.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("[0].status", is(task1.getStatus().toString())))
                .andExpect(jsonPath("[0].late").value(task1.isLate()));
        ;
    }

    @Test
    public void save() throws Exception {
        // Setup our mocked service
        LocalDateTime firstDateTime = LocalDateTime.now();
        Task task1 = Task.builder().id(1l).name("Task 1").description("This is task 1").deadline(firstDateTime).status(TaskStatusEnum.DONE).build();
        when(service.save(task1)).thenReturn(task1);

        // Execute the POST request
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(task1)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", is(task1.getId().intValue())))
                .andExpect(jsonPath("name", is(task1.getName())))
                .andExpect(jsonPath("description", is(task1.getDescription())))
                .andExpect(jsonPath("deadline", is(task1.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("status", is(task1.getStatus().toString())))
                .andExpect(jsonPath("late", is(task1.isLate())));
    }

    @Test
    public void saveAlreadyExists() throws Exception {
        LocalDateTime firstDateTime = LocalDateTime.now();
        Task task2 = Task.builder().id(2l).name("Task 1").description("This is task 1").deadline(firstDateTime).status(TaskStatusEnum.DONE).build();
        when(service.save(task2)).thenThrow(new TaskAlreadyExistsException("This task already exists"));

        // Execute the POST request
        mockMvc.perform(post("/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(task2)))
                .andExpect(result -> assertEquals("This task already exists", result.getResolvedException().getMessage()));
    }

    @Test
    public void update() throws Exception {
        // Setup our mocked service
        LocalDateTime firstDateTime = LocalDateTime.now();
        Task task1 = Task.builder().id(1l).name("Task 1").description("This is task 1").deadline(firstDateTime).status(TaskStatusEnum.DOING).build();
        Task taskUpdated = Task.builder().id(1l).name("Task 1").description("This is task 1").deadline(firstDateTime).status(TaskStatusEnum.DONE).build();

        when(service.update(any(Task.class), anyLong())).thenReturn(taskUpdated);

        // Execute the PUT request
        mockMvc.perform(put("/tasks/{id}", 1l)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(task1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id", is(taskUpdated.getId().intValue())))
                .andExpect(jsonPath("name", is(taskUpdated.getName())))
                .andExpect(jsonPath("description", is(taskUpdated.getDescription())))
                .andExpect(jsonPath("deadline", is(taskUpdated.getDeadline().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("status", is(taskUpdated.getStatus().toString())));
    }

    @Test
    public void updateTaskDone() throws Exception {
        // Setup our mocked service
        LocalDateTime firstDateTime = LocalDateTime.now();
        Task task1 = Task.builder().id(1l).name("Task 1").description("This is task 1").deadline(firstDateTime).status(TaskStatusEnum.DONE).build();

        when(service.update(task1, 1l)).thenThrow(new TaskAlreadyDoneException(CONFLICT, "This task already done, you cannot modify it!"));

        // Execute the PUT request
        mockMvc.perform(put("/tasks/{id}", 1l)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(task1)))
                .andExpect(result -> assertEquals("This task already done, you cannot modify it!", result.getResolvedException().getMessage()));
    }

    @Test
    public void delete() throws Exception {
        // Setup our mocked service
        LocalDateTime firstDateTime = LocalDateTime.now();
        Task task1 = Task.builder().id(1l).name("Task 1").description("This is task 1").deadline(firstDateTime).status(TaskStatusEnum.DONE).build();
        when(service.findById(1l)).thenReturn(task1);

        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tasks/{id}", 1l)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteAlreadyDone() throws Exception {
        // Setup our mocked service
        LocalDateTime firstDateTime = LocalDateTime.now();
        Task task1 = Task.builder().id(1l).name("Task 1").description("This is task 1").deadline(firstDateTime).status(TaskStatusEnum.DONE).build();
        doThrow(TaskAlreadyDoneException.class).when(service).delete(anyLong());

        // Execute the DELETE request
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/tasks/{id}", 1l)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(task1)))
                .andExpect(status().isConflict());
    }
}