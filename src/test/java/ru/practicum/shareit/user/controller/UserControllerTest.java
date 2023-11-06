package ru.practicum.shareit.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.UserController;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserService userService;

    @Autowired
    private MockMvc mvc;

    private User user1;
    private User user2;
    private User user3;
    private UserDto userDto1;
    private UserDto userDto2;
    private UserDto userDto3;

    @BeforeEach
    public void createUsers() {
        user1 = User.builder().id(1L).name("John").email("john.doe@mail.com").build();
        user2 = User.builder().id(2L).name("name2").email("name2@mail.ru").build();
        user3 = User.builder().id(3L).name("name3").email("name3@mail.ru").build();
        userDto1 = new UserDto(
                1L,
                "John",
                "john.doe@mail.com"
        );
        userDto2 = new UserDto(
                2L,
                "name2",
                "name2@mail.ru"
        );
        userDto3 = new UserDto(
                3L,
                "name3",
                "name3@mail.ru"
        );
    }

    @Test
    void createShouldReturnNewUser() throws Exception {
        when(userService.save(any(User.class)))
                .thenReturn(user1);

        mvc.perform(post("/users")
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    void updateShouldReturnUpdateUser() throws Exception {
        when(userService.update(any(UserDto.class)))
                .thenReturn(user1);

        mvc.perform(patch("/users/1")
                        .param("userId", "1")
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    void updateShouldThrowException() throws Exception {
        when(userService.update(any(UserDto.class)))
                .thenThrow(UserNotFoundException.class);

        mvc.perform(patch("/users/1")
                        .param("userId", "1")
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void findByIdShouldReturnUpdateUser() throws Exception {
        when(userService.findById(Mockito.anyLong()))
                .thenReturn(user1);

        mvc.perform(get("/users/1")
                        .param("userId", "1")
                        .content(mapper.writeValueAsString(userDto1))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(userDto1.getName())))
                .andExpect(jsonPath("$.email", is(userDto1.getEmail())));
    }

    @Test
    void deleteShouldDeleteUser() throws Exception {
        mvc.perform(delete("/users/1")
                        .param("userId", "1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(userService, Mockito.times(1))
                .deleteById(1L);
    }



    @Test
    void finaAllShouldReturnListOfUsers() throws Exception {
        when(userService.findAll())
                .thenReturn(List.of(user1, user2, user3));


        mvc.perform(get("/users")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0]id", is(userDto1.getId()), Long.class))
                .andExpect(jsonPath("$.[0]name", is(userDto1.getName())))
                .andExpect(jsonPath("$.[0]email", is(userDto1.getEmail())))
                .andExpect(jsonPath("$.[1]id", is(userDto2.getId()), Long.class))
                .andExpect(jsonPath("$.[1]name", is(userDto2.getName())))
                .andExpect(jsonPath("$.[1]email", is(userDto2.getEmail())))
                .andExpect(jsonPath("$.[2]id", is(userDto3.getId()), Long.class))
                .andExpect(jsonPath("$.[2]name", is(userDto3.getName())))
                .andExpect(jsonPath("$.[2]email", is(userDto3.getEmail())));
    }
}
