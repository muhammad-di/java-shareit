package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.pattern.strategy.impl.booker.BookingStateFetchStrategyForBooker;
import ru.practicum.shareit.booking.pattern.strategy.impl.owner.BookingStateFetchStrategyForOwner;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.booking.validation.BookingValidation;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final Map<String, BookingStateFetchStrategyForBooker> bookingStateFetchStrategyForBookerMap;
    private final Map<String, BookingStateFetchStrategyForOwner> bookingStateFetchStrategyForOwnerMap;


    @Transactional
    public Booking save(Booking booking, long bookerId)
            throws
            ItemNotFoundException,
            ItemNotAvailableException,
            InvalidStartTimeException,
            InvalidEndTimeException,
            UserNotFoundException,
            BookingNotFoundException,
            InvalidBookerException {
        User booker = findUserById(bookerId);
        Item item = findItemById(booking.getItem().getId());

        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(Status.WAITING);
        BookingValidation.validate(booking);
        return bookingRepository.save(booking);
    }

    @Transactional
    public Booking approve(long bookingId, long ownerId, boolean approved)
            throws BookingAlreadyApprovedException, InvalidOwnerException, BookingNotFoundException {
        Booking booking = findBookingById(bookingId);

        BookingValidation.validateForApprove(booking, ownerId);
        setStatus(booking, approved);
        return bookingRepository.save(booking);
    }

    @Transactional(readOnly = true)
    public Booking findById(long bookingId, long userId)
            throws BookingNotFoundException, UserNotAllowedAccessBookingException {
        Booking booking = findBookingById(bookingId);

        BookingValidation.validateForFindById(booking, userId);
        return booking;
    }

    @Transactional(readOnly = true)
    public Collection<Booking> findAllByBookerId(long bookerId, String stateString, int from, int size)
            throws UserNotFoundException, UnsupportedStateException {
        String state = BookingValidation.validateStateForBooker(stateString);
        Pageable sortedByStartDesc = getPages(from, size);
        existsUserById(bookerId);
        BookingStateFetchStrategyForBooker strategy = bookingStateFetchStrategyForBookerMap.get(state);
        if (strategy != null) {
            Page<Booking> page = strategy.findAllByUserId(bookerId, sortedByStartDesc);
            Collection<Booking> list = findAllWhenSizeTooBig(bookerId, state, page);
            if (list == null) {
                return page.stream().collect(Collectors.toList());
            }
            return list;
        }
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    public Collection<Booking> findAllByOwnerId(long ownerId, String stateString, int from, int size)
            throws UserNotFoundException, UnsupportedStateException {
        String state = BookingValidation.validateStateForOwner(stateString);
        Pageable sortedByStartDesc = getPages(from, size);

        existsUserById(ownerId);
        BookingStateFetchStrategyForOwner strategy = bookingStateFetchStrategyForOwnerMap.get(state);
        if (strategy != null) {
            return strategy.findAllByUserId(ownerId, sortedByStartDesc);
        }
        return Collections.emptyList();
    }


    private User findUserById(long bookerId) throws UserNotFoundException {
        return userRepository.findById(bookerId).orElseThrow(() -> {
            String message = String.format("a user with id { %d } does not exist", bookerId);
            return new UserNotFoundException(message);
        });
    }

    private void existsUserById(long bookerId) throws UserNotFoundException {
        if (!userRepository.existsById(bookerId)) {
            String message = String.format("a user with id { %d } does not exist", bookerId);
            throw new UserNotFoundException(message);
        }
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

    private Collection<Booking> findAllWhenSizeTooBig(long bookerId, String state, Page<Booking> page) {
        int totalPages = page.getTotalPages();
        int requestedPage = page.getNumber();
        if (totalPages < requestedPage) {
            int from = totalPages - 1;
            int size = page.getSize();

            Pageable sortedByStartDesc = PageRequest.of(from, size, Sort.by("start").descending());
            return bookingStateFetchStrategyForBookerMap.get(state)
                    .findAllByUserId(bookerId, sortedByStartDesc)
                    .stream()
                    .collect(Collectors.toList());
        }
        return null;
    }

    private Pageable getPages(int from, int size) {
        return PageRequest.of(from, size, Sort.by("start").descending());
    }


}