package ru.practicum.shareit.booking.exception;


import org.springframework.http.HttpStatus;

public class InvalidStartTimeException extends Exception {
    private HttpStatus errorCode;

    public InvalidStartTimeException(String message) {
        super(message);
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
