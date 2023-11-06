package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookingDto {


    private User userCopy1;
    private User booker2;
    private Booking booking1;
    private Booking bookingCopy1;
    LocalDateTime testStart;
    LocalDateTime testEnd;


    @BeforeEach
    public void create() {
         testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
         testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);

        User user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        userCopy1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        User booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        booker2 = User.builder().id(3).name("John2").email("john2.doe@mail.com").build();
        Item item1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();
        booking1 = Booking.builder()
                .id(1)
                .start(testStart)
                .end(testEnd)
                .booker(booker1)
                .item(item1)
                .status(Status.WAITING)
                .build();
        bookingCopy1 = Booking.builder()
                .id(1)
                .start(testStart)
                .end(testEnd)
                .booker(booker1)
                .item(item1)
                .status(Status.WAITING)
                .build();
    }

    @Test
    void testEqual() throws Exception {
        assertThat(booking1, equalTo(bookingCopy1));
    }

    @Test
    void testHash() throws Exception {
        assertThat(booking1.hashCode(), equalTo(bookingCopy1.hashCode()));
    }

    @Test
    void testToString() throws Exception {
        assertThat(booking1.toString(), equalTo(bookingCopy1.toString()));
    }

    @Test
    void testGetters() throws Exception {
        assertThat(booking1.getId(), equalTo(bookingCopy1.getId()));
        assertThat(booking1.getEnd(), equalTo(bookingCopy1.getEnd()));
        assertThat(booking1.getStart(), equalTo(bookingCopy1.getStart()));
        assertThat(booking1.getStatus(), equalTo(bookingCopy1.getStatus()));
        assertThat(booking1.getBooker(), equalTo(bookingCopy1.getBooker()));

    }

    @Test
    void testSetters() throws Exception {
        LocalDateTime s = testStart;
        LocalDateTime e = testEnd;
        s.plusHours(2);
        e.plusHours(2);

        booking1.setId(2);
        bookingCopy1.setId(2);
        booking1.setEnd(e);
        bookingCopy1.setEnd(e);
        booking1.setStart(s);
        bookingCopy1.setStart(s);
        booking1.setStatus(Status.APPROVED);
        bookingCopy1.setStatus(Status.APPROVED);
        booking1.setBooker(booker2);
        bookingCopy1.setBooker(booker2);

        assertThat(booking1.getId(), equalTo(bookingCopy1.getId()));
        assertThat(booking1.getEnd(), equalTo(bookingCopy1.getEnd()));
        assertThat(booking1.getStart(), equalTo(bookingCopy1.getStart()));
        assertThat(booking1.getStatus(), equalTo(bookingCopy1.getStatus()));
        assertThat(booking1.getBooker(), equalTo(bookingCopy1.getBooker()));
    }


}
