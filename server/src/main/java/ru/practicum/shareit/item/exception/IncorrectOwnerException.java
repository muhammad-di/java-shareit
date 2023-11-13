package ru.practicum.shareit.item.exception;


import org.springframework.http.HttpStatus;

public class IncorrectOwnerException extends Exception {
    private HttpStatus errorCode;

    public IncorrectOwnerException(String message) {
        super(message);
    }

    public IncorrectOwnerException(String message, HttpStatus errorCode) {
        super(message);
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
