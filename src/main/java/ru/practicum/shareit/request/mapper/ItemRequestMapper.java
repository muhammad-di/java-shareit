package ru.practicum.shareit.request.mapper;

import ru.practicum.shareit.request.dto.ItemRequestDtoForRequestBody;
import ru.practicum.shareit.request.dto.ItemRequestDtoForResponseBody;
import ru.practicum.shareit.request.model.ItemRequest;

import java.time.LocalDateTime;

public class ItemRequestMapper {
    public static ItemRequestDtoForResponseBody toItemRequestDtoForResponseBody(ItemRequest itemRequest) {
        return ItemRequestDtoForResponseBody.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .created(itemRequest.getCreated())
                .build();
    }

    public static ItemRequest toItemRequest(ItemRequestDtoForRequestBody dto) {
        return ItemRequest.builder()
                .description(dto.getDescription())
                .created(LocalDateTime.now())
                .build();
    }
}
