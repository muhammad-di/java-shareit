package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.BookingController;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.BookingAlreadyApprovedException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.booking.exception.InvalidBookerException;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingService bookingService;

    @Autowired
    private MockMvc mvc;

    private User user1;
    private User booker1;
    private Booking booking1;
    private BookingDto bookingDto1;


    @BeforeEach
    public void create() {
        LocalDateTime testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
        LocalDateTime testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);

        user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        UserDto userDto1 = UserMapper.toUserDto(user1);
        booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        UserDto bookerDto1 = UserMapper.toUserDto(booker1);
        Item item1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();
        ItemDto itemDto1 = ItemMapper.toItemDto(item1);
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
    public void createShouldReturnNewUser() throws Exception {
        when(bookingService.save(any(Booking.class), anyLong()))
                .thenReturn(booking1);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booker1.getId())
                        .content(mapper.writeValueAsString(bookingDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))));

    }

    @Test
    void createShouldThrowBookingNotFoundException() throws Exception {
        when(bookingService.save(any(Booking.class), anyLong()))
                .thenThrow(BookingNotFoundException.class);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booker1.getId())
                        .content(mapper.writeValueAsString(bookingDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }

    @Test
    void createShouldThrowInvalidBookerException() throws Exception {
        when(bookingService.save(any(Booking.class), anyLong()))
                .thenThrow(InvalidBookerException.class);

        mvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", booker1.getId())
                        .content(mapper.writeValueAsString(bookingDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


    @Test
    void approveShouldReturnApprovedUser() throws Exception {
        booking1.setStatus(Status.APPROVED);
        bookingDto1.setStatus(Status.APPROVED);
        when(bookingService.approve(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(booking1);

        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", booker1.getId())
                        .content(mapper.writeValueAsString(bookingDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString()), String.class));

    }

    @Test
    void approveShouldThrowBookingAlreadyApprovedException() throws Exception {
        booking1.setStatus(Status.APPROVED);
        bookingDto1.setStatus(Status.APPROVED);
        when(bookingService.approve(anyLong(), anyLong(), anyBoolean()))
                .thenThrow(BookingAlreadyApprovedException.class);

        mvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header("X-Sharer-User-Id", booker1.getId())
                        .content(mapper.writeValueAsString(bookingDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void findByIdTest() throws Exception {
        when(bookingService.findById(anyLong(), anyLong()))
                .thenReturn(booking1);

        mvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", booker1.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$.start", is(bookingDto1.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.end", is(bookingDto1.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.status", is(booking1.getStatus().toString()), String.class));
    }

    @Test
    void findAllByBookerIdTest() throws Exception {
        when(bookingService.findAllByBookerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(booking1));

        mvc.perform(get("/bookings")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", booker1.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto1.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].end", is(bookingDto1.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].status", is(booking1.getStatus().toString()), String.class));
    }

    @Test
    void findAllByOwnerIdTest() throws Exception {
        when(bookingService.findAllByOwnerId(anyLong(), anyString(), anyInt(), anyInt()))
                .thenReturn(List.of(booking1));

        mvc.perform(get("/bookings/owner")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", user1.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(bookingDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].start", is(bookingDto1.getStart().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].end", is(bookingDto1.getEnd().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].status", is(booking1.getStatus().toString()), String.class));
    }
}
