package ru.practicum.shareit.user.exception;


import org.springframework.http.HttpStatus;

public class UserWithEmailAlreadyExists extends Exception {
    private HttpStatus errorCode;

    public UserWithEmailAlreadyExists(String message) {
        super(message);
    }

    public UserWithEmailAlreadyExists(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public UserWithEmailAlreadyExists(String message, Throwable cause, HttpStatus errorCode) {
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
