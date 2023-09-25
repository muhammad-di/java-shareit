package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private static final int MIN_ID = 1;
    private final ItemService service;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long userId,
                          @Valid @RequestBody ItemDto itemDto)
            throws InvalidRequestHeaderException, UserNotFoundException {
        return service.create(itemDto, userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable long itemId) throws PathNotFoundException, ItemNotFoundException {
        return service.findById(itemId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId,
                          @RequestHeader("X-Sharer-User-Id") long userId,
                          @RequestBody ItemDto itemDto)
            throws InvalidRequestHeaderException, UserNotFoundException, IncorrectOwnerException {
        return service.update(itemDto, userId, itemId);
    }

    @GetMapping
    public Collection<Item> findAll(@RequestHeader("X-Sharer-User-Id") long userId)
            throws InvalidRequestHeaderException, UserNotFoundException {
        return service.findAll(userId);
    }

    @GetMapping("/search")
    public Collection<Item> searchByName(@RequestParam String text) {
        return service.searchByName(text);
    }
}
