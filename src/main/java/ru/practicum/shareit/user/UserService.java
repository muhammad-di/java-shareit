package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserWithEmailAlreadyExists;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public UserDto create(UserDto userDto) throws UserWithEmailAlreadyExists {
        if (repository.contains(userDto.getEmail())) {
            String message = String.format("the user with an email {%s} already exists", userDto.getEmail());
            throw new UserWithEmailAlreadyExists(message);
        }
        User user = UserMapper.toUser(userDto);
        User userRes = repository.create(user);
        return UserMapper.toUserDto(userRes);
    }

    public UserDto update(long userId, UserDto userDto) throws UserNotFoundException, UserWithEmailAlreadyExists {
        userDto.setId(userId);
        if (!repository.contains(userId)) {
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
