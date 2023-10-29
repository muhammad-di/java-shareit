package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.Collection;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        user = userService.save(user);
        return UserMapper.toUserDto(user);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserDto update(@PathVariable long userId, @RequestBody UserDto userDto) throws UserNotFoundException {
        User user = UserMapper.toUser(userDto);
        user.setId(userId);
        user = userService.update(user);
        return UserMapper.toUserDto(user);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserDto findById(@PathVariable long userId) throws UserNotFoundException {
        User user = userService.findById(userId);
        return UserMapper.toUserDto(user);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable long userId) throws UserNotFoundException {
        userService.deleteById(userId);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Collection<UserDto> findAll() {
        return userService.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }
}
