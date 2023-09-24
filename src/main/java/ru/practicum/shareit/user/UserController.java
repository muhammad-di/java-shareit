package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.exception.UserWithEmailAlreadyExists;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.Collection;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDto create(@Valid @RequestBody UserDto userDto)
            throws UserWithEmailAlreadyExists {
        return service.create(userDto);
    }

    @PatchMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserDto update(@PathVariable long userId, @RequestBody UserDto userDto)
            throws UserNotFoundException, UserWithEmailAlreadyExists {
        userDto.setId(userId);
        return service.update(userDto);
    }

    @GetMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserDto findById(@PathVariable long userId)
            throws UserNotFoundException {
        return service.findById(userId);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public void delete(@PathVariable long userId)
            throws UserNotFoundException {
        service.delete(userId);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public Collection<User> findAll() {
        return service.findAll();
    }
}
