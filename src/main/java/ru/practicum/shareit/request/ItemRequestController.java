package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDtoForRequestBody;
import ru.practicum.shareit.request.dto.ItemRequestDtoForResponseBody;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
@Validated
public class ItemRequestController {
    private final ItemRequestService service;

    @PostMapping
    public ItemRequestDtoForResponseBody create(@RequestHeader("X-Sharer-User-Id") @Min(1) long requestorId,
                                                @Valid @RequestBody ItemRequestDtoForRequestBody dto)
            throws UserNotFoundException {
        ItemRequest request = ItemRequestMapper.toItemRequest(dto);
        request = service.save(request, requestorId);
        return ItemRequestMapper.toItemRequestDtoForResponseBody(request);
    }

    @GetMapping("/{requestId}")
    public ItemRequestDtoForResponseBody findById(@PathVariable @Min(1) long requestId,
                                                  @RequestHeader("X-Sharer-User-Id") @Min(1) long requestorId
    ) throws ItemRequestNotFoundException, UserNotFoundException {
        return service.findById(requestId, requestorId);
    }

    @GetMapping
    public Collection<ItemRequestDtoForResponseBody> findAllByRequestorId(
            @RequestHeader("X-Sharer-User-Id") @Min(1) long requestorId
    )
            throws UserNotFoundException {
        return service.findAllByRequestorId(requestorId);
    }

    @GetMapping("/all")
    public Collection<ItemRequestDtoForResponseBody> findAll(
            @RequestHeader("X-Sharer-User-Id") @Min(1) long requestorId,
            @Min(0) @RequestParam(value = "from", defaultValue = "0") int from,
            @Min(1) @RequestParam(value = "size", defaultValue = "1") int size
    ) {
        return service.findAll(requestorId, from, size);
    }
}
