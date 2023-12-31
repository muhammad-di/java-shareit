package ru.practicum.shareit.item.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.comment.dto.CommentDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Objects;


@Data
@Builder
public class ItemDtoForGet {
    @Min(0)
    private long id;
    @NotEmpty(message = "Name is mandatory")
    private final String name;
    @NotEmpty(message = "Description is mandatory")
    private final String description;
    @NotNull
    private Boolean available;
    private BookingDtoForItemDto lastBooking;
    private BookingDtoForItemDto nextBooking;
    private Collection<CommentDto> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemDtoForGet itemDto = (ItemDtoForGet) o;
        return id == itemDto.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

