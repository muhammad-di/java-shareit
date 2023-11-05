package ru.practicum.shareit.user.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.UserServiceImpl;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository repository;

    private User user1;


    @BeforeEach
    public void createUsers() {
        user1 = User.builder().id(1L).name("name1").email("name1@mail.ru").build();
    }

    @Test
    public void testFindById() throws UserNotFoundException {
        UserService service = new UserServiceImpl(repository);

        Mockito.
                when(repository.findById(1L))
                .thenReturn(Optional.of(user1));

        User actual = service.findById(1);

        assertThat(actual, Matchers.hasProperty("id", equalTo(1L)));
        assertThat(actual, Matchers.hasProperty("name", equalTo("name1")));
        assertThat(actual, Matchers.hasProperty("email", equalTo("name1@mail.ru")));
    }

    @Test
    public void testFindByIdShouldThrowUserNotFoundException() {
        UserService service = new UserServiceImpl(repository);

        Mockito.
                when(repository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(null));

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> service.findById(1L)
        );

        Assertions.assertEquals("MASSAGE: a user with id { 1 } does not exist; ERROR CODE: null",
                exception.getErrorMessage());

    }

    @Test
    public void testSave() {
        UserService service = new UserServiceImpl(repository);

        Mockito.
                when(repository.save(Mockito.any(User.class)))
                .thenReturn(user1);

        User actual = service.save(user1);

        assertThat(actual, Matchers.hasProperty("id", equalTo(1L)));
        assertThat(actual, Matchers.hasProperty("name", equalTo("name1")));
        assertThat(actual, Matchers.hasProperty("email", equalTo("name1@mail.ru")));
    }

    @Test
    public void testSaveWhenArgumentNullShouldThrowIllegalArgumentException() throws IllegalArgumentException {
        UserService service = new UserServiceImpl(repository);

        Mockito.
                when(repository.save(null))
                .thenThrow(new IllegalArgumentException("Illegal argument"));


        final IllegalArgumentException exception = Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> service.save(null)
        );

        Assertions.assertEquals("Illegal argument", exception.getMessage());
    }

    @Test
    public void testUpdateWhenNameAndEmailNotNull() throws UserNotFoundException {
        UserService service = new UserServiceImpl(repository);
        UserDto userDto = UserDto.builder().id(1).name("newName").email("name@newEmail.ru").build();
        User updatedUser = User.builder().id(1).name("newName").email("name@newEmail.ru").build();

        Mockito.
                when(repository.save(updatedUser))
                .thenReturn(updatedUser);
        Mockito.
                when(repository.findById(1L))
                .thenReturn(Optional.of(user1));

        User actual = service.update(userDto);

        assertThat(actual, Matchers.hasProperty("id", equalTo(1L)));
        assertThat(actual, Matchers.hasProperty("name", equalTo("newName")));
        assertThat(actual, Matchers.hasProperty("email", equalTo("name@newEmail.ru")));
    }

    @Test
    public void testUpdateWhenNameNullAndEmailNotNull() throws UserNotFoundException {
        UserService service = new UserServiceImpl(repository);
        UserDto userDto = UserDto.builder().id(1).name(null).email("name@newEmail.ru").build();
        User updatedUser = User.builder().id(1).name("name").email("name@newEmail.ru").build();

        Mockito.
                when(repository.save(updatedUser))
                .thenReturn(updatedUser);
        Mockito.
                when(repository.findById(1L))
                .thenReturn(Optional.of(user1));

        User actual = service.update(userDto);

        assertThat(actual, Matchers.hasProperty("id", equalTo(1L)));
        assertThat(actual, Matchers.hasProperty("name", equalTo("name")));
        assertThat(actual, Matchers.hasProperty("email", equalTo("name@newEmail.ru")));
    }

    @Test
    public void testUpdateWhenNameNotNullAndEmailNull() throws UserNotFoundException {
        UserService service = new UserServiceImpl(repository);
        UserDto userDto = UserDto.builder().id(1).name("newName").email(null).build();
        User updatedUser = User.builder().id(1).name("newName").email("name1@mail.ru").build();

        Mockito.
                when(repository.save(updatedUser))
                .thenReturn(updatedUser);
        Mockito.
                when(repository.findById(1L))
                .thenReturn(Optional.of(user1));

        User actual = service.update(userDto);

        assertThat(actual, Matchers.hasProperty("id", equalTo(1L)));
        assertThat(actual, Matchers.hasProperty("name", equalTo("newName")));
        assertThat(actual, Matchers.hasProperty("email", equalTo("name1@mail.ru")));
    }

    @Test
    public void testUpdateWhenUserDoesNotExistShouldThrowUserNotFoundException() {
        UserService service = new UserServiceImpl(repository);
        UserDto userDto = UserDto.builder().id(1).name("newName").email("name@newEmail.ru").build();

        Mockito.
                when(repository.findById(1L))
                .thenReturn(Optional.ofNullable(null));

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> service.update(userDto)
        );

        Assertions.assertEquals("MASSAGE: a user with id { 1 } does not exist; ERROR CODE: null",
                exception.getErrorMessage());
    }

    @Test
    public void testDeleteById1() throws UserNotFoundException {
        UserService service = new UserServiceImpl(repository);

        Mockito.
                when(repository.existsById(Mockito.anyLong()))
                .thenReturn(true);

        service.deleteById(1L);
        Mockito.verify(repository, Mockito.times(1))
                .deleteById(1L);
    }

    @Test
    public void testDeleteByIdShouldThrowUserNotFoundException() {
        UserService service = new UserServiceImpl(repository);

        Mockito.
                when(repository.existsById(Mockito.anyLong()))
                .thenReturn(false);

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> service.deleteById(1L)
        );

        Assertions.assertEquals("MASSAGE: a user with id { 1 } does not exist; ERROR CODE: null",
                exception.getErrorMessage());
    }
}
