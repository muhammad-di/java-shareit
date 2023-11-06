package ru.practicum.shareit.booking.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class BookingDtoTest {


    private UserDto bookerDto2;
    private BookingDto bookingDto1;
    private BookingDto bookingCopyDto1;
    LocalDateTime testStart;
    LocalDateTime testEnd;


    @BeforeEach
    public void create() {
        testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
        testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);

        User user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        User booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        User booker2 = User.builder().id(3).name("John2").email("john2.doe@mail.com").build();
        bookerDto2 = UserMapper.toUserDto(booker2);
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

        bookingDto1 = BookingMapping.toBookingDto(booking1);
        bookingCopyDto1 = BookingMapping.toBookingDto(bookingCopy1);
    }

    @Test
    void testEqual() throws Exception {
        assertThat(bookingDto1, equalTo(bookingCopyDto1));
    }

    @Test
    void testHash() throws Exception {
        assertThat(bookingDto1.hashCode(), equalTo(bookingCopyDto1.hashCode()));
    }

    @Test
    void testToString() throws Exception {
        assertThat(bookingDto1.toString(), equalTo(bookingCopyDto1.toString()));
    }

    @Test
    void testGetters() throws Exception {
        assertThat(bookingDto1.getId(), equalTo(bookingCopyDto1.getId()));
        assertThat(bookingDto1.getEnd(), equalTo(bookingCopyDto1.getEnd()));
        assertThat(bookingDto1.getStart(), equalTo(bookingCopyDto1.getStart()));
        assertThat(bookingDto1.getStatus(), equalTo(bookingCopyDto1.getStatus()));
        assertThat(bookingDto1.getBooker(), equalTo(bookingCopyDto1.getBooker()));
        assertThat(bookingDto1.getItem(), equalTo(bookingCopyDto1.getItem()));
    }

    @Test
    void testSetters() throws Exception {
        LocalDateTime s = testStart;
        LocalDateTime e = testEnd;
        s.plusHours(2);
        e.plusHours(2);

        bookingDto1.setId(2);
        bookingCopyDto1.setId(2);
        bookingDto1.setEnd(e);
        bookingCopyDto1.setEnd(e);
        bookingDto1.setStart(s);
        bookingCopyDto1.setStart(s);
        bookingDto1.setStatus(Status.APPROVED);
        bookingCopyDto1.setStatus(Status.APPROVED);
        bookingDto1.setBooker(bookerDto2);
        bookingCopyDto1.setBooker(bookerDto2);

        assertThat(bookingDto1.getId(), equalTo(bookingCopyDto1.getId()));
        assertThat(bookingDto1.getEnd(), equalTo(bookingCopyDto1.getEnd()));
        assertThat(bookingDto1.getStart(), equalTo(bookingCopyDto1.getStart()));
        assertThat(bookingDto1.getStatus(), equalTo(bookingCopyDto1.getStatus()));
        assertThat(bookingDto1.getBooker(), equalTo(bookingCopyDto1.getBooker()));
        assertThat(bookingDto1.getItem(), equalTo(bookingCopyDto1.getItem()));
    }


}
