package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.exception.*;
import ru.practicum.shareit.booking.mapper.BookingMapping;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceImplTest {

    @Autowired
    private final EntityManager em;

    private final BookingService service;

    @Autowired
    private final BookingRepository bookingRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final ItemRequestRepository itemRequestRepository;

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
//        LocalDateTime testRequestCreated = LocalDateTime.of(2023, 8, 2, 3, 15);
//        LocalDateTime testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
//        LocalDateTime testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);

        LocalDateTime testRequestCreated = LocalDateTime.now();
        LocalDateTime testStart = LocalDateTime.now().plusHours(1);
        LocalDateTime testEnd = LocalDateTime.now().plusHours(2);

        owner1 = User.builder().name("John").email("john.doe@mail.com").build();
        booker1 = User.builder().name("name2").email("name2@mail.ru").build();
        requestor1 = User.builder().name("name3").email("name3@mail.ru").build();
        itemRequest1 = ItemRequest.builder().description("description1").requestor(requestor1).created(testRequestCreated).build();
        ownerDto1 = UserMapper.toUserDto(owner1);
        bookerDto1 = UserMapper.toUserDto(booker1);
        requestorDto1 = UserMapper.toUserDto(requestor1);

        item1 = Item.builder().name("item1").description("description1").request(null).available(true).owner(owner1).build();
        itemDto1 = ItemMapper.toItemDto(item1);
        itemDtoForGet1 = ItemMapper.toItemDtoForGet(item1);

        booking1 = Booking.builder()
                .start(testStart)
                .end(testEnd)
                .booker(booker1)
                .item(item1)
                .status(Status.WAITING)
                .build();
        bookingDto1 = BookingMapping.toBookingDto(booking1);

    }

    @Test
    void testSave() throws UserNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, ItemNotFoundException, InvalidEndTimeException {
        User cBooker1 = userRepository.saveAndFlush(booker1);
        User cOwner1 = userRepository.saveAndFlush(owner1);

//        User cRequestor1 = userRepository.saveAndFlush(requestor1);
//        itemRequest1.setRequestor(cRequestor1);
//        ItemRequest cItemRequest1 = itemRequestRepository.saveAndFlush(itemRequest1);
//        item1.setRequest(cItemRequest1);

        item1.setOwner(cOwner1);
        Item cItem1 = itemRepository.saveAndFlush(item1);

        Booking actual = service.save(booking1, cBooker1.getId());

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking expected = query.setParameter("id", actual.getId()).getSingleResult();

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    void testApprove() throws UserNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, ItemNotFoundException, InvalidEndTimeException, InvalidOwnerException, BookingAlreadyApprovedException {
        User cBooker1 = userRepository.saveAndFlush(booker1);
        User cOwner1 = userRepository.saveAndFlush(owner1);
        item1.setOwner(cOwner1);
        Item cItem1 = itemRepository.saveAndFlush(item1);
        Booking cBooking1 = service.save(booking1, cBooker1.getId());

        Booking actual = service.approve(cBooking1.getId(), cOwner1.getId(), true);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking expected = query.setParameter("id", actual.getId()).getSingleResult();

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    void testFindById() throws UserNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, ItemNotFoundException, InvalidEndTimeException, InvalidOwnerException, BookingAlreadyApprovedException, UserNotAllowedAccessBookingException {
        User cBooker1 = userRepository.saveAndFlush(booker1);
        User cOwner1 = userRepository.saveAndFlush(owner1);
        item1.setOwner(cOwner1);
        Item cItem1 = itemRepository.saveAndFlush(item1);
        Booking cBooking1 = service.save(booking1, cBooker1.getId());
        Booking cUpdatedBooking1 = service.approve(cBooking1.getId(), cOwner1.getId(), true);

        Booking actual = service.findById(cBooking1.getId(), cBooker1.getId());

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking expected = query.setParameter("id", actual.getId()).getSingleResult();

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    void testFindAllByBookerId() throws UserNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, ItemNotFoundException, InvalidEndTimeException, InvalidOwnerException, BookingAlreadyApprovedException, UserNotAllowedAccessBookingException, UnsupportedStateException {
        User cBooker1 = userRepository.saveAndFlush(booker1);
        User cOwner1 = userRepository.saveAndFlush(owner1);
        item1.setOwner(cOwner1);
        Item cItem1 = itemRepository.saveAndFlush(item1);
        Booking cBooking1 = service.save(booking1, cBooker1.getId());
        Booking cUpdatedBooking1 = service.approve(cBooking1.getId(), cOwner1.getId(), true);

        Collection<Booking> actual = service.findAllByBookerId(cBooker1.getId(), "ALL", 0, 1);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.booker.id = :id", Booking.class);
        Collection<Booking> expected = query.setParameter("id", cBooker1.getId()).getResultList();

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    void testFindAllByOwnerId() throws UserNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, ItemNotFoundException, InvalidEndTimeException, InvalidOwnerException, BookingAlreadyApprovedException, UserNotAllowedAccessBookingException, UnsupportedStateException {
        User cBooker1 = userRepository.saveAndFlush(booker1);
        User cOwner1 = userRepository.saveAndFlush(owner1);
        item1.setOwner(cOwner1);
        Item cItem1 = itemRepository.saveAndFlush(item1);
        Booking cBooking1 = service.save(booking1, cBooker1.getId());
        Booking cUpdatedBooking1 = service.approve(cBooking1.getId(), cOwner1.getId(), true);

        Collection<Booking> actual = service.findAllByOwnerId(cOwner1.getId(), "ALL", 0, 1);

        TypedQuery<Booking> query = em.createQuery("Select b from Booking b where b.item.owner.id = :id", Booking.class);
        Collection<Booking> expected = query.setParameter("id", cOwner1.getId()).getResultList();

        assertThat(actual, samePropertyValuesAs(expected));
    }
}