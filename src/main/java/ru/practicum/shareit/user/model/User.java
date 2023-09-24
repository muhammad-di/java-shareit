package ru.practicum.shareit.user.model;

import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class User {
    private final String name;
    private final String email;
    private long id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> value = new HashMap<>();
        value.put("name", name);
        value.put("email", email);
        return value;
    }
}
