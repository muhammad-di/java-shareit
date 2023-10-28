//package ru.practicum.shareit.user.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.practicum.shareit.user.UserServiceImpl2;
//import ru.practicum.shareit.user.dto.UserDto;
//import ru.practicum.shareit.user.exception.UserNotFoundException;
//import ru.practicum.shareit.user.exception.UserWithEmailAlreadyExists;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Slf4j
//@SpringBootTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//public class UserServiceImpl2Test {
//    private final UserServiceImpl2 userServiceImpl2;
//    private UserDto user1;
//    private UserDto user2;
//    private UserDto user3;
//
//    @BeforeEach
//    public void createUsers() throws UserWithEmailAlreadyExists {
//        user1 = userServiceImpl2.create(UserDto.builder()
//                .name("theUser1")
//                .email("theUser1@mail.org")
//                .build());
//        user2 = userServiceImpl2.create(UserDto.builder()
//                .name("theUser2")
//                .email("theUser2@mail.org")
//                .build());
//        user3 = userServiceImpl2.create(UserDto.builder()
//                .name("theUser3")
//                .email("theUser3@mail.org")
//                .build());
//    }
//
//    @AfterEach
//    public void clean() {
//        try {
//            userServiceImpl2.delete(user1.getId());
//            userServiceImpl2.delete(user2.getId());
//            userServiceImpl2.delete(user3.getId());
//        } catch (UserNotFoundException e) {
//            log.info("------------already deleted {}", user1.getId());
//        }
//        try {
//            userServiceImpl2.delete(user2.getId());
//        } catch (UserNotFoundException e) {
//            log.info("------------already deleted {}", user2.getId());
//        }
//        try {
//            userServiceImpl2.delete(user3.getId());
//        } catch (UserNotFoundException e) {
//            log.info("------------already deleted {}", user3.getId());
//        }
//
//    }
//
//    @Test
//    public void testDeleteByIdWhichDoesNotExist() {
//        Exception exception = assertThrows(UserNotFoundException.class, () -> userServiceImpl2.delete(420));
//
//        String expectedMessage = "a user with id { 420 } does not exist";
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage, actualMessage);
//    }
//
//    @Test
//    public void testDeleteById1() throws UserNotFoundException {
//        log.info("------------{}", userServiceImpl2.findById(user1.getId()));
//        userServiceImpl2.delete(user1.getId());
//
//        Exception exception = assertThrows(UserNotFoundException.class, () -> userServiceImpl2.findById(user1.getId()));
//
//        String expectedMessage = String.format("a user with id { %d } does not exist", user1.getId());
//        String actualMessage = exception.getMessage();
//
//        assertEquals(expectedMessage, actualMessage);
//    }
//
//}
