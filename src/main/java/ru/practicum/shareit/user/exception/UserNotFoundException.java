package ru.practicum.shareit.user.exception;


import org.springframework.http.HttpStatus;

public class UserNotFoundException extends Exception {
    private HttpStatus errorCode;

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public UserNotFoundException(String message, Throwable cause, HttpStatus errorCode) {
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
