package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserWithEmailAlreadyExists;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

@Service
public class UserService {
    private final UserRepository repository;

    @Autowired
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public UserDto create(UserDto userDto) throws UserWithEmailAlreadyExists {
        if (repository.contains(userDto.getEmail())) {
            String message = String.format("the user with an email {%s} already exists", userDto.getEmail());
            throw new UserWithEmailAlreadyExists(message);
        }
        User user = UserMapper.toUser(userDto);
        User userRes = repository.create(user);
        return UserMapper.toUserDto(userRes);
    }

    public UserDto update(UserDto userDto) throws UserNotFoundException, UserWithEmailAlreadyExists {
        if (!repository.contains(userDto.getId())) {
            String message = String.format("a user with id { %d } does not exist", userDto.getId());
            throw new UserNotFoundException(message);
        }
        User user = UserMapper.toUser(userDto);
        User userRes = repository.update(user);
        return UserMapper.toUserDto(userRes);
    }

    public UserDto findById(long id) throws UserNotFoundException {
        User user = repository.findById(id);
        if (user == null) {
            String message = String.format("a user with id { %d } does not exist", id);
            throw new UserNotFoundException(message);
        }
        return UserMapper.toUserDto(user);
    }

    public void delete(long id) throws UserNotFoundException {
        if (!repository.delete(id)) {
            String message = String.format("a user with id { %d } does not exist", id);
            throw new UserNotFoundException(message);
        }
    }

    public Collection<User> findAll() {
        return repository.findAll();
    }
}
