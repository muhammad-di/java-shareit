package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDtoForResponseBody;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import java.util.Collection;


public interface ItemRequestService {

    ItemRequest save(ItemRequest request, long requestorId) throws UserNotFoundException;

    ItemRequestDtoForResponseBody findById(long requestId, long requestorId)
            throws UserNotFoundException, ItemRequestNotFoundException;

    Collection<ItemRequestDtoForResponseBody> findAllByRequestorId(long requestorId) throws UserNotFoundException;

    Collection<ItemRequestDtoForResponseBody> findAll(long requestorId, int from, int size);
}
