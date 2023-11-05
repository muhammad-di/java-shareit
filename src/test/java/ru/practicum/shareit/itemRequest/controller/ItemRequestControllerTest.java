package ru.practicum.shareit.itemRequest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestController;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDtoForRequestBody;
import ru.practicum.shareit.request.dto.ItemRequestDtoForResponseBody;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestService service;

    @Autowired
    private MockMvc mvc;

    private Item item1;
    private ItemDto itemDto1;
    private ItemDtoForGet itemDtoForGet1;
    private User user1;
    private UserDto userDto1;
    private User booker1;
    private UserDto bookerDto1;
    private User requestor1;
    private UserDto requestorDto1;
    private Booking booking1;
    private BookingDto bookingDto1;
    private Comment comment1;
    private CommentDto commentDto1;
    private ItemRequest itemRequest1;
    private ItemRequestDtoForRequestBody itemRequestDtoForRequestBody1;
    private ItemRequestDtoForResponseBody itemRequestDtoForResponseBody1;


    @BeforeEach
    public void create() {
        LocalDateTime testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
        LocalDateTime testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);
        LocalDateTime testCommentCreated = LocalDateTime.of(2023, 11, 5, 5, 15);
        LocalDateTime testItemRequestCreated = LocalDateTime.of(2023, 10, 5, 5, 15);


        user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        userDto1 = UserMapper.toUserDto(user1);
        booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        bookerDto1 = UserMapper.toUserDto(booker1);
        requestor1 = User.builder().id(3).name("John3").email("john3.doe@mail.com").build();
        requestorDto1 = UserMapper.toUserDto(requestor1);
        item1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();
        itemDto1 = ItemMapper.toItemDto(item1);
        itemDtoForGet1 = ItemMapper.toItemDtoForGet(item1);

        booking1 = Booking.builder()
                .id(1)
                .start(testStart)
                .end(testEnd)
                .booker(booker1)
                .item(item1)
                .status(Status.WAITING)
                .build();
        bookingDto1 = BookingMapping.toBookingDto(booking1);

        comment1 = Comment.builder()
                .id(1)
                .text("comment1")
                .item(item1)
                .author(booker1)
                .created(testCommentCreated)
                .build();
        commentDto1 = CommentMapper.toCommentDto(comment1);

        itemRequest1 = ItemRequest.builder()
                .id(1)
                .description("description1")
                .requestor(requestor1)
                .created(testItemRequestCreated)
                .build();

        itemRequestDtoForResponseBody1 = ItemRequestMapper.toItemRequestDtoForResponseBody(itemRequest1);

        itemRequestDtoForRequestBody1 = ItemRequestDtoForRequestBody.builder().description("description1").build();
    }

    @Test
    void createShouldReturnNewUser() throws Exception {
        when(service.save(any(ItemRequest.class), anyLong()))
                .thenReturn(itemRequest1);

        mvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", requestor1.getId())
                        .content(mapper.writeValueAsString(itemRequestDtoForRequestBody1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemRequestDtoForResponseBody1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoForResponseBody1.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDtoForResponseBody1.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.items", is(itemRequestDtoForResponseBody1.getItems())));
    }


    @Test
    void findByIdTest() throws Exception {
        when(service.findById(anyLong(), anyLong()))
                .thenReturn(itemRequestDtoForResponseBody1);

        mvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", requestor1.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemRequestDtoForResponseBody1.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemRequestDtoForResponseBody1.getDescription())))
                .andExpect(jsonPath("$.created", is(itemRequestDtoForResponseBody1.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$.items", is(itemRequestDtoForResponseBody1.getItems())));
    }

    @Test
    void findAllByRequestorIdTest() throws Exception {
        when(service.findAllByRequestorId(anyLong()))
                .thenReturn(List.of(itemRequestDtoForResponseBody1));


        mvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", requestor1.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDtoForResponseBody1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDtoForResponseBody1.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequestDtoForResponseBody1.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].items", is(itemRequestDtoForResponseBody1.getItems())));
    }

    @Test
    void findAllTest() throws Exception {
        when(service.findAll(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(itemRequestDtoForResponseBody1));


        mvc.perform(get("/requests/all")
                        .param("from", "0")
                        .param("size", "1")
                        .header("X-Sharer-User-Id", requestor1.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemRequestDtoForResponseBody1.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(itemRequestDtoForResponseBody1.getDescription())))
                .andExpect(jsonPath("$[0].created", is(itemRequestDtoForResponseBody1.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))))
                .andExpect(jsonPath("$[0].items", is(itemRequestDtoForResponseBody1.getItems())));
    }
}
