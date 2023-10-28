package ru.practicum.shareit.booking.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import java.util.Objects;

/**
 * TODO Sprint add-bookings.
 */
@Data
@Builder
public class BookingDtoForItemDto {
    @Min(0)
    private long id;
    private long bookerId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookingDtoForItemDto that = (BookingDtoForItemDto) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
