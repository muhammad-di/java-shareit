package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.InvalidRequestHeaderException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.PathNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class ItemService {

    private static final int MIN_ID = 1;
    private final ItemRepository repository;
    private final UserRepository userRepository;

    public ItemDto create(ItemDto itemDto, long userId) throws UserNotFoundException, InvalidRequestHeaderException {
        validateRequestHeader(userId);
        containsOwnerId(userId);
        Item item = ItemMapper.toItem(itemDto, userId);
        item = repository.create(item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto findById(long id) throws ItemNotFoundException, PathNotFoundException {
        if (id < MIN_ID) {
            String message = String.format("Path \"/%d\" does not exist", id);
            throw new PathNotFoundException(message);
        }
        Item item = repository.findById(id);
        if (item == null) {
            String message = String.format("an item with id { %d } does not exist", id);
            throw new ItemNotFoundException(message);
        }
        return ItemMapper.toItemDto(item);
    }

    public ItemDto update(ItemDto itemDto, long userId, long itemId)
            throws UserNotFoundException, IncorrectOwnerException, InvalidRequestHeaderException {
        validateRequestHeader(userId);
        containsOwnerId(userId);
        itemDto.setId(itemId);
        Item item = ItemMapper.toItem(itemDto, userId);
        item = repository.update(item);
        return ItemMapper.toItemDto(item);
    }

    public Collection<Item> findAll(long userId) throws UserNotFoundException, InvalidRequestHeaderException {
        validateRequestHeader(userId);
        containsOwnerId(userId);
        return repository.findAll(userId);
    }

    public Collection<Item> searchByName(String text) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyList();
        }
        return repository.searchByName(text);
    }

    private void containsOwnerId(long userId) throws UserNotFoundException {
        if (!userRepository.contains(userId)) {
            String message = String.format("a user with id { %d } does not exist", userId);
            throw new UserNotFoundException(message);
        }
    }

    private void validateRequestHeader(long userId) throws InvalidRequestHeaderException {
        if (userId < MIN_ID) {
            throw new InvalidRequestHeaderException("Invalid X-Sharer-User-Id header");
        }
    }
}
