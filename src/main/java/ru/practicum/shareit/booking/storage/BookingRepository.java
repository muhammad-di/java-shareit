package ru.practicum.shareit.booking.storage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Page<Booking> findAllByBookerId(long bookerId, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(long bookerId,
                                                             LocalDateTime start,
                                                             LocalDateTime end,
                                                             Pageable pageable);

    Page<Booking> findAllByBookerIdAndStartAfter(long bookerId, LocalDateTime current, Pageable pageable);

    Page<Booking> findAllByBookerIdAndEndBefore(long bookerId, LocalDateTime current, Pageable pageable);

    Page<Booking> findAllByBookerIdAndStatus(long bookerId, Status status, Pageable pageable);

    List<Booking> findAllByItemOwnerId(long ownerId, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartBeforeAndEndAfter(long ownerId,
                                                                LocalDateTime start,
                                                                LocalDateTime end,
                                                                Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStartAfter(long ownerId, LocalDateTime current, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndEndBefore(long ownerId, LocalDateTime current, Pageable pageable);

    List<Booking> findAllByItemOwnerIdAndStatus(long ownerId, Status status, Pageable pageable);


    List<Booking> findAllByItemIdAndStatusAndStartBeforeOrderByEndDesc(long itemId,
                                                                       Status status,
                                                                       LocalDateTime currentDateTime);

    List<Booking> findAllByItemIdAndStatusAndStartAfterOrderByStartAsc(long itemId,
                                                                       Status status,
                                                                       LocalDateTime currentDateTime);

    List<Booking> findByItemIdAndBookerIdAndStatusAndStartBefore(long itemId,
                                                                 long bookerId,
                                                                 Status status,
                                                                 LocalDateTime currentDateTime);
}
