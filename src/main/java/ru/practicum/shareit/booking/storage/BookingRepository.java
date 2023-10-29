package ru.practicum.shareit.booking.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findAllByBookerIdOrderByIdDesc(long id);

    List<Booking> findAllByBookerIdAndStatusOrderByIdDesc(long id, Status status);

    List<Booking> findAllByBooker_IdAndStartAfterOrderByIdDesc(long id, LocalDateTime current);

    List<Booking> findAllByBooker_IdAndStartLessThanEqualAndEndGreaterThanEqualOrderById(long bookerId,
                                                                                         LocalDateTime currentStar,
                                                                                         LocalDateTime currentEnd);

    List<Booking> findAllByBooker_IdAndEndLessThanEqualOrderByStartDesc(long bookerId, LocalDateTime current);

    List<Booking> findAllByItem_IdAndItemOwnerIdAndStatusAndStartLessThanOrderById(long itemId,
                                                                                   long ownerId,
                                                                                   Status status,
                                                                                   LocalDateTime currentDateTime);

    List<Booking> findAllByItem_IdAndBookerIdAndStatusAndStartLessThanEqualAndEndGreaterThanOrderById(long itemId,
                                                                                                      long ownerId,
                                                                                                      Status status,
                                                                                                      LocalDateTime currentDateTimeStart,
                                                                                                      LocalDateTime currentDateTimeForEnd);

    Booking findByIdAndBookerId(long bookingId, long bookerOrOwnerId);

    List<Booking> findAllByItemIdAndBookerIdAndStatus(long itemId, long bookerId, Status status);

    @Query(value = "select * from bookings b " +
            "join items i on b.item_id  = i.id " +
            "where b.id  = :bookingId " +
            "and i.owner_id = :ownerId " +
            "order by b.id desc", nativeQuery = true)
    Booking findBookingByIdAndOwnerId(@Param("bookingId") long bookingId, @Param("ownerId") long ownerId);

    @Query(value = "select * from bookings b " +
            "join items i on b.item_id  = i.id " +
            "where b.id  = :bookingId " +
            "and i.owner_id = :ownerId " +
            "order by b.id desc", nativeQuery = true)
    Optional<Booking> findBookingOptionalByIdAndOwnerId(@Param("bookingId") long bookingId,
                                                        @Param("ownerId") long ownerId);


    @Query(value = "select * from bookings b " +
            "join items i on b.item_id  = i.id " +
            "where i.owner_id  = :ownerId " +
            "order by b.id desc", nativeQuery = true)
    List<Booking> findBookingByOwnerId(@Param("ownerId") long ownerId);

    @Query(value = "select * from bookings b " +
            "join items i on b.item_id  = i.id " +
            "where i.owner_id  = :ownerId " +
            "AND b.status  = :status " +
            "order by b.id", nativeQuery = true)
    List<Booking> findBookingByOwnerIdAndStatus(@Param("ownerId") long ownerId, @Param("status") String status);

    @Query(value = "select * from bookings b " +
            "join items i on b.item_id  = i.id " +
            "where i.owner_id  = :id " +
            "and b.start_date > :current " +
            "order by b.id desc", nativeQuery = true)
    List<Booking> findBookingByOwnerIdAndStartAfterOrderByIdDesc(@Param("id") long id,
                                                                 @Param("current") LocalDateTime current);

    List<Booking> findAllByItem_OwnerIdAndStartLessThanEqualAndEndGreaterThanEqualOrderByStart(long ownerId,
                                                                                               LocalDateTime currentStar,
                                                                                               LocalDateTime currentEnd);

    List<Booking> findAllByItem_OwnerIdAndEndLessThanEqualOrderByStartDesc(long ownerId, LocalDateTime current);
}
