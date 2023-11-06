package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookingDtoForItemDtoTest {


    BookingDtoForItemDto bookingDto1;
    BookingDtoForItemDto bookingDtoCopy1;


    @BeforeEach
    public void create() {
        LocalDateTime testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
        LocalDateTime testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);

        User user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        User booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        Item item1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();
        Booking booking1 = Booking.builder()
                .id(1)
                .start(testStart)
                .end(testEnd)
                .booker(booker1)
                .item(item1)
                .status(Status.WAITING)
                .build();
        Booking bookingCopy1 = Booking.builder()
                .id(1)
                .start(testStart)
                .end(testEnd)
                .booker(booker1)
                .item(item1)
                .status(Status.WAITING)
                .build();
        bookingDto1 = BookingMapping.toBookingDtoForItemDto(booking1);
        bookingDtoCopy1 = BookingMapping.toBookingDtoForItemDto(bookingCopy1);

    }

    @Test
    void testEquals() throws Exception {
        assertEquals(bookingDto1, bookingDtoCopy1);
    }

    @Test
    void testHash() throws Exception {
        assertEquals(bookingDto1.hashCode(), bookingDtoCopy1.hashCode());
    }

    @Test
    void testToString() throws Exception {
        assertEquals(bookingDto1.toString(), bookingDtoCopy1.toString());
    }

    @Test
    void testGetters() throws Exception {
        assertThat(bookingDto1.getId(), equalTo(bookingDtoCopy1.getId()));
        assertThat(bookingDto1.getBookerId(), equalTo(bookingDtoCopy1.getBookerId()));
    }

    @Test
    void testSetters() throws Exception {
        bookingDto1.setId(2);
        bookingDtoCopy1.setId(2);
        bookingDto1.setBookerId(3);
        bookingDtoCopy1.setBookerId(3);

        assertThat(bookingDto1.getId(), equalTo(bookingDtoCopy1.getId()));
        assertThat(bookingDto1.getBookerId(), equalTo(bookingDtoCopy1.getBookerId()));
    }
}
