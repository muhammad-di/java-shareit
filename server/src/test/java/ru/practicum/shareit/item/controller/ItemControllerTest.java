package ru.practicum.shareit.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
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

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemService service;

    @Autowired
    private MockMvc mvc;

    private Item item1;
    private ItemDto itemDto1;
    private ItemDtoForGet itemDtoForGet1;
    private User user1;
    private User booker1;
    private Comment comment1;
    private CommentDto commentDto1;


    @BeforeEach
    public void createItems() {
        LocalDateTime testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
        LocalDateTime testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);
        LocalDateTime testCommentCreated = LocalDateTime.of(2023, 11, 5, 5, 15);

        user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        item1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();
        itemDto1 = ItemMapper.toItemDto(item1);
        itemDtoForGet1 = ItemMapper.toItemDtoForGet(item1);

        Booking booking1 = Booking.builder()
                .id(1)
                .start(testStart)
                .end(testEnd)
                .booker(booker1)
                .item(item1)
                .status(Status.WAITING)
                .build();

        comment1 = Comment.builder()
                .id(1)
                .text("comment1")
                .item(item1)
                .author(booker1)
                .created(testCommentCreated)
                .build();
        commentDto1 = CommentMapper.toCommentDto(comment1);
    }

    @Test
    void createShouldReturnNewUser() throws Exception {
        when(service.save(any(Item.class), anyLong()))
                .thenReturn(item1);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", user1.getId())
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto1.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto1.getRequestId()), Long.class));

    }

    @Test
    void createShouldThrowItemRequestNotFoundException() throws Exception {
        when(service.save(any(Item.class), anyLong()))
                .thenThrow(ItemRequestNotFoundException.class);

        mvc.perform(post("/items")
                        .header("X-Sharer-User-Id", user1.getId())
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

    }


    @Test
    void updateShouldReturnNewUser() throws Exception {
        when(service.update(any(ItemDto.class), anyLong()))
                .thenReturn(item1);

        mvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", user1.getId())
                        .content(mapper.writeValueAsString(itemDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto1.getName())))
                .andExpect(jsonPath("$.description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDto1.getAvailable())))
                .andExpect(jsonPath("$.requestId", is(itemDto1.getRequestId()), Long.class));

    }


    @Test
    void findByIdTest() throws Exception {
        when(service.findById(anyLong(), anyLong()))
                .thenReturn(itemDtoForGet1);

        mvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", booker1.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoForGet1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoForGet1.getName())))
                .andExpect(jsonPath("$.description", is(itemDtoForGet1.getDescription())))
                .andExpect(jsonPath("$.available", is(itemDtoForGet1.getAvailable())));
    }

    @Test
    void findAllByOwnerIdTest() throws Exception {
        when(service.findAllByOwnerId(anyLong()))
                .thenReturn(List.of(itemDtoForGet1));


        mvc.perform(get("/items")
                        .header("X-Sharer-User-Id", booker1.getId())
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDtoForGet1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDtoForGet1.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDtoForGet1.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDtoForGet1.getAvailable())));
    }


    @Test
    void searchByNameTest() throws Exception {
        when(service.searchByDescription(anyString()))
                .thenReturn(List.of(item1));


        mvc.perform(get("/items/search")
                        .param("text", "test string")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is(itemDto1.getId()), Long.class))
                .andExpect(jsonPath("$[0].name", is(itemDto1.getName())))
                .andExpect(jsonPath("$[0].description", is(itemDto1.getDescription())))
                .andExpect(jsonPath("$[0].available", is(itemDto1.getAvailable())))
                .andExpect(jsonPath("$[0].requestId", is(itemDto1.getRequestId()), Long.class));

    }

    @Test
    void createCommentTest() throws Exception {
        when(service.createComment(any(Comment.class)))
                .thenReturn(comment1);

        mvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", user1.getId())
                        .content(mapper.writeValueAsString(commentDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto1.getId()), Long.class))
                .andExpect(jsonPath("$.text", is(commentDto1.getText())))
                .andExpect(jsonPath("$.authorName", is(commentDto1.getAuthorName())))
                .andExpect(jsonPath("$.created", is(commentDto1.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))));

    }
}
