package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.practicum.shareit.item.exception.ItemNotFoundException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemNotFoundExceptionTest {


    private ItemNotFoundException exception;


    @BeforeEach
    public void create() {
        exception = new ItemNotFoundException("test message");
        exception.setErrorCode(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetErrorCode() {
        assertThat(exception.getErrorCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void testSetErrorCodeByConstractor() {
        ItemNotFoundException exception1 = new ItemNotFoundException("test message", HttpStatus.BAD_REQUEST);
        assertThat(exception1.getErrorCode(), equalTo(HttpStatus.BAD_REQUEST));
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
