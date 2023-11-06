package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;


@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
public class BookingController {
    private final BookingService service;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public BookingDto create(@RequestHeader("X-Sharer-User-Id") @Min(1) long bookerId,
                             @Valid @RequestBody BookingDto bookingDto)
            throws
            ItemNotFoundException,
            ItemNotAvailableException,
            InvalidStartTimeException,
            InvalidEndTimeException,
            BookingNotFoundException,
            UserNotFoundException,
            InvalidBookerException {
        Booking booking = BookingMapping.toBooking(bookingDto);
        booking = service.save(booking, bookerId);
        return BookingMapping.toBookingDto(booking);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approve(@RequestHeader("X-Sharer-User-Id") @Min(1) long ownerId,
                              @Min(1) @PathVariable long bookingId,
                              @RequestParam boolean approved)
            throws BookingNotFoundException, BookingAlreadyApprovedException, InvalidOwnerException {
        Booking booking = service.approve(bookingId, ownerId, approved);
        return BookingMapping.toBookingDto(booking);
    }

    @GetMapping("/{bookingId}")
    public BookingDto findById(@RequestHeader("X-Sharer-User-Id") @Min(1) long userId,
                               @Min(1) @PathVariable long bookingId)
            throws BookingNotFoundException, UserNotAllowedAccessBookingException {
        Booking booking = service.findById(bookingId, userId);
        return BookingMapping.toBookingDto(booking);
    }

    @GetMapping
    public Collection<BookingDto> findAllByBookerId(@RequestHeader("X-Sharer-User-Id") @Min(1) long bookerId,
                                                    @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                    @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
                                                    @Min(1) @RequestParam(value = "size", defaultValue = "5") int size
    )
            throws UserNotFoundException, UnsupportedStateException {
        Collection<Booking> booking = service.findAllByBookerId(bookerId, state, from, size);
        return booking.stream()
                .map(BookingMapping::toBookingDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/owner")
    public Collection<BookingDto> findAllByOwnerId(@RequestHeader("X-Sharer-User-Id") @Min(1) long ownerId,
                                                   @RequestParam(name = "state", defaultValue = "ALL") String state,
                                                   @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
                                                   @Min(1) @RequestParam(value = "size", defaultValue = "5") int size
    )
            throws UserNotFoundException, UnsupportedStateException {
        Collection<Booking> booking = service.findAllByOwnerId(ownerId, state, from, size);
        return booking.stream()
                .map(BookingMapping::toBookingDto)
                .collect(Collectors.toList());
    }
}
