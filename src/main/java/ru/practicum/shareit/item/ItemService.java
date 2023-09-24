package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;

@Service
public class ItemService {
    private final ItemRepository repository;
    private final UserRepository userRepository;

    @Autowired
    public ItemService(ItemRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public ItemDto create(ItemDto itemDto, long userId) throws UserNotFoundException {
        containsOwnerId(userId);
        Item item = ItemMapper.toItem(itemDto, userId);
        item = repository.create(item);
        return ItemMapper.toItemDto(item);
    }

    public ItemDto findById(long id) throws ItemNotFoundException {
        Item item = repository.findById(id);
        if (item == null) {
            String message = String.format("an item with id { %d } does not exist", id);
            throw new ItemNotFoundException(message);
        }
        return ItemMapper.toItemDto(item);
    }

    public ItemDto update(ItemDto itemDto, long userId) throws UserNotFoundException, IncorrectOwnerException {
        containsOwnerId(userId);
        Item item = ItemMapper.toItem(itemDto, userId);
        item = repository.update(item);
        return ItemMapper.toItemDto(item);
    }

    public Collection<Item> findAll(long userId) throws UserNotFoundException {
        containsOwnerId(userId);
        return repository.findAll(userId);
    }

    public Collection<Item> searchByName(String text) {
        return repository.searchByName(text);
    }

    private void containsOwnerId(long userId) throws UserNotFoundException {
        if (!userRepository.contains(userId)) {
            String message = String.format("a user with id { %d } does not exist", userId);
            throw new UserNotFoundException(message);
        }
    }
}
