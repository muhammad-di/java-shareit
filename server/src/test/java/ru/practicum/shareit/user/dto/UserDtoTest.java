package ru.practicum.shareit.user.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class UserDtoTest {


    private Item item1;
    private ItemDto itemDto1;
    private User user1;
    private User userCopy1;
    private UserDto userDto1;
    private UserDto userCopyDto1;
    private User booker1;
    private UserDto bookerDto1;
    private Booking booking1;
    private BookingDto bookingDto1;
    private LocalDateTime testStart;
    private LocalDateTime testEnd;


    @BeforeEach
    public void create() {
        testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
        testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);

        user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        userCopy1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        userDto1 = UserMapper.toUserDto(user1);
        userCopyDto1 = UserMapper.toUserDto(userCopy1);
        booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        bookerDto1 = UserMapper.toUserDto(booker1);
        item1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();
        itemDto1 = ItemMapper.toItemDto(item1);
        booking1 = Booking.builder()
                .id(1)
                .start(testStart)
                .end(testEnd)
                .booker(booker1)
                .item(item1)
                .status(Status.WAITING)
                .build();
        bookingDto1 = BookingMapping.toBookingDto(booking1);
    }

    @Test
    void testUserEqual() throws Exception {
        assertThat(userDto1, equalTo(userCopyDto1));
    }

    @Test
    void testUserHash() throws Exception {
        assertThat(userDto1.hashCode(), equalTo(userCopyDto1.hashCode()));
    }

    @Test
    void testUserToString() throws Exception {
        assertThat(userDto1.toString(), equalTo(userCopyDto1.toString()));
    }

    @Test
    void testUserGetters() throws Exception {
        assertThat(userDto1.getId(), equalTo(userCopyDto1.getId()));
        assertThat(userDto1.getEmail(), equalTo(userCopyDto1.getEmail()));
        assertThat(userDto1.getName(), equalTo(userCopyDto1.getName()));
    }
}
