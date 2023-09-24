package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Collections;


@RestController
@RequestMapping("/items")
public class ItemController {
    private static final int MIN_ID = 1;

    private final ItemService service;

    @Autowired
    public ItemController(ItemService itemService) {
        this.service = itemService;
    }

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Valid @RequestBody ItemDto itemDto)
            throws InvalidRequestHeaderException, UserNotFoundException {
        validateRequestHeader(userId);
        return service.create(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable long itemId) throws PathNotFoundException, ItemNotFoundException {
        if (itemId < MIN_ID) {
            String message = String.format("Path \"/%d\" does not exist", itemId);
            throw new PathNotFoundException(message);
        }
        return service.findById(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody ItemDto itemDto)
            throws InvalidRequestHeaderException, UserNotFoundException, IncorrectOwnerException {
        validateRequestHeader(userId);
        itemDto.setId(itemId);
        return service.update(itemDto, userId);
    }

    @GetMapping
    public Collection<Item> findAll(@RequestHeader("X-Sharer-User-Id") long userId)
            throws InvalidRequestHeaderException, UserNotFoundException {
        validateRequestHeader(userId);
        return service.findAll(userId);
    }

    @GetMapping("/search")
    public Collection<Item> searchByName(@RequestParam String text) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyList();
        }
        return service.searchByName(text);
    }

    private void validateRequestHeader(long userId) throws InvalidRequestHeaderException {
        if (userId < MIN_ID) {
            throw new InvalidRequestHeaderException("Invalid X-Sharer-User-Id header");
        }
    }
}
