package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                         @RequestBody @Valid ItemDto requestDto) {
        log.info("Creating item {}, ownerId={}", requestDto, ownerId);
        return itemClient.createItem(ownerId, requestDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@Positive @PathVariable long itemId,
                                         @Positive @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                         @RequestBody ItemDto itemDto) {
        log.info("Patching item with ownerId={}, itemId={}, body={}", ownerId, itemId, itemDto);
        itemDto.setId(itemId);
        return itemClient.update(itemId, ownerId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@Positive @PathVariable long itemId,
                                          @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Get item itemId={}, userId={}", itemId, userId);
        return itemClient.getItem(userId, itemId);
    }


    @GetMapping
    public ResponseEntity<Object> findAllByOwnerId(@Positive @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        log.info("Get items by owner ownerId={}", ownerId);
        return itemClient.findAllByOwnerId(ownerId);
    }


    @GetMapping("/search")
    public ResponseEntity<Object> searchByName(@RequestParam String text,
                                               @Positive @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Search item by name text={}, userId={}", text, userId);
        return itemClient.searchByName(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@Positive @PathVariable long itemId,
                                                @Positive @RequestHeader("X-Sharer-User-Id") Long bookerId,
                                                @RequestBody @Valid CommentDto requestDto) {
        log.info("Creating comment {}, itemId={}, bookerId={}", requestDto, itemId, bookerId);
        return itemClient.createComment(bookerId, itemId, requestDto);
    }
}
