package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
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
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.ItemService;
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
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceImplTest {

    @Autowired
    private final EntityManager em;

    @Autowired
    private final ItemService service;

    @Autowired
    private final BookingService bookingService;

    @Autowired
    private final UserService userService;

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
    private CommentDto commentDto1;
    private Comment comment1;


    @BeforeEach
    public void create() {
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
                .status(Status.APPROVED)
                .build();
        bookingDto1 = BookingMapping.toBookingDto(booking1);

        commentDto1 = CommentDto.builder().text("commentText1").build();
        comment1 = CommentMapper.toComment(commentDto1);

    }

    private Booking createForComment() throws UserNotFoundException, ItemRequestNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, ItemNotFoundException, InvalidEndTimeException {
        User cBooker1 = userService.save(booker1);
        comment1.setAuthor(cBooker1.getId());
        User cOwner1 = userService.save(owner1);
        Item cItem1 = service.save(item1, cOwner1.getId());
        comment1.setItem(cItem1.getId());
        Booking booking = Booking.builder()
                .start(LocalDateTime.now().plusSeconds(1))
                .end(LocalDateTime.now().plusSeconds(2))
                .booker(booker1)
                .item(item1)
                .status(Status.APPROVED)
                .build();
        return bookingService.save(booking, cBooker1.getId());
    }


    @Test
    void testSave() throws UserNotFoundException, ItemRequestNotFoundException {
        User cBooker1 = userRepository.saveAndFlush(booker1);
        User cOwner1 = userRepository.saveAndFlush(owner1);

        item1.setOwner(cOwner1);
        Item actual = service.save(item1, cOwner1.getId());

        TypedQuery<Item> query = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item expected = query.setParameter("id", actual.getId()).getSingleResult();

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    void testApprove() throws IncorrectOwnerException, ItemNotFoundException {
        User cBooker1 = userRepository.saveAndFlush(booker1);
        User cOwner1 = userRepository.saveAndFlush(owner1);
        item1.setOwner(cOwner1);
        Item cItem1 = itemRepository.saveAndFlush(item1);
        ItemDto cItemDto1 = ItemDto.builder().id(cItem1.getId()).name("updatedItem1").description("updatedDescription1").available(true).build();
        Item actual = service.update(cItemDto1, cOwner1.getId());

        TypedQuery<Item> query = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item expected = query.setParameter("id", actual.getId()).getSingleResult();

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    void testFindById() throws UserNotFoundException, ItemRequestNotFoundException, ItemNotFoundException {
        User cBooker1 = userRepository.saveAndFlush(booker1);
        User cOwner1 = userRepository.saveAndFlush(owner1);

        item1.setOwner(cOwner1);
        Item cItem1 = service.save(item1, cOwner1.getId());

        ItemDtoForGet actual = service.findById(cItem1.getId(), cBooker1.getId());


        TypedQuery<Item> query = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item expected1 = query.setParameter("id", actual.getId()).getSingleResult();
        ItemDtoForGet expected = ItemMapper.toItemDtoForGet(expected1);

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    void testFindAllByOwnerId() throws UserNotFoundException {
        User cBooker1 = userRepository.saveAndFlush(booker1);
        User cOwner1 = userRepository.saveAndFlush(owner1);
        item1.setOwner(cOwner1);
        Item cItem1 = itemRepository.saveAndFlush(item1);

        Collection<ItemDtoForGet> actual = service.findAllByOwnerId(cOwner1.getId());

        TypedQuery<Item> query = em.createQuery("Select it from Item it where it.owner.id = :id", Item.class);
        List<Item> expected1 = query.setParameter("id", cOwner1.getId()).getResultList();
        Collection<ItemDtoForGet> expected = expected1.stream().map(ItemMapper::toItemDtoForGet).collect(Collectors.toList());

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    void testSearchByDescription() {
        User cBooker1 = userRepository.saveAndFlush(booker1);
        User cOwner1 = userRepository.saveAndFlush(owner1);
        item1.setOwner(cOwner1);
        Item cItem1 = itemRepository.saveAndFlush(item1);

        Collection<Item> actual = service.searchByDescription("description");

        TypedQuery<Item> query = em.createQuery("Select it from Item it where upper(it.description) like upper(concat('%', :description, '%')) ", Item.class);
        Collection<Item> expected = query.setParameter("description", "description").getResultList();

        assertThat(actual, samePropertyValuesAs(expected));
    }


    @Test
    void testCreateCommentThrowsException() throws UserNotFoundException, ItemRequestNotFoundException, IncorrectBookerException, ItemNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, InvalidEndTimeException {

        createForComment();
        final IncorrectBookerException exception = Assertions.assertThrows(
                IncorrectBookerException.class,
                () -> service.createComment(comment1)
        );

        assertThat(exception.getErrorMessage(), equalTo("MASSAGE: a booking does not exist; ERROR CODE: null"));

    }

}