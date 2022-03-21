package io.github.victorleitecosta10.taskApi.advice;

import io.github.victorleitecosta10.taskApi.exceptions.ApiErrors;
import io.github.victorleitecosta10.taskApi.exceptions.TaskAlreadyDoneException;
import io.github.victorleitecosta10.taskApi.exceptions.TaskAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TaskControllerAdvice {

    @ExceptionHandler(TaskAlreadyDoneException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrors handleTaskAlreadyDoneException(TaskAlreadyDoneException ex) {
        return new ApiErrors(ex);
    }

    @ExceptionHandler(TaskAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiErrors handleTaskAlreadyExistsException(TaskAlreadyExistsException ex) {
        return new ApiErrors(ex);
    }

}
