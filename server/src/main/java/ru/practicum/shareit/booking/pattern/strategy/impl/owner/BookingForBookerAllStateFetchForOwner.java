package ru.practicum.shareit.booking.pattern.strategy.impl.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;

import java.util.Collection;

@Component("ALL_OWNER")
@RequiredArgsConstructor
public class BookingForBookerAllStateFetchForOwner implements BookingStateFetchStrategyForOwner {

    private final BookingRepository bookingRepository;

    @Override
    public Collection<Booking> findAllByUserId(long ownerId, Pageable pageable) {
        return bookingRepository.findAllByItemOwnerId(ownerId, pageable);
    }
}
