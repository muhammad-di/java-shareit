package ru.practicum.shareit.item.exception;


import org.springframework.http.HttpStatus;

public class InvalidRequestHeaderException extends Exception {
    private HttpStatus errorCode;

    public InvalidRequestHeaderException(String message) {
        super(message);
    }

    public InvalidRequestHeaderException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public InvalidRequestHeaderException(String message, Throwable cause, HttpStatus errorCode) {
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
