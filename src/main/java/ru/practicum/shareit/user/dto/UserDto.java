package ru.practicum.shareit.user.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Objects;

@Data
@Builder
public class UserDto {
    private long id;
    @NotEmpty(message = "Name is mandatory")
    private final String name;
    @Email
    @NotEmpty(message = "Email is mandatory")
    private final String email;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return id == userDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
