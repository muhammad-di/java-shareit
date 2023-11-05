package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.exception.IncorrectBookerException;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final ItemRequestRepository itemRequestRepository;


    @Transactional
    @Override
    public Item save(Item item, long ownerId) throws UserNotFoundException, ItemRequestNotFoundException {
        User owner = getUser(ownerId);
        item.setOwner(owner);
        setItemRequest(item);
        return itemRepository.save(item);
    }

    @Transactional
    @Override
    public Item update(ItemDto itemDto, long ownerId)
            throws ItemNotFoundException, IncorrectOwnerException {
        Item itemFromDb = findItemById(itemDto.getId());
        validateOwner(itemFromDb.getOwner(), ownerId);
        Item item = ItemMapper.toItemForUpdate(itemDto, itemFromDb);

        return itemRepository.save(item);
    }

    @Transactional(readOnly = true)
    public ItemDtoForGet findById(long itemId, long userId) throws ItemNotFoundException {
        Item item = findItemById(itemId);
        ItemDtoForGet itemDto = ItemMapper.toItemDtoForGet(item);
        if (item.getOwner().getId() == userId) {
            setBookings(itemDto);
        }
        setComments(itemDto);
        return itemDto;
    }


    @Transactional(readOnly = true)
    public Collection<ItemDtoForGet> findAllByOwnerId(long ownerId) throws UserNotFoundException {
        existsUserById(ownerId);
        return itemRepository.findAllByOwnerIdOrderById(ownerId)
                .stream()
                .map(ItemMapper::toItemDtoForGet)
                .map(itemDto -> setBookings(itemDto))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Collection<Item> searchByDescription(String text) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyList();
        }
        return itemRepository.findAllByDescriptionContainsIgnoreCaseAndAvailableTrue(text);
    }

    @Transactional
    public Comment createComment(Comment comment) throws
            ItemNotFoundException, IncorrectBookerException {
        LocalDateTime created = LocalDateTime.now();
        Item item = findItemById(comment.getItem().getId());
        Booking booking = findBookingByItemIdAndBookerId(comment.getItem().getId(), comment.getAuthor().getId());

        comment.setItem(item);
        comment.setAuthor(booking.getBooker());
        comment.setCreated(created);
        return commentRepository.save(comment);
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

    private void setItemRequest(Item item) throws ItemRequestNotFoundException {
        if (item.getRequest() != null) {
            ItemRequest itemRequest = itemRequestRepository.findById(item.getRequest().getId())
                    .orElseThrow(() -> {
                        String message = String.format("an item request with id { %d } does not exist", item.getRequest().getId());
                        return new ItemRequestNotFoundException(message);
                    });
            item.setRequest(itemRequest);
        }
    }

    private Item findItemById(long id) throws ItemNotFoundException {
        return itemRepository.findById(id)
                .orElseThrow(() -> {
                    String message = String.format("an item with id { %d } does not exist", id);
                    return new ItemNotFoundException(message);
                });
    }

    private Booking findBookingByItemIdAndBookerId(long itemId, long bookerId) throws IncorrectBookerException {
        LocalDateTime currentDateTime = LocalDateTime.now();
        return bookingRepository
                .findByItemIdAndBookerIdAndStatusAndStartBefore(itemId, bookerId, Status.APPROVED, currentDateTime)
                .stream()
                .findFirst()
                .orElseThrow(() -> {
                    String message = "a booking does not exist";
                    return new IncorrectBookerException(message);
                });
    }

    private void validateOwner(User owner, long ownerId) throws IncorrectOwnerException {
        if (owner.getId() != ownerId) {
            String message = "a wrong owner exception";
            throw new IncorrectOwnerException(message);
        }
    }

    private ItemDtoForGet setBookings(ItemDtoForGet itemDto) {
        LocalDateTime currentDateTime = LocalDateTime.now();

        List<Booking> lastBookingList = bookingRepository
                .findAllByItemIdAndStatusAndStartBeforeOrderByEndDesc(itemDto.getId(), Status.APPROVED, currentDateTime);
        List<Booking> nextBookingList = bookingRepository
                .findAllByItemIdAndStatusAndStartAfterOrderByStartAsc(itemDto.getId(), Status.APPROVED, currentDateTime);

        if (!lastBookingList.isEmpty()) {
            Booking booking = lastBookingList.stream().findFirst().get();
            itemDto.setLastBooking(BookingMapping.toBookingDtoForItemDto(booking));
        }
        if (!nextBookingList.isEmpty()) {
            Booking booking = nextBookingList.stream().findFirst().get();
            itemDto.setNextBooking(BookingMapping.toBookingDtoForItemDto(booking));
        }
        return itemDto;
    }

    private void setComments(ItemDtoForGet itemDto) {
        List<CommentDto> listOfCommentsDto = commentRepository.findAllByItemId(itemDto.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        itemDto.setComments(listOfCommentsDto);
    }
}
