package io.github.victorleitecosta10.taskApi.exceptions;

import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class ApiErrors {

    private List<String> errors;

    public ApiErrors(BindingResult bindingResult) {
        this.errors = new ArrayList<>();
        bindingResult.getAllErrors().forEach(errors -> this.errors.add(errors.getDefaultMessage()));
    }

    public ApiErrors(TaskAlreadyDoneException ex) {
        this.errors = asList(ex.getMessage());
    }

    public ApiErrors(TaskAlreadyExistsException ex) {
        this.errors = asList(ex.getMessage());
    }

    public List<String> getErrors() {
        return errors;
    }
}
