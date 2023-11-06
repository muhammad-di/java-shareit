package ru.practicum.shareit.request.exception;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RequestExceptionTest {


    private Item item1;
    private Booking booking1;
    private LocalDateTime testStart;
    private LocalDateTime testEnd;


    @BeforeEach
    public void create() {
        testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
        testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);

        User user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        User booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        item1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();
        booking1 = Booking.builder()
                .id(1)
                .start(testStart)
                .end(testEnd)
                .booker(booker1)
                .item(item1)
                .status(Status.WAITING)
                .build();
    }

    @Test
    void validateEndBeforeCurrentTimeShouldThrowInvalidEndTimeException() throws Exception {
        booking1.setEnd(testStart);
        booking1.setStart(testEnd);

        InvalidEndTimeException exception = Assertions.assertThrows(
                InvalidEndTimeException.class,
                () -> BookingValidation.validate(booking1)
        );


        assertEquals("MASSAGE: end time is before current time; ERROR CODE: null", exception.getErrorMessage());
    }

    @Test
    void validateForApproveShouldThrowInvalidOwnerException() throws Exception {


        InvalidOwnerException exception = Assertions.assertThrows(
                InvalidOwnerException.class,
                () -> BookingValidation.validateForApprove(booking1, 99)
        );

        assertEquals("MASSAGE: a wrong owner exception; ERROR CODE: null", exception.getErrorMessage());
    }


    @Test
    void validateStartBeforeCurrentTimeShouldThrowInvalidStartTimeException() throws Exception {
        booking1.setStart(testStart.minusMonths(10));
        booking1.setEnd(LocalDateTime.now().plusHours(2));

        InvalidStartTimeException exception = Assertions.assertThrows(
                InvalidStartTimeException.class,
                () -> BookingValidation.validate(booking1)
        );

        assertEquals("MASSAGE: start time is before current time; ERROR CODE: null", exception.getErrorMessage());
    }


    @Test
    void validateItemAvailabilityShouldThrowInvalidStartTimeException() throws Exception {
        booking1.setStart(LocalDateTime.now().plusHours(1));
        booking1.setEnd(LocalDateTime.now().plusHours(2));
        item1.setAvailable(false);

        ItemNotAvailableException exception = Assertions.assertThrows(
                ItemNotAvailableException.class,
                () -> BookingValidation.validate(booking1)
        );

        assertEquals("MASSAGE: an item with id { 1 } is not available; ERROR CODE: null", exception.getErrorMessage());
    }


    @Test
    void validateStateForBookerShouldThrowInvalidStartTimeException() throws Exception {
        UnsupportedStateException exception = Assertions.assertThrows(
                UnsupportedStateException.class,
                () -> BookingValidation.validateStateForBooker("NotAll")
        );

        assertEquals("MASSAGE: Unknown state: NotAll; ERROR CODE: null", exception.getErrorMessage());
    }


    @Test
    void validateForFindByIdShouldThrowInvalidStartTimeException() throws Exception {
        booking1.setStart(LocalDateTime.now().plusHours(1));
        booking1.setEnd(LocalDateTime.now().plusHours(2));

        UserNotAllowedAccessBookingException exception = Assertions.assertThrows(
                UserNotAllowedAccessBookingException.class,
                () -> BookingValidation.validateForFindById(booking1, 66)
        );

        assertEquals("MASSAGE: a user with id { 66 } can not access this booking; ERROR CODE: null", exception.getErrorMessage());
    }
}
