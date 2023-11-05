package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserRepositoryTest {
    @Autowired
    private final EntityManager em;

    @Autowired
    private final UserRepository repository;

    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void createUsers() {
        user1 = User.builder().name("name1").email("name1@mail.ru").build();
        user2 = User.builder().name("name2").email("name2@mail.ru").build();
        user3 = User.builder().name("name3").email("name3@mail.ru").build();
    }

    @Test
    public void shouldFindByIdUser1() throws UserNotFoundException {
        User createdUser = repository.save(user1);
        Optional<User> userOptional = repository.findById(createdUser.getId());
        User user = userOptional.orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"));

        assertThat(user, samePropertyValuesAs(user1));
        assertThat(user, Matchers.hasProperty("id", equalTo(createdUser.getId())));
        assertThat(user, Matchers.hasProperty("name", equalTo("name1")));
        assertThat(user, Matchers.hasProperty("email", equalTo("name1@mail.ru")));

        List<User> users = repository.findAll();
        log.info("1------------{}", users);
    }


    @Test
    public void shouldThrowExceptionWhenFindById_2() {
        Optional<User> userOptional = repository.findById(2L);

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userOptional.orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"))
        );
    }

    @Test
    public void shouldThrowExceptionWhenFindById40() {
        Optional<User> userOptional = repository.findById(40L);

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userOptional.orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"))
        );
    }

    @Test
    public void shouldThrowExceptionWhenFindById1000() {
        Optional<User> userOptional = repository.findById(1000L);

        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> userOptional.orElseThrow(() -> new UserNotFoundException("USER NOT FOUND"))
        );
    }

    @Test
    public void shouldFindNoUsersIfRepositoryIsEmpty() {
        Iterable users = repository.findAll();

        assertThat(users, equalTo(Collections.emptyList()));

        log.info("2------------{}", users);
    }

    @Test
    public void shouldFindAllUsersListNotBeEmpty() {
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);

        List<User> users = repository.findAll();
        assertThat(users, is(not(empty())));

        log.info("3------------{}", users);
    }

    @Test
    public void shouldFindAllUsersSize3() {
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);

        List<User> users = repository.findAll();
        assertThat(users, hasSize(3));

        log.info("4------------{}", users);
    }

    @Test
    public void shouldFindAllUsersContainUser1User2User3() {
        repository.save(user1);
        repository.save(user2);
        repository.save(user3);

        List<User> users = repository.findAll();
        assertThat(users, contains(user1, user2, user3));

        log.info("5------------{}", users);
    }

    @Test
    public void shouldSaveUser1() {
        User createdUser = repository.save(user1);

        TypedQuery<User> queryForUser = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = queryForUser.setParameter("id", createdUser.getId()).getSingleResult();

        assertThat(createdUser, samePropertyValuesAs(user1));
        assertThat(createdUser, Matchers.hasProperty("id", equalTo(user.getId())));
        assertThat(createdUser, Matchers.hasProperty("name", equalTo(user.getName())));
        assertThat(createdUser, Matchers.hasProperty("email", equalTo(user.getEmail())));

        List<User> users = repository.findAll();
        log.info("6------------{}", users);
    }

    @Test
    public void shouldReturnTrueWhenExistById12() {
        User createdUser = repository.save(user1);
        boolean actual = repository.existsById(createdUser.getId());

        assertThat(actual, equalTo(true));
    }

    @Test
    public void shouldReturnTrueWhenExistById14() {
        repository.save(user2);
        User createdUser = repository.save(user3);
        boolean actual = repository.existsById(createdUser.getId());

        assertThat(actual, equalTo(true));
    }

    @Test
    public void shouldReturnTrueWhenExistById17() {
        repository.save(user1);
        repository.save(user2);
        User createdUser = repository.save(user3);
        boolean actual = repository.existsById(createdUser.getId());

        assertThat(actual, equalTo(true));
    }

    @Test
    public void shouldReturnFalseWhenExistById18() {
        boolean actual = repository.existsById(18L);

        assertThat(actual, equalTo(false));
    }

    @Test
    public void shouldReturnFalseWhenExistById50() {
        boolean actual = repository.existsById(50L);

        assertThat(actual, equalTo(false));
    }


    @Test
    public void shouldReturnFalseWhenExistById_1000() {
        boolean actual = repository.existsById(1000L);

        assertThat(actual, equalTo(false));
    }
}