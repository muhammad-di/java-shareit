package ru.practicum.shareit.booking.pattern.strategy.impl.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;

import java.time.LocalDateTime;
import java.util.Collection;

@Component("CURRENT_OWNER")
@RequiredArgsConstructor
public class BookingForBookerCurrentStateFetchForOwner implements BookingStateFetchStrategyForOwner {

    private final BookingRepository bookingRepository;

    @Override
    public Collection<Booking> findAllByUserId(long ownerId, Pageable pageable) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return bookingRepository
                .findAllByItemOwnerIdAndStartBeforeAndEndAfter(
                        ownerId,
                        currentDateTime,
                        currentDateTime,
                        pageable
                );
    }
}
