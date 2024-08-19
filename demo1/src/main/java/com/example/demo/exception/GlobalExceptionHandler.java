package com.example.demo.exception;

import jakarta.annotation.Nonnull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@Nonnull MethodArgumentNotValidException ex,
                                                                  @Nonnull HttpHeaders headers,
                                                                  @Nonnull HttpStatusCode status,
                                                                  @Nonnull WebRequest request) {

        StringBuilder message = new StringBuilder();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            message.append("Cause -> ").append(fieldError.getDefaultMessage()).append("\n");
        }
        ExceptionResponse response = new ExceptionResponse(status.toString(), message.toString());
        return new ResponseEntity<>(response, headers, status);
    }



    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ExceptionResponse> handle404(NotFoundException ex) {
        ExceptionResponse errorResponse = new ExceptionResponse("not_found_exception", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<ExceptionResponse> handle400(BadRequestException ex) {
        ExceptionResponse errorResponse = new ExceptionResponse("bad_request_exception", ex.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleGenericException(Exception ex) {
        ExceptionResponse errorResponse = new ExceptionResponse("internal_server_error", "An unexpected error occurred");
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
}
