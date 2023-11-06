package ru.practicum.shareit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        e.setErrorCode(HttpStatus.NOT_FOUND);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleIncorrectOwnerException(final IncorrectOwnerException e) {
        e.setErrorCode(HttpStatus.FORBIDDEN);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFoundException(final ItemNotFoundException e) {
        e.setErrorCode(HttpStatus.NOT_FOUND);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemNotAvailableException(final ItemNotAvailableException e) {
        e.setErrorCode(HttpStatus.BAD_REQUEST);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidEndTimeException(final InvalidEndTimeException e) {
        e.setErrorCode(HttpStatus.BAD_REQUEST);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidStartTimeException(final InvalidStartTimeException e) {
        e.setErrorCode(HttpStatus.BAD_REQUEST);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnsupportedStateException(final UnsupportedStateException e) {
        e.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFoundException(final BookingNotFoundException e) {
        e.setErrorCode(HttpStatus.NOT_FOUND);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingAlreadyApprovedException(final BookingAlreadyApprovedException e) {
        e.setErrorCode(HttpStatus.BAD_REQUEST);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvalidBookerException(final InvalidBookerException e) {
        e.setErrorCode(HttpStatus.NOT_FOUND);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIncorrectBookerException(final IncorrectBookerException e) {
        e.setErrorCode(HttpStatus.BAD_REQUEST);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleInvalidOwnerException(final InvalidOwnerException e) {
        e.setErrorCode(HttpStatus.NOT_FOUND);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotAllowedAccessBookingException(final UserNotAllowedAccessBookingException e) {
        e.setErrorCode(HttpStatus.NOT_FOUND);
        return new ErrorResponse(e.getErrorMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemRequestNotFoundException(final ItemRequestNotFoundException e) {
        e.setErrorCode(HttpStatus.NOT_FOUND);
        return new ErrorResponse(e.getErrorMessage());
    }


}
