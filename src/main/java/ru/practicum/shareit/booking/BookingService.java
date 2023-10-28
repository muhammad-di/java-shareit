package ru.practicum.shareit.booking;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;

public interface BookingService {
    Booking save(Booking booking, long bookerId)
            throws
            ItemNotFoundException,
            ItemNotAvailableException,
            InvalidStartTimeException,
            InvalidEndTimeException,
            UserNotFoundException,
            BookingNotFoundException, InvalidBookerException;

    Booking approve(long bookingId, long ownerId, boolean approved) throws BookingNotFoundException, BookingAlreadyApprovedException;

    Booking findByBookerOrOwnerId(long bookingId, long bookerOrOwnerId) throws BookingNotFoundException, UserNotFoundException;

    Collection<Booking> findAllByBookerId(long bookerId, String stateString) throws UserNotFoundException, UnsupportedStateException;

    Collection<Booking> findAllByOwnerId(long ownerId, String stateString) throws UserNotFoundException, UnsupportedStateException;
}
