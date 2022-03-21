package io.github.victorleitecosta10.taskApi.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskAlreadyExistsException extends RuntimeException {
    public TaskAlreadyExistsException(String errorMessage) {
        super(errorMessage);
    }
}
