package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.exception.UserWithEmailAlreadyExists;
import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserRepository {
    User create(User user);

    User update(User user) throws UserWithEmailAlreadyExists;

    User findById(long id);

    boolean contains(long id);

    boolean contains(String email);

    boolean delete(long id);

    Collection<User> findAll();
}
