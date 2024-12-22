package com.libraryapi.exception;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class ValidationException extends RuntimeException {


    private final List<FieldError> fieldErrors;

    public ValidationException(FieldError fieldError) {
        super("Validation Error");
        this.fieldErrors = Collections.singletonList(fieldError);
    }

    public ValidationException(List<FieldError> fieldErrors) {
        super("Validation Error");
        this.fieldErrors = fieldErrors;
    }

    public String getMessage() {
        return (String) this.fieldErrors.stream().map(DefaultMessageSourceResolvable::getDefaultMessage).collect(Collectors.joining());
    }

    public List<FieldError> getFieldErrors() {
        return Collections.unmodifiableList(this.fieldErrors);
    }


}
