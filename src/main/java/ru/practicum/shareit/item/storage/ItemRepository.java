package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;

public interface ItemRepository {
    Item create(Item item);

    boolean contains(long id);

    Item findById(long id);

    Item update(Item item) throws IncorrectOwnerException;

    Collection<Item> findAll(long userId);

    Collection<Item> searchByName(String text);
}
