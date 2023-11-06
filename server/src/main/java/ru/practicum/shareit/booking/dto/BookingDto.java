package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
@Builder
public class BookingDto {
    @Min(0)
    private long id;

    @Min(1)
    private long itemId;

    @NotNull(message = "Start is mandatory")
    private LocalDateTime start;

    @NotNull(message = "End is mandatory")
    private LocalDateTime end;

    private Status status;
    private UserDto booker;
    private ItemDto item;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDto that = (BookingDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
