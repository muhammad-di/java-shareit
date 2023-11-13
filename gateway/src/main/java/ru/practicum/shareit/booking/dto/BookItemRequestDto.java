package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BookItemRequestDto {
    @PositiveOrZero
    private long itemId;

    @FutureOrPresent
    @NotNull(message = "Start is mandatory")
    private LocalDateTime start;

    @Future
    @NotNull(message = "End is mandatory")
    private LocalDateTime end;
}
