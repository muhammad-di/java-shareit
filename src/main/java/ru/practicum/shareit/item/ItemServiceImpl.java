package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.practicum.shareit.booking.exception.BookingInFutureException;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectBookerException;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    @Transactional
    @Override
    public Item save(Item item, long ownerId) throws UserNotFoundException {
        containsUserId(ownerId);
        User owner = userRepository.findById(ownerId).get();
        item.setOwner(owner);
        itemRepository.save(item);
        return item;
    }

    @Transactional
    @Override
    public Item update(Item item, long ownerId)
            throws UserNotFoundException, ItemNotFoundException, IncorrectOwnerException {
        Item itemFromDb = findItemById(item.getId());
        validateOwner(itemFromDb.getOwner(), ownerId);

        if (item.getName() == null && item.getDescription() == null && item.getAvailable() != null) {
            itemRepository.updateAvailable(item.getAvailable(), item.getId());
            itemFromDb.setAvailable(item.getAvailable());
            return itemFromDb;
        } else if (item.getName() == null && item.getDescription() != null && item.getAvailable() == null) {
            itemRepository.updateDescription(item.getDescription(), item.getId());
            itemFromDb.setDescription(item.getDescription());
            return itemFromDb;
        } else if (item.getName() != null && item.getDescription() == null && item.getAvailable() == null) {
            itemRepository.updateName(item.getName(), item.getId());
            itemFromDb.setName(item.getName());
            return itemFromDb;
        } else {
            item.setOwner(itemFromDb.getOwner());
            return itemRepository.save(item);
        }
    }

    @Transactional(readOnly = true)
    public ItemDto findById(long itemId, long ownerOrBookerId) throws ItemNotFoundException {
        Item item = findItemById(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        if (item.getOwner().getId() == ownerOrBookerId) {
            setBookingsForOwner(itemDto, ownerOrBookerId);
            setComments(itemDto);
        } else {
            setBookingsForBooker(itemDto, ownerOrBookerId);
            setComments(itemDto);
        }
        return itemDto;
    }


    @Transactional(readOnly = true)
    public Collection<ItemDto> findAllByUserId(long userId) throws UserNotFoundException {
        containsUserId(userId);
        Collection<Item> list = itemRepository.findAllByOwnerIdOrderById(userId);
        Collection<ItemDto> listDto = list.stream()
                .map(ItemMapper::toItemDto)
                .map(itemDto -> setBookingsForOwner(itemDto, userId))
                .collect(Collectors.toList());
        return listDto;
    }

    @Transactional(readOnly = true)
    public Collection<Item> searchByName(String text) {
        if (!StringUtils.hasText(text)) {
            return Collections.emptyList();
        }
        return itemRepository.findAllByDescriptionContainsIgnoreCaseAndAvailableTrue(text);
    }

    @Transactional
    public Comment createComment(Comment comment) throws
            ItemNotFoundException, IncorrectBookerException, BookingInFutureException {
        LocalDateTime created = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault());
        Item item = findItemById(comment.getItem().getId());
        Booking booking = findBookingByItemIdAndBookerId(comment.getItem().getId(), comment.getAuthor().getId());

        comment.setItem(item);
        comment.setAuthor(booking.getBooker());
        comment.setCreated(created);
        return commentRepository.save(comment);
    }


    private void containsUserId(long userId) throws UserNotFoundException {
        if (!userRepository.existsById(userId)) {
            String message = String.format("a user with id { %d } does not exist", userId);
            throw new UserNotFoundException(message);
        }
    }

    private Item findItemById(long id) throws ItemNotFoundException {
        return itemRepository.findById(id)
                .orElseThrow(() -> {
                    String message = String.format("an item with id { %d } does not exist", id);
                    return new ItemNotFoundException(message);
                });
    }

    private Booking findBookingByItemIdAndBookerId(long itemId, long bookerId)
            throws IncorrectBookerException, BookingInFutureException {
        List<Booking> bookings = bookingRepository
                .findAllByItemIdAndBookerIdAndStatus(itemId, bookerId, Status.APPROVED);
        Booking booking = bookings.stream().findFirst().orElseThrow(() -> {
            String message = String.format("a booking does not exist");
            return new IncorrectBookerException(message);
        });
        if (booking.getStart().isAfter(LocalDateTime.now())) {
            String message = String.format("a booking does not exist");
            throw new BookingInFutureException(message);
        }
        return booking;
    }

    private void validateOwner(User owner, long ownerId) throws IncorrectOwnerException {
        if (owner.getId() != ownerId) {
            String message = String.format("a wrong owner exception");
            throw new IncorrectOwnerException(message);
        }
    }

    private void validateBooker(User owner, long ownerId) throws IncorrectOwnerException {
        if (owner.getId() != ownerId) {
            String message = String.format("a wrong owner exception");
            throw new IncorrectOwnerException(message);
        }
    }

    private ItemDto setBookingsForOwner(ItemDto itemDto, long ownerId) {
        LocalDateTime currentDateTime = LocalDateTime.now().plusHours(12);
        List<Booking> list = bookingRepository.findAllByItem_IdAndItemOwnerIdAndStatusAndStartLessThanOrderById(itemDto.getId(), ownerId, Status.APPROVED, currentDateTime);
        if (!list.isEmpty() && list.size() > 1) {
            Booking firstBooking = list.get(list.size() - 2);
            Booking secondBooking = list.get(list.size() - 1);
            if (firstBooking.getStart().isBefore(secondBooking.getStart())) {
                itemDto.setLastBooking(BookingMapping.toBookingDtoForItemDto(firstBooking));
                itemDto.setNextBooking(BookingMapping.toBookingDtoForItemDto(secondBooking));
            } else {
                itemDto.setLastBooking(BookingMapping.toBookingDtoForItemDto(secondBooking));
                itemDto.setNextBooking(BookingMapping.toBookingDtoForItemDto(firstBooking));
            }

        } else if (!list.isEmpty()) {
            itemDto.setLastBooking(BookingMapping.toBookingDtoForItemDto(list.get(0)));
        }
        return itemDto;
    }

    private ItemDto setBookingsForBooker(ItemDto itemDto, long bookerId) {
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<Booking> list = bookingRepository.findAllByItem_IdAndBookerIdAndStatusAndStartLessThanEqualAndEndGreaterThanOrderById(itemDto.getId(), bookerId, Status.APPROVED, currentDateTime, currentDateTime);
        if (!list.isEmpty() && list.size() > 1) {
            itemDto.setLastBooking(BookingMapping.toBookingDtoForItemDto(list.get(list.size() - 2)));
            itemDto.setNextBooking(BookingMapping.toBookingDtoForItemDto(list.get(list.size() - 1)));
        } else if (!list.isEmpty()) {
            itemDto.setLastBooking(BookingMapping.toBookingDtoForItemDto(list.get(0)));
        }
        return itemDto;
    }

    private void setComments(ItemDto itemDto) {
        List<CommentDto> listOfCommentsDto = commentRepository.findAllByItemId(itemDto.getId())
                .stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList());
        itemDto.setComments(listOfCommentsDto);
    }
}
