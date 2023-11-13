package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    User save(User user);

    User update(UserDto userDto) throws UserNotFoundException;

    List<User> findAll();

    User findById(long id) throws UserNotFoundException;

    void deleteById(long id) throws UserNotFoundException;
}