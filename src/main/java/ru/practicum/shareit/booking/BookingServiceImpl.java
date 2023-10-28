package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Booking save(Booking booking, long bookerId)
            throws
            ItemNotFoundException,
            ItemNotAvailableException,
            InvalidStartTimeException,
            InvalidEndTimeException,
            UserNotFoundException,
            BookingNotFoundException, InvalidBookerException {
        User booker = findUserById(bookerId);
        Item item = findItemById(booking.getItem().getId());

        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        BookingValidation.validate(booking);
        bookingRepository.save(booking);
        return findBookingById(booking.getId());
    }

    @Transactional
    public Booking approve(long bookingId, long ownerId, boolean approved)
            throws BookingNotFoundException, BookingAlreadyApprovedException {
        Optional<Booking> bookingOpt = bookingRepository.findBookingOptionalByIdAndOwnerId(bookingId, ownerId);
        Booking booking = bookingOpt.orElseThrow(() -> new BookingNotFoundException("a booking does not exist"));

        if (booking.getStatus().equals(Status.APPROVED)) {
            throw new BookingAlreadyApprovedException("a booking is already approved");
        }
        setStatus(booking, approved);
        bookingRepository.save(booking);
        return findBookingById(booking.getId());
    }

    @Transactional(readOnly = true)
    public Booking findByBookerOrOwnerId(long bookingId, long bookerOrOwnerId)
            throws BookingNotFoundException, UserNotFoundException {
        findUserById(bookerOrOwnerId);
        Booking bookingByBooker = bookingRepository.findByIdAndBookerId(bookingId, bookerOrOwnerId);
        Booking bookingByOwner = bookingRepository.findBookingByIdAndOwnerId(bookingId, bookerOrOwnerId);
        if (bookingByBooker == null && bookingByOwner == null) {
            String message = String.format("a booking does not exist");
            throw new BookingNotFoundException(message);
        } else if (bookingByBooker == null) {
            return bookingByOwner;
        } else {
            return bookingByBooker;
        }
    }

    @Transactional(readOnly = true)
    public Collection<Booking> findAllByBookerId(long bookerId, String stateString)
            throws UserNotFoundException, UnsupportedStateException {
        LocalDateTime currentTime = LocalDateTime.now();
        State state = stringStateToEnumState(stateString);

        findUserById(bookerId);
        if (state.equals(State.ALL)) {
            return bookingRepository.findAllByBookerIdOrderByIdDesc(bookerId);
        } else if (state.equals(State.FUTURE)) {
            LocalDateTime current = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
            return bookingRepository.findAllByBooker_IdAndStartAfterOrderByIdDesc(bookerId, current);
        } else if (state.equals(State.WAITING) || state.equals(State.REJECTED)) {
            Status status = Status.valueOf(stateString);
            return bookingRepository.findAllByBookerIdAndStatusOrderByIdDesc(bookerId, status);
        } else if (state.equals(State.CURRENT)) {
            return bookingRepository
                    .findAllByBooker_IdAndStartLessThanEqualAndEndGreaterThanEqualOrderById(bookerId,
                            currentTime,
                            currentTime);
        } else if (state.equals(State.PAST)) {
            return bookingRepository
                    .findAllByBooker_IdAndEndLessThanEqualOrderByStartDesc(bookerId, currentTime);
        } else {
            return Collections.emptyList();
        }
    }

    @Transactional(readOnly = true)
    public Collection<Booking> findAllByOwnerId(long ownerId, String stateString)
            throws UserNotFoundException, UnsupportedStateException {
        LocalDateTime currentTime = LocalDateTime.now();
        State state = stringStateToEnumState(stateString);

        findUserById(ownerId);
        if (state.equals(State.ALL)) {
            return bookingRepository.findBookingByOwnerId(ownerId);
        } else if (state.equals(State.FUTURE)) {
            LocalDateTime current = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
            return bookingRepository.findBookingByOwnerIdAndStartAfterOrderByIdDesc(ownerId, current);
        } else if (state.equals(State.WAITING) || state.equals(State.REJECTED)) {
            return bookingRepository.findBookingByOwnerIdAndStatus(ownerId, stateString);
        } else if (state.equals(State.CURRENT)) {
            return bookingRepository
                    .findAllByItem_OwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStart(ownerId,
                            currentTime,
                            currentTime);
        } else if (state.equals(State.PAST)) {
            return bookingRepository
                    .findAllByItem_OwnerIdAndEndLessThanEqualOrderByStartDesc(ownerId, currentTime);
        } else {
            return Collections.emptyList();
        }
    }


    private User findUserById(long bookerId) throws UserNotFoundException {
        return userRepository.findById(bookerId).orElseThrow(() -> {
            String message = String.format("a user with id { %d } does not exist", bookerId);
            return new UserNotFoundException(message);
        });


    }

    private Booking findBookingById(long bookingId) throws BookingNotFoundException {
        return bookingRepository.findById(bookingId).orElseThrow(() -> {
            String message = String.format("a booking with id { %d } does not exist", bookingId);
            return new BookingNotFoundException(message);
        });


    }

    private Item findItemById(long itemId) throws ItemNotFoundException {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> {
                    String message = String.format("an item with id { %d } does not exist", itemId);
                    return new ItemNotFoundException(message);
                });
    }

    private void setStatus(Booking booking, boolean approved) {
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else {
            booking.setStatus(Status.REJECTED);
        }
    }

    private State stringStateToEnumState(String str) throws UnsupportedStateException {
        try {
            return State.valueOf(str);

        } catch (IllegalArgumentException e) {
            String message = "Unknown state: " + str;
            throw new UnsupportedStateException(message);
        }
    }
}
