package ru.practicum.shareit.booking.validation;

import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDateTime;

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

    public static Boolean validateForApprove(Booking booking, long userIdClaimsBeingOwner)
            throws InvalidOwnerException, BookingAlreadyApprovedException {
        return validateOwner(booking, userIdClaimsBeingOwner)
                && validateStatusNotAlreadyApproved(booking);

    }

    public static Boolean validateForFindById(Booking booking, long userId)
            throws UserNotAllowedAccessBookingException {
        return validateOwnerOrBooker(booking, userId);

    }

    public static String validateStateForBooker(String str) throws UnsupportedStateException {
        return validateState(str, "_BOOKER");
    }

    public static String validateStateForOwner(String str) throws UnsupportedStateException {
        return  validateState(str, "_OWNER");
    }

    private static String validateState(String str, String userType) throws UnsupportedStateException {
        try {
            State state = State.valueOf(str);
            return str.concat(userType);
        } catch (IllegalArgumentException e) {
            String message = "Unknown state: " + str;
            throw new UnsupportedStateException(message);
        }
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
        LocalDateTime timeNow = LocalDateTime.now();
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
        LocalDateTime timeNow = LocalDateTime.now();
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

    private static Boolean validateOwner(Booking booking, long userIdClaimsBeingOwner) throws InvalidOwnerException {
        long ownerId = booking.getItem().getOwner().getId();
        if (ownerId != userIdClaimsBeingOwner) {
            String message = String.format("a wrong owner exception");
            throw new InvalidOwnerException(message);
        } else {
            return true;
        }
    }

    private static Boolean validateStatusNotAlreadyApproved(Booking booking) throws BookingAlreadyApprovedException {
        Status status = booking.getStatus();
        if (status.equals(Status.APPROVED)) {
            String message = String.format("a booking is already approved");
            throw new BookingAlreadyApprovedException(message);
        } else {
            return true;
        }
    }

    private static Boolean validateOwnerOrBooker(Booking booking, long userId) throws UserNotAllowedAccessBookingException {
        long ownerId = booking.getItem().getOwner().getId();
        long bookerId = booking.getBooker().getId();
        if (userId == ownerId || userId == bookerId) {
            return true;
        } else {
            String message = String.format("a user with id { %d } can not access this booking", userId);
            throw new UserNotAllowedAccessBookingException(message);
        }
    }
}
