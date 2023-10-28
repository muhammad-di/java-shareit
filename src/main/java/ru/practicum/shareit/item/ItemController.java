package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.exception.BookingInFutureException;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectBookerException;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
public class ItemController {
    private final ItemService service;

    @PostMapping
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") @Min(1) long userId,
                          @Valid @RequestBody ItemDto itemDto)
            throws UserNotFoundException {
        Item item = ItemMapper.toItem(itemDto);
        item = service.save(item, userId);
        return ItemMapper.toItemDto(item);
    }


    @GetMapping("/{itemId}")
    public ItemDto findById(@PathVariable @Min(1) long itemId, @RequestHeader("X-Sharer-User-Id") @Min(1) long ownerId
    ) throws ItemNotFoundException {
        return service.findById(itemId, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable @Min(1) long itemId,
                          @RequestHeader("X-Sharer-User-Id") @Min(1) long userId,
                          @RequestBody ItemDto itemDto)
            throws UserNotFoundException, IncorrectOwnerException, ItemNotFoundException {
        Item item = ItemMapper.toItem(itemDto);
        item.setId(itemId);
        item = service.update(item, userId);
        return ItemMapper.toItemDto(item);
    }

    @GetMapping
    public Collection<ItemDto> findAllByOwnerId(@RequestHeader("X-Sharer-User-Id") @Min(1) long userId)
            throws UserNotFoundException {
        return service.findAllByOwnerId(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchByName(@RequestParam String text) {
        return service.searchByName(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable @Min(1) long itemId,
                                    @RequestHeader("X-Sharer-User-Id") @Min(1) long bookerId,
                                    @Valid @RequestBody CommentDto commentDto)
            throws ItemNotFoundException, IncorrectBookerException, BookingInFutureException {
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(itemId);
        comment.setAuthor(bookerId);
        Comment commentResponse = service.createComment(comment);
        return CommentMapper.toCommentDto(commentResponse);
    }
}
