package ru.practicum.shareit.item;

import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.exception.IncorrectBookerException;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;


public interface ItemService {

    Item save(Item item, long ownerId) throws UserNotFoundException, ItemRequestNotFoundException;

    Item update(ItemDto itemDto, long ownerId) throws ItemNotFoundException, IncorrectOwnerException;

    ItemDtoForGet findById(long itemId, long userId) throws ItemNotFoundException;

    Collection<ItemDtoForGet> findAllByOwnerId(long ownerId) throws UserNotFoundException;

    Collection<Item> searchByName(String name);

    Comment createComment(Comment comment) throws ItemNotFoundException, IncorrectBookerException;
}
