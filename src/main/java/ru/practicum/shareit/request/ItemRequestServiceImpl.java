package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDtoForResponseBody;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public ItemRequest save(ItemRequest request, long requestorId) throws UserNotFoundException {
        User requestor = getUser(requestorId);
        request.setRequestor(requestor);

        return itemRequestRepository.save(request);
    }

    @Transactional(readOnly = true)
    public ItemRequestDtoForResponseBody findById(long requestId, long requestorId)
            throws UserNotFoundException, ItemRequestNotFoundException {
        existsUserById(requestorId);
        ItemRequest request = getItemRequest(requestId);
        ItemRequestDtoForResponseBody dto = ItemRequestMapper.toItemRequestDtoForResponseBody(request);

        setItems(dto);
        return dto;
    }

    @Transactional(readOnly = true)
    public Collection<ItemRequestDtoForResponseBody> findAllByRequestorId(long requestorId) throws UserNotFoundException {
        existsUserById(requestorId);
        return itemRequestRepository.findAllByRequestorIdOrderById(requestorId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDtoForResponseBody)
                .peek(this::setItems)
                .collect(Collectors.toList());
    }


    @Transactional(readOnly = true)
    public Collection<ItemRequestDtoForResponseBody> findAll(long requestorId, int from, int size) {
        Pageable sortedByCreatedDesc =
                PageRequest.of(from, size, Sort.by("created").descending());
        return itemRequestRepository.findAllByRequestorIdIsNotOrderById(requestorId, sortedByCreatedDesc)
                .stream()
                .map(ItemRequestMapper::toItemRequestDtoForResponseBody)
                .peek(this::setItems)
                .collect(Collectors.toList());
    }


    private void existsUserById(long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            String message = String.format("a user with id { %d } does not exist", userId);
            throw new UserNotFoundException(message);
        }
    }


    private User getUser(long userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    String message = String.format("a user with id { %d } does not exist", userId);
                    return new UserNotFoundException(message);
                });
    }

    private ItemRequest getItemRequest(long requestId) throws ItemRequestNotFoundException {
        return itemRequestRepository.findById(requestId)
                .orElseThrow(() -> {
                    String message = String.format("an item request with id { %d } does not exist", requestId);
                    return new ItemRequestNotFoundException(message);
                });
    }

    private void setItems(ItemRequestDtoForResponseBody itemRequest) {
        List<ItemDto> items = itemRepository.findAllByRequestId(itemRequest.getId())
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());

        itemRequest.setItems(items);
    }
}
