package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;


    @Transactional
    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional
    @Override
    public User update(UserDto userDto) throws UserNotFoundException {
        User userFromDb = getUser(userDto.getId());
        User user = UserMapper.toUserForUpdate(userDto, userFromDb);

        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findById(long id) throws UserNotFoundException {
        return getUser(id);
    }


    @Transactional
    @Override
    public void deleteById(long id) throws UserNotFoundException {
        if (!userRepository.existsById(id)) {
            String message = String.format("a user with id { %d } does not exist", id);
            throw new UserNotFoundException(message);
        }
        userRepository.deleteById(id);
    }

    private User getUser(long id) throws UserNotFoundException {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    String message = String.format("a user with id { %d } does not exist", id);
                    return new UserNotFoundException(message);
                });
    }
}
