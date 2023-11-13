package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class ItemDto {
    private long id;
    private final String name;
    private final String description;
    private Boolean available;
    private Long requestId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDto itemDto = (ItemDto) o;
        return id == itemDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}


