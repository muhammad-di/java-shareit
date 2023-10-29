package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.exception.BookingInFutureException;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectBookerException;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;


public interface ItemService {
    Collection<ItemDto> findAllByUserId(long userId) throws UserNotFoundException;

    Collection<Item> searchByName(String name);

    ItemDto findById(long itemId, long ownerId) throws ItemNotFoundException;

    Item save(Item user, long userId) throws UserNotFoundException;

    Item update(Item item, long userId) throws UserNotFoundException, ItemNotFoundException, IncorrectOwnerException;

    Comment createComment(Comment comment) throws ItemNotFoundException, IncorrectBookerException, BookingInFutureException;
}
