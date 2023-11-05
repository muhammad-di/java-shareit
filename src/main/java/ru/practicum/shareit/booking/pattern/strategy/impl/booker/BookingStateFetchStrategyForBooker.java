package ru.practicum.shareit.booking.pattern.strategy.impl.booker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingStateFetchStrategyForBooker {
    Page<Booking> findAllByUserId(long bookerId, Pageable pageable);
}
