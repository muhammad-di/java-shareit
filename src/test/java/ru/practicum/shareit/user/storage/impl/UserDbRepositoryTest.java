//package ru.practicum.shareit.user.storage.impl;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import ru.practicum.shareit.user.model.User;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.*;
//
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//
//@Slf4j
//@SpringBootTest
//@AutoConfigureTestDatabase
//@RequiredArgsConstructor(onConstructor_ = @Autowired)
//public class UserDbRepositoryTest {
//    private final UserDbRepository userDbRepository;
//    private User user1;
//    private User user2;
//    private User user3;
//    private Collection<User> userList;
//
//
//    @BeforeEach
//    public void createUsers() {
//        user1 = User.builder()
//                .name("theUser1")
//                .email("theUser1@mail.org")
//                .build();
//        user2 = User.builder()
//                .name("theUser2")
//                .email("theUser2@mail.org")
//                .build();
//        user3 = User.builder()
//                .name("theUser3")
//                .email("theUser3@mail.org")
//                .build();
//        userDbRepository.create(user1);
//        userDbRepository.create(user2);
//        userDbRepository.create(user3);
//
//        userList = List.of(user1, user2, user3);
//    }
//
//    @AfterEach
//    public void clean() {
//        userDbRepository.delete(user1.getId());
//        userDbRepository.delete(user2.getId());
//        userDbRepository.delete(user3.getId());
//
//    }
//
//    //FindById--------------------------------------------------------------------------------------------------------------
//
//    @Test
//    public void testFindUserByExistingId1() {
//        Optional<User> userOptional = Optional.of(userDbRepository.findById(user1.getId()));
//        assertThat(userOptional).isPresent().hasValue(user1);
//    }
//
//    @Test
//    public void testFindUserByExistingId2() {
//        Optional<User> userOptional = Optional.of(userDbRepository.findById(user2.getId()));
//        assertThat(userOptional).isPresent().hasValue(user2);
//    }
//
//    @Test
//    public void testFindUserByExistingId3() {
//        Optional<User> userOptional = Optional.of(userDbRepository.findById(user3.getId()));
//        assertThat(userOptional).isPresent().hasValue(user3);
//    }
//
//    @Test
//    public void testFindUserByNonExistingId2000ShouldReturnNull() {
//        Optional<User> userOptional = Optional.ofNullable(userDbRepository.findById(2000));
//        assertFalse(userOptional.isPresent());
//    }
//
//    @Test
//    public void testFindUserByNonExistingId555ShouldReturnNull() {
//        Optional<User> userOptional = Optional.ofNullable(userDbRepository.findById(555));
//        assertFalse(userOptional.isPresent());
//    }
//
//    @Test
//    public void testFindUserByNonExistingId39ShouldReturnNull() {
//        Optional<User> userOptional = Optional.ofNullable(userDbRepository.findById(39));
//        assertFalse(userOptional.isPresent());
//    }
//
//    //FindAll------------------------------------------------------------------------------------------------------------
//    @Test
//    public void testFindAll() {
//        Optional<Collection<User>> userListOptional = Optional.of(userDbRepository.findAll());
//        assertThat(userListOptional).isPresent().hasValue(userList);
//    }
//
//    @Test
//    public void testFindAllEmptyList() {
//        clean();
//        Optional<Collection<User>> userListOptional = Optional.of(userDbRepository.findAll());
//        assertThat(userListOptional).isPresent().hasValue(new ArrayList<>());
//    }
//
//    //Create------------------------------------------------------------------------------------------------------------
//
//    @Test
//    public void testCreateUserWithNameTheUser4() {
//        User user = User.builder()
//                .name("theUser4")
//                .email("theUser4@mail.org")
//                .build();
//        User expected = User.builder()
//                .id(4)
//                .name("theUser4")
//                .email("theUser4@mail.org")
//                .build();
//
//        Optional<User> userOptional = Optional.of(userDbRepository.create(user));
//        assertThat(userOptional).isPresent().hasValue(expected);
//        userDbRepository.delete(4);
//    }
//
//    //Delete------------------------------------------------------------------------------------------------------------
//
//    @Test
//    public void testDeleteUserById1() {
//        userDbRepository.delete(1);
//        Optional<User> userOptional = Optional.ofNullable(userDbRepository.findById(1));
//        assertFalse(userOptional.isPresent());
//    }
//
//    @Test
//    public void testDeleteUserById2() {
//        userDbRepository.delete(2);
//        Optional<User> userOptional = Optional.ofNullable(userDbRepository.findById(2));
//        assertFalse(userOptional.isPresent());
//    }
//
//    @Test
//    public void testDeleteUserById3() {
//        userDbRepository.delete(3);
//        Optional<User> userOptional = Optional.ofNullable(userDbRepository.findById(3));
//        assertFalse(userOptional.isPresent());
//    }
//
//    @Test
//    public void testDeleteUserById600() {
//
//        boolean isA = userDbRepository.delete(600);
//        assertFalse(isA);
//    }
//}
