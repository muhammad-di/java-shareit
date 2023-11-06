package ru.practicum.shareit.item.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class IncorrectOwnerExceptionTest {


    private IncorrectOwnerException exception;


    @BeforeEach
    public void create() {
        exception = new IncorrectOwnerException("test message");
        exception.setErrorCode(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetErrorCode() {
        assertThat(exception.getErrorCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void testSetErrorCode() {
        exception.setErrorCode(HttpStatus.BAD_REQUEST);
        assertThat(exception.getErrorCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void testGetErrorMessage() {
        assertThat(exception.getErrorMessage(), equalTo("MASSAGE: test message; ERROR CODE: 404 NOT_FOUND"));
    }


}
