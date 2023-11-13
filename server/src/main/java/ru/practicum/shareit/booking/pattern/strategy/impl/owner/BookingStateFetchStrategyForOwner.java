package ru.practicum.shareit.booking.pattern.strategy.impl.owner;

import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.model.Booking;

import java.util.Collection;

public interface BookingStateFetchStrategyForOwner {
    Collection<Booking> findAllByUserId(long ownerId, Pageable pageable);
}
