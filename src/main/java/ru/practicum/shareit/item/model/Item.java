package ru.practicum.shareit.item.model;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class Item {
    private final String name;
    private final String description;
    private final ItemRequest request;
    private long id;
    private Boolean available;
    private long owner;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Map<String, Object> toMap() {
        Map<String, Object> value = new HashMap<>();
        value.put("name", name);
        value.put("description", description);
        value.put("available", available);
        return value;
    }
}
