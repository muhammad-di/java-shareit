package ru.practicum.shareit.user;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

interface UserService {

    @Transactional(readOnly = true)
    List<User> findAll();

    @Transactional(readOnly = true)
    User findById(long id) throws UserNotFoundException;

    @Transactional
    User save(User user);

    @Transactional
    User update(User user) throws UserNotFoundException;

    @Transactional
    void deleteById(long id) throws UserNotFoundException;
}