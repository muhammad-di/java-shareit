package ru.practicum.shareit.booking.exception;


import org.springframework.http.HttpStatus;

public class ItemNotAvailableException extends Exception {
    private HttpStatus errorCode;

    public ItemNotAvailableException(String message) {
        super(message);
    }

    public ItemNotAvailableException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ItemNotAvailableException(String message, Throwable cause, HttpStatus errorCode) {
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
