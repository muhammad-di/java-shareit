package ru.practicum.shareit.booking.pattern.strategy.impl.booker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;


public interface BookingStateFetchStrategyForBooker {
    Page<Booking> findAllByUserId(long bookerId, Pageable pageable);
}
