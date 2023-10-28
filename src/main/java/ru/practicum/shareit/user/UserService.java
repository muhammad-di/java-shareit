package ru.practicum.shareit.user;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

interface UserService {

    List<User> findAll();

    User findById(long id) throws UserNotFoundException;

    User save(User user);

    User update(User user) throws UserNotFoundException;

    void deleteById(long id) throws UserNotFoundException;
}