package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.exception.InvalidBookerException;
import ru.practicum.shareit.booking.exception.InvalidEndTimeException;
import ru.practicum.shareit.booking.exception.InvalidStartTimeException;
import ru.practicum.shareit.booking.exception.ItemNotAvailableException;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class BookingValidation {
    public static Boolean validate(Booking booking)
            throws ItemNotAvailableException, InvalidEndTimeException, InvalidStartTimeException, InvalidBookerException {
        return validateItemAvailability(booking)
                && validateEndBeforeCurrentTime(booking)
                && validateEndBeforeStart(booking)
                && validateEndEqualStart(booking)
                && validateStartBeforeCurrentTime(booking)
                && validateOwnerBookerNotSame(booking);

    }

    public static Boolean validateItemAvailability(Booking booking) throws ItemNotAvailableException {
        Item item = booking.getItem();
        if (!item.getAvailable()) {
            String message = String.format("an item with id { %d } is not available", item.getId());
            throw new ItemNotAvailableException(message);
        } else {
            return true;
        }
    }

    private static Boolean validateEndBeforeCurrentTime(Booking booking) throws InvalidEndTimeException {
        LocalDateTime end = booking.getEnd();
        LocalDateTime timeNow = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        if (timeNow.isAfter(end)) {
            String message = String.format("end time is before current time");
            throw new InvalidEndTimeException(message);
        } else {
            return true;
        }
    }

    private static Boolean validateEndBeforeStart(Booking booking) throws InvalidEndTimeException {
        LocalDateTime end = booking.getEnd();
        LocalDateTime start = booking.getStart();
        if (end.isBefore(start)) {
            String message = String.format("end time is before start time");
            throw new InvalidEndTimeException(message);
        } else {
            return true;
        }
    }

    private static Boolean validateEndEqualStart(Booking booking) throws InvalidEndTimeException {
        LocalDateTime end = booking.getEnd();
        LocalDateTime start = booking.getStart();
        if (end.isEqual(start)) {
            String message = String.format("end and start time are the same");
            throw new InvalidEndTimeException(message);
        } else {
            return true;
        }
    }

    private static Boolean validateStartBeforeCurrentTime(Booking booking) throws InvalidStartTimeException {
        LocalDateTime start = booking.getStart();
        LocalDateTime timeNow = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        if (timeNow.isAfter(start)) {
            String message = String.format("start time is before current time");
            throw new InvalidStartTimeException(message);
        } else {
            return true;
        }
    }

    private static Boolean validateOwnerBookerNotSame(Booking booking) throws InvalidBookerException {
        long ownerId = booking.getItem().getOwner().getId();
        long bookerId = booking.getBooker().getId();
        if (ownerId == bookerId) {
            String message = String.format("owner of an item and booker of the item can not be same user");
            throw new InvalidBookerException(message);
        } else {
            return true;
        }
    }
}
