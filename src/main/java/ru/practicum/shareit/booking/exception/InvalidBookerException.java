package ru.practicum.shareit.booking.exception;


import org.springframework.http.HttpStatus;

public class InvalidBookerException extends Exception {
    private HttpStatus errorCode;

    public InvalidBookerException(String message) {
        super(message);
    }

    public InvalidBookerException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidBookerException(String message, Throwable cause, HttpStatus errorCode) {
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
