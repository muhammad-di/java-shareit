package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserWithEmailAlreadyExists;


import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private final UserService userService;
    private UserDto user1;
    private UserDto user2;
    private UserDto user3;

    @BeforeEach
    public void createUsers() throws UserWithEmailAlreadyExists {
        user1 = userService.create(UserDto.builder()
                .name("theUser1")
                .email("theUser1@mail.org")
                .build());
        user2 = userService.create(UserDto.builder()
                .name("theUser2")
                .email("theUser2@mail.org")
                .build());
        user3 = userService.create(UserDto.builder()
                .name("theUser3")
                .email("theUser3@mail.org")
                .build());
    }

    @AfterEach
    public void clean() {
        try {
            userService.delete(user1.getId());
            userService.delete(user2.getId());
            userService.delete(user3.getId());
        } catch (UserNotFoundException e) {
            log.info("------------already deleted {}", user1.getId());
        }
        try {
            userService.delete(user2.getId());
        } catch (UserNotFoundException e) {
            log.info("------------already deleted {}", user2.getId());
        }
        try {
            userService.delete(user3.getId());
        } catch (UserNotFoundException e) {
            log.info("------------already deleted {}", user3.getId());
        }

    }

    @Test
    public void testDeleteByIdWhichDoesNotExist() {
        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.delete(420));

        String expectedMessage = "a user with id { 420 } does not exist";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    public void testDeleteById1() throws UserNotFoundException {
        log.info("------------{}", userService.findById(user1.getId()));
        userService.delete(user1.getId());

        Exception exception = assertThrows(UserNotFoundException.class, () -> userService.findById(user1.getId()));

        String expectedMessage = String.format("a user with id { %d } does not exist", user1.getId());
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
    }

}
