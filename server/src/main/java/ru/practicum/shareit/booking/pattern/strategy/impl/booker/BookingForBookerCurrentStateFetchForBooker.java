package ru.practicum.shareit.booking.pattern.strategy.impl.booker;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.storage.BookingRepository;

import java.time.LocalDateTime;

@Component("CURRENT_BOOKER")
@RequiredArgsConstructor
public class BookingForBookerCurrentStateFetchForBooker implements BookingStateFetchStrategyForBooker {

    private final BookingRepository bookingRepository;

    @Override
    public Page<Booking> findAllByUserId(long bookerId, Pageable pageable) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return bookingRepository
                .findAllByBookerIdAndStartBeforeAndEndAfter(
                        bookerId,
                        currentDateTime,
                        currentDateTime,
                        pageable
                );
    }
}
