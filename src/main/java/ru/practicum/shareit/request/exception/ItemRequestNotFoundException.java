package ru.practicum.shareit.request.exception;


import org.springframework.http.HttpStatus;

public class ItemRequestNotFoundException extends Exception {
    private HttpStatus errorCode;

    public ItemRequestNotFoundException(String message) {
        super(message);
    }

    public ItemRequestNotFoundException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ItemRequestNotFoundException(String message, Throwable cause, HttpStatus errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ItemRequestNotFoundException() {

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
