package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
public class ItemDto {
    @NotEmpty(message = "Name is mandatory")
    private final String name;
    @NotEmpty(message = "Description is mandatory")
    private final String description;
    private long id;
    @NotNull
    private Boolean available;
    private long request;


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
