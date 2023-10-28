package ru.practicum.shareit.item;

import org.springframework.transaction.annotation.Transactional;
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
    @Transactional(readOnly = true)
    Collection<ItemDto> findAllByOwnerId(long userId) throws UserNotFoundException;

    @Transactional(readOnly = true)
    Collection<Item> searchByName(String name);

    @Transactional(readOnly = true)
    ItemDto findById(long itemId, long ownerId) throws ItemNotFoundException;

    @Transactional
    Item save(Item user, long userId) throws UserNotFoundException;

    @Transactional
    Item update(Item item, long userId) throws UserNotFoundException, ItemNotFoundException, IncorrectOwnerException;

    @Transactional
    Comment createComment(Comment comment) throws ItemNotFoundException, IncorrectBookerException, BookingInFutureException;
}
