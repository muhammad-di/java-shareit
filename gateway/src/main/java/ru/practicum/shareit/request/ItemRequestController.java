package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoForRequestBody;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> requestItem(@Positive @RequestHeader("X-Sharer-User-Id") Long requestorId,
                                              @RequestBody @Valid ItemRequestDtoForRequestBody requestDto) {
        log.info("Creating item request {}, requestorId={}", requestDto, requestorId);
        return itemRequestClient.requestItem(requestorId, requestDto);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequest(@Positive @RequestHeader("X-Sharer-User-Id") Long requestorId,
                                                 @Positive @PathVariable long requestId) {
        log.info("Get item request with requestId={}, requestorId={}", requestId, requestorId);
        return itemRequestClient.getItemRequest(requestorId, requestId);
    }


    @GetMapping
    public ResponseEntity<Object> findAllByRequestorId(@Positive @RequestHeader("X-Sharer-User-Id") Long requestorId) {
        log.info("Get item requests with requestorId={}", requestorId);
        return itemRequestClient.findAllByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@Positive @RequestHeader("X-Sharer-User-Id") long requestorId,
                                          @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                          @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("Get item requests userId={}, from={}, size={}", requestorId, from, size);
        return itemRequestClient.findAll(requestorId, from, size);
    }
}