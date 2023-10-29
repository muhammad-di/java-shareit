package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    @Modifying
    @Query("update Item i " +
            "set i.available = :available " +
            "where i.id = :id")
    void updateAvailable(@Param("available") boolean available, @Param("id") long id);

    @Modifying
    @Query("update Item i " +
            "set i.description = :description " +
            "where i.id = :id")
    void updateDescription(@Param("description") String description, @Param("id") long id);

    @Modifying
    @Query("update Item i " +
            "set i.name = :name " +
            "where i.id = :id")
    void updateName(@Param("name") String name, @Param("id") long id);

    List<Item> findAllByOwnerIdOrderById(long id);

    @Query(value = "select * from items i " +
            "join bookings b on b.item_id  = i.id " +
            "where b.booker_id  = :id " +
            "order by b.id desc", nativeQuery = true)
    List<Item> findAllByBookerIdOrderById(long id);

    List<Item> findAllByDescriptionContainsIgnoreCaseAndAvailableTrue(String text);
}
