package com.mercan.lottery.exception;

import com.mercan.lottery.dto.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.OptimisticLockException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Component
@RestControllerAdvice
public class WebExceptionHandler {

    @ExceptionHandler({TicketNotFoundException.class, TicketCheckedException.class})
    public ResponseEntity<ApiError> handleNotFounds(Exception e) {
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        ApiError build = ApiError.builder()
                .reasonCode(HttpStatus.NOT_FOUND.name())
                .errors(errors)
                .build();
        return new ResponseEntity<>(build, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ApiError> handleMissingParameter(MissingServletRequestParameterException missingServletRequestParameterException) {
        List<String> errors = new ArrayList<>();
        errors.add(missingServletRequestParameterException.getMessage());
        ApiError build = ApiError.builder()
                .reasonCode(HttpStatus.BAD_REQUEST.name())
                .errors(errors)
                .build();
        return new ResponseEntity<>(build, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<ApiError> handleConstraint(ConstraintViolationException constraintViolationException) {
        List<String> errors = new ArrayList<>();
        for (ConstraintViolation violation : constraintViolationException.getConstraintViolations()) {
            errors.add(violation.getMessage() + " Input value:{" + violation.getInvalidValue() + "}");
        }
        ApiError build = ApiError.builder()
                .reasonCode(HttpStatus.BAD_REQUEST.name())
                .errors(errors)
                .build();
        return new ResponseEntity<>(build, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({OptimisticLockException.class})
    public ResponseEntity<ApiError> handleOptimisticLock(OptimisticLockException optimisticLockException) {
        List<String> errors = new ArrayList<>();
        errors.add(optimisticLockException.getMessage());
        ApiError build = ApiError.builder()
                .reasonCode(HttpStatus.CONFLICT.name())
                .errors(errors)
                .build();
        return new ResponseEntity<>(build, HttpStatus.CONFLICT);
    }

}
