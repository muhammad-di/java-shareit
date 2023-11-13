package ru.practicum.shareit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.item.exception.IncorrectBookerException;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorHandlerTest {

    private ErrorHandler errorHandler;
    private String message;
    private ErrorResponse response;

    @BeforeEach
    public void create() {
        errorHandler = new ErrorHandler();
        message = "Test message";
    }

    @Test
    void testHandleUserNotFoundException() {
        response = errorHandler.handleUserNotFoundException(new UserNotFoundException(message, HttpStatus.NOT_FOUND));
        assertEquals("MASSAGE: Test message; ERROR CODE: 404 NOT_FOUND", response.getError());

    }


    @Test
    void testHandleIncorrectOwnerException() {
        response = errorHandler.handleIncorrectOwnerException(new IncorrectOwnerException(message, HttpStatus.FORBIDDEN));
        assertEquals("MASSAGE: Test message; ERROR CODE: 403 FORBIDDEN", response.getError());
    }

    @Test
    void testHandleItemNotFoundException() {
        response = errorHandler.handleItemNotFoundException(new ItemNotFoundException(message, HttpStatus.NOT_FOUND));
        assertEquals("MASSAGE: Test message; ERROR CODE: 404 NOT_FOUND", response.getError());
    }

    @Test
    void testHandleThrowable() {
        response = errorHandler.handleThrowable(new Throwable(message));
        assertEquals("Test message", response.getError());
    }

    @Test
    void testHandleItemNotAvailableException() {
        ItemNotAvailableException exp = new ItemNotAvailableException(message);
        exp.setErrorCode(HttpStatus.BAD_REQUEST);
        response = errorHandler.handleItemNotAvailableException(exp);
        assertEquals("MASSAGE: Test message; ERROR CODE: 400 BAD_REQUEST", response.getError());
    }

    @Test
    void testHandleInvalidEndTimeException() {
        InvalidEndTimeException exp = new InvalidEndTimeException(message);
        response = errorHandler.handleInvalidEndTimeException(exp);
        assertEquals("MASSAGE: Test message; ERROR CODE: 400 BAD_REQUEST", response.getError());
    }

    @Test
    void testHandleInvalidStartTimeException() {
        InvalidStartTimeException exp = new InvalidStartTimeException(message);
        response = errorHandler.handleInvalidStartTimeException(exp);
        assertEquals("MASSAGE: Test message; ERROR CODE: 400 BAD_REQUEST", response.getError());
    }

    @Test
    void testHandleUnsupportedStateException() {
        UnsupportedStateException exp = new UnsupportedStateException(message);
        response = errorHandler.handleUnsupportedStateException(exp);
        assertEquals("Test message", response.getError());
    }

    @Test
    void testHandleBookingNotFoundException() {
        BookingNotFoundException exp = new BookingNotFoundException(message);
        response = errorHandler.handleBookingNotFoundException(exp);
        assertEquals("MASSAGE: Test message; ERROR CODE: 404 NOT_FOUND", response.getError());
    }

    @Test
    void testHandleInvalidBookerException() {
        InvalidBookerException exp = new InvalidBookerException(message);
        response = errorHandler.handleInvalidBookerException(exp);
        assertEquals("MASSAGE: Test message; ERROR CODE: 404 NOT_FOUND", response.getError());
    }

    @Test
    void testHandleIncorrectBookerException() {
        IncorrectBookerException exp = new IncorrectBookerException(message);
        response = errorHandler.handleIncorrectBookerException(exp);
        assertEquals("MASSAGE: Test message; ERROR CODE: 400 BAD_REQUEST", response.getError());
    }

    @Test
    void testHandleInvalidOwnerException() {
        InvalidOwnerException exp = new InvalidOwnerException(message);
        response = errorHandler.handleInvalidOwnerException(exp);
        assertEquals("MASSAGE: Test message; ERROR CODE: 404 NOT_FOUND", response.getError());
    }

    @Test
    void testHandleUserNotAllowedAccessBookingException() {
        UserNotAllowedAccessBookingException exp = new UserNotAllowedAccessBookingException(message);
        response = errorHandler.handleUserNotAllowedAccessBookingException(exp);
        assertEquals("MASSAGE: Test message; ERROR CODE: 404 NOT_FOUND", response.getError());
    }


    @Test
    void testHandleItemRequestNotFoundException() {
        ItemRequestNotFoundException exp = new ItemRequestNotFoundException(message);
        response = errorHandler.handleItemRequestNotFoundException(exp);
        assertEquals("MASSAGE: Test message; ERROR CODE: 404 NOT_FOUND", response.getError());
    }

    @Test
    void testHandleBookingAlreadyApprovedException() {
        BookingAlreadyApprovedException exp = new BookingAlreadyApprovedException(message);
        response = errorHandler.handleBookingAlreadyApprovedException(exp);
        assertEquals("MASSAGE: Test message; ERROR CODE: 400 BAD_REQUEST", response.getError());
    }
}
