package ru.practicum.shareit.booking.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.BookingServiceImpl;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.BookingAlreadyApprovedException;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.pattern.strategy.impl.booker.BookingStateFetchStrategyForBooker;
import ru.practicum.shareit.booking.pattern.strategy.impl.owner.BookingStateFetchStrategyForOwner;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
public class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private Map<String, BookingStateFetchStrategyForBooker> bookingStateFetchStrategyForBookerMap;

    @Mock
    private Map<String, BookingStateFetchStrategyForOwner> bookingStateFetchStrategyForOwnerMap;

    private User owner1;
    private User booker1;
    private User requestor1;
    private UserDto ownerDto1;
    private UserDto bookerDto1;
    private UserDto requestorDto1;
    private Item item1;
    private ItemDto itemDto1;
    private ItemDtoForGet itemDtoForGet1;
    private ItemRequest itemRequest1;
    private Booking booking1;
    private BookingDto bookingDto1;


    @BeforeEach
    public void createUsers() {

        LocalDateTime testRequestCreated = LocalDateTime.now();
        LocalDateTime testStart = LocalDateTime.now().plusHours(1);
        LocalDateTime testEnd = LocalDateTime.now().plusHours(2);

        owner1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        booker1 = User.builder().id(2).name("name2").email("name2@mail.ru").build();
        requestor1 = User.builder().id(3).name("name3").email("name3@mail.ru").build();
        itemRequest1 = ItemRequest.builder().id(1).description("description1").requestor(requestor1).created(testRequestCreated).build();
        ownerDto1 = UserMapper.toUserDto(owner1);
        bookerDto1 = UserMapper.toUserDto(booker1);
        requestorDto1 = UserMapper.toUserDto(requestor1);

        item1 = Item.builder().id(1).name("item1").description("description1").request(null).available(true).owner(owner1).build();
        itemDto1 = ItemMapper.toItemDto(item1);
        itemDtoForGet1 = ItemMapper.toItemDtoForGet(item1);

        booking1 = Booking.builder()
                .id(1)
                .start(testStart)
                .end(testEnd)
                .booker(booker1)
                .item(item1)
                .status(Status.WAITING)
                .build();
        bookingDto1 = BookingMapping.toBookingDto(booking1);

    }

    @Test
    public void testApproveShouldThrowBookingAlreadyApprovedException() {
        BookingService service = new BookingServiceImpl(bookingRepository, userRepository, itemRepository, bookingStateFetchStrategyForBookerMap, bookingStateFetchStrategyForOwnerMap);
        booking1.setStatus(Status.APPROVED);
        Mockito.when(bookingRepository.findById(anyLong()))
                .thenReturn(Optional.ofNullable(booking1));

        BookingAlreadyApprovedException exception = Assertions.assertThrows(
                BookingAlreadyApprovedException.class,
                () -> service.approve(1, 1, true)
        );
        Assertions.assertEquals("MASSAGE: a booking is already approved; ERROR CODE: null", exception.getErrorMessage());
    }


}
