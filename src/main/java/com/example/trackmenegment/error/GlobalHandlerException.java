package com.example.trackmenegment.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;


@RestControllerAdvice
public class GlobalHandlerException {

    private ResponseEntity<ExceptionResponse> responseResponseEntity(
            final Exception e,
            final WebRequest exchange, final HttpStatus status) {
        return new ResponseEntity<>(
                ExceptionResponse.builder()
                        .message(e.getMessage())
                        .occurred(LocalDateTime.now())
                        .status(status.value()).build(), status
        );
    }

    @ResponseStatus(FORBIDDEN)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionResponse> exceptionResponseResponseEntity(AccessDeniedException e
            , WebRequest exchange) {
        return responseResponseEntity(e, exchange, FORBIDDEN);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ExistsNameException.class)
    public ResponseEntity<ExceptionResponse> exceptionResponseResponseEntity(ExistsNameException e
            , WebRequest exchange) {
        return responseResponseEntity(e,
                exchange,
                FORBIDDEN);
    }

    @ResponseStatus(UNAUTHORIZED)
    @ExceptionHandler(HttpClientErrorException.Unauthorized.class)
    public ResponseEntity<ExceptionResponse> unauthorized(
            HttpClientErrorException.Unauthorized e, WebRequest exchange) {
        return responseResponseEntity(e, exchange, UNAUTHORIZED);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> badRequest(
            HttpMessageNotReadableException e, WebRequest exchange) {
        return responseResponseEntity(e, exchange, BAD_REQUEST);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ByIdException.class)
    public ResponseEntity<ExceptionResponse> byIdNot(
            ByIdException e, WebRequest exchange) {
        return responseResponseEntity(e, exchange, NOT_FOUND);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ExceptionResponse> client(
            ClientException e, WebRequest exchange) {
        return responseResponseEntity(e, exchange, NOT_FOUND);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(PhoneException.class)
    public ResponseEntity<ExceptionResponse> phone(
            PhoneException e, WebRequest exchange) {
        return responseResponseEntity(e, exchange, CONFLICT);
    }
}
