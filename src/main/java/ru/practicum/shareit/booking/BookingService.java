package ru.practicum.shareit.booking;

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
            BookingNotFoundException,
            InvalidBookerException;

    Booking approve(long bookingId, long ownerId, boolean approved)
            throws BookingNotFoundException, BookingAlreadyApprovedException, InvalidOwnerException;

    Booking findById(long bookingId, long userId)
            throws BookingNotFoundException, UserNotAllowedAccessBookingException;

    Collection<Booking> findAllByBookerId(long bookerId, String stateString, int from, int size)
            throws UserNotFoundException, UnsupportedStateException;

    Collection<Booking> findAllByOwnerId(long ownerId, String stateString, int from, int size)
            throws UserNotFoundException, UnsupportedStateException;
}
