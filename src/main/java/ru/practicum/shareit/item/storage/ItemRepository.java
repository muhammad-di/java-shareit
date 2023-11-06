package ru.practicum.shareit.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwnerIdOrderById(long id);

    List<Item> findAllByDescriptionContainsIgnoreCaseAndAvailableTrue(String text);

    List<Item> findAllByRequestId(long requestId);
}
