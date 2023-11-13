package ru.practicum.shareit.booking.pattern.strategy.impl.owner;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;

import java.util.Collection;

@Component("REJECTED_OWNER")
@RequiredArgsConstructor
public class BookingForBookerRejectedStateFetchForOwner implements BookingStateFetchStrategyForOwner {

    private final BookingRepository bookingRepository;

    @Override
    public Collection<Booking> findAllByUserId(long ownerId, Pageable pageable) {
        return bookingRepository.findAllByItemOwnerIdAndStatus(ownerId, Status.REJECTED, pageable);
    }
}
