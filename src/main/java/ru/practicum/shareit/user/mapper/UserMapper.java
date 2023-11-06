package ru.practicum.shareit.user.mapper;

import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build();
    }

    public static User toUser(UserDto userDto) {
        return User.builder()
                .id(userDto.getId())
                .name(userDto.getName())
                .email(userDto.getEmail())
                .build();
    }

    public static User toUserForUpdate(UserDto userDto, User userFromBd) {
        return User.builder()
                .id(userDto.getId())
                .name(getName(userDto, userFromBd))
                .email(getEmail(userDto, userFromBd))
                .build();
    }

    private static String getName(UserDto userDto, User userFromBd) {
        if (userDto.getName() != null) {
            return userDto.getName();
        } else if (userFromBd.getName() != null) {
            return userFromBd.getName();
        } else {
            return null;
        }
    }

    private static String getEmail(UserDto userDto, User userFromBd) {
        if (userDto.getEmail() != null) {
            return userDto.getEmail();
        } else if (userFromBd.getEmail() != null) {
            return userFromBd.getEmail();
        } else {
            return null;
        }
    }
}
