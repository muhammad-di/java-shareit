package ru.practicum.shareit.booking.exception;


import org.springframework.http.HttpStatus;

public class BookingInFutureException extends Exception {
    private HttpStatus errorCode;

    public BookingInFutureException(String message) {
        super(message);
    }

    public BookingInFutureException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public BookingInFutureException(String message, Throwable cause, HttpStatus errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(HttpStatus errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return String.format("" +
                        "MASSAGE: %s; " +
                        "ERROR CODE: %s",
                super.getMessage(),
                errorCode
        );
    }
}
