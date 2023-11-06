package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserServiceImplTest {

    @Autowired
    private final EntityManager em;
    @Autowired
    private final UserService service;

    private User user1;
    private User user2;
    private User user3;
    private UserDto userDto1;


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
    }

    @Test
    void testSave() {
        User responseUser = service.save(user1);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", user1.getEmail()).getSingleResult();

        assertThat(user.getId(), notNullValue());
        assertThat(user.getName(), equalTo(responseUser.getName()));
        assertThat(user.getEmail(), equalTo(responseUser.getEmail()));
    }

    @Test
    void testUpdate() throws UserNotFoundException {
        User responseUser = service.save(user1);
        userDto1.setId(responseUser.getId());
        User responseUserAfterUpdate = service.update(userDto1);

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", responseUser.getId()).getSingleResult();

        assertThat(user.getId(), equalTo(responseUserAfterUpdate.getId()));
        assertThat(user.getName(), equalTo(responseUserAfterUpdate.getName()));
        assertThat(user.getEmail(), equalTo(responseUserAfterUpdate.getEmail()));
    }

    @Test
    void testFindAll() {
        service.save(user1);
        service.save(user2);
        service.save(user3);
        List<User> list = service.findAll();
        User listUser1 = list.get(0);
        User listUser2 = list.get(1);
        User listUser3 = list.get(2);

        TypedQuery<User> query = em.createQuery("Select u from User u", User.class);
        List<User> users = query.getResultList();
        User responseUser1 = users.get(0);
        User responseUser2 = users.get(1);
        User responseUser3 = users.get(2);

        assertThat(responseUser1.getId(), notNullValue());
        assertThat(responseUser1.getName(), equalTo(listUser1.getName()));
        assertThat(responseUser1.getEmail(), equalTo(listUser1.getEmail()));
        assertThat(responseUser2.getId(), notNullValue());
        assertThat(responseUser2.getName(), equalTo(listUser2.getName()));
        assertThat(responseUser2.getEmail(), equalTo(listUser2.getEmail()));
        assertThat(responseUser3.getId(), notNullValue());
        assertThat(responseUser3.getName(), equalTo(listUser3.getName()));
        assertThat(responseUser3.getEmail(), equalTo(listUser3.getEmail()));
    }

    @Test
    void testFindById() throws UserNotFoundException {
        User responseUserFromSave = service.save(user1);
        User responseUserFromFindById = service.findById(responseUserFromSave.getId());

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = query.setParameter("id", responseUserFromSave.getId()).getSingleResult();

        assertThat(user.getId(), equalTo(responseUserFromFindById.getId()));
        assertThat(user.getName(), equalTo(responseUserFromFindById.getName()));
        assertThat(user.getEmail(), equalTo(responseUserFromFindById.getEmail()));
    }

    @Test
    void testDeleteById() throws UserNotFoundException {
        User responseUserFromSave = service.save(user1);
        service.deleteById(responseUserFromSave.getId());

        TypedQuery<User> query = em.createQuery("Select u from User u where u.id = :id", User.class);

        final NoResultException exception = Assertions.assertThrows(
                NoResultException.class,
                () -> query.setParameter("id", responseUserFromSave.getId()).getSingleResult()
        );

        Assertions.assertEquals("No entity found for query", exception.getMessage());


    }
}