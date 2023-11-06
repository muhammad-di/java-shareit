package ru.practicum.shareit.item.exception;


import org.springframework.http.HttpStatus;

public class ItemNotFoundException extends Exception {
    private HttpStatus errorCode;

    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException(String message, HttpStatus errorCode) {
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
