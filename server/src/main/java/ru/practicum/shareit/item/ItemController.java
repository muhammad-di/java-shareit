package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.exception.IncorrectBookerException;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.constraints.Min;
import java.util.Collection;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService service;

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ItemDto create(@RequestHeader("X-Sharer-User-Id") long ownerId,
                          @RequestBody ItemDto itemDto)
            throws UserNotFoundException, ItemRequestNotFoundException {
        Item item = ItemMapper.toItem(itemDto);
        item = service.save(item, ownerId);
        return ItemMapper.toItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@PathVariable long itemId,
                          @RequestHeader("X-Sharer-User-Id") long ownerId,
                          @RequestBody ItemDto itemDto)
            throws UserNotFoundException, IncorrectOwnerException, ItemNotFoundException {
        itemDto.setId(itemId);
        Item item = service.update(itemDto, ownerId);
        return ItemMapper.toItemDto(item);
    }


    @GetMapping("/{itemId}")
    public ItemDtoForGet findById(@PathVariable long itemId,
                                  @RequestHeader("X-Sharer-User-Id") long userId
    ) throws ItemNotFoundException {
        return service.findById(itemId, userId);
    }

    @GetMapping
    public Collection<ItemDtoForGet> findAllByOwnerId(@RequestHeader("X-Sharer-User-Id") long userId)
            throws UserNotFoundException {
        return service.findAllByOwnerId(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> searchByName(@RequestParam String text) {
        return service.searchByDescription(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@PathVariable long itemId,
                                    @RequestHeader("X-Sharer-User-Id") @Min(1) long bookerId,
                                    @RequestBody CommentDto commentDto)
            throws ItemNotFoundException, IncorrectBookerException {
        Comment comment = CommentMapper.toComment(commentDto);
        comment.setItem(itemId);
        comment.setAuthor(bookerId);
        Comment commentResponse = service.createComment(comment);
        return CommentMapper.toCommentDto(commentResponse);
    }
}
