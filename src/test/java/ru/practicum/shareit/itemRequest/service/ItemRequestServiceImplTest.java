package ru.practicum.shareit.itemRequest.service;

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
import ru.practicum.shareit.comment.dto.CommentDto;
import ru.practicum.shareit.comment.mapper.CommentMapper;
import ru.practicum.shareit.comment.model.Comment;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestService;
import ru.practicum.shareit.request.dto.ItemRequestDtoForRequestBody;
import ru.practicum.shareit.request.dto.ItemRequestDtoForResponseBody;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;


@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemRequestServiceImplTest {

    @Autowired
    private final EntityManager em;

    @Autowired
    private final ItemService itemService;

    @Autowired
    private final BookingService bookingService;

    @Autowired
    private final UserService userService;

    @Autowired
    private final ItemRequestService itemRequestService;

    private User owner1;
    private User booker1;
    private User requestor1;
    private UserDto ownerDto1;
    private UserDto bookerDto1;
    private UserDto requestorDto1;
    private Item item1;
    private ItemDto itemDto1;
    private ItemDtoForGet itemDtoForGet1;
    private ItemRequestDtoForRequestBody itemRequestDtoForRequestBody1;
    private Booking booking1;
    private BookingDto bookingDto1;
    private CommentDto commentDto1;
    private Comment comment1;


    @BeforeEach
    public void create() {
        LocalDateTime testRequestCreated = LocalDateTime.now().minusHours(1);
        LocalDateTime testStart = LocalDateTime.now().plusHours(1);
        LocalDateTime testEnd = LocalDateTime.now().plusHours(2);

        owner1 = User.builder().name("John").email("john.doe@mail.com").build();
        booker1 = User.builder().name("name2").email("name2@mail.ru").build();
        requestor1 = User.builder().name("name3").email("name3@mail.ru").build();
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

        itemRequestDtoForRequestBody1 = ItemRequestDtoForRequestBody.builder().description("request description").build();
    }

    private User createForComment() throws UserNotFoundException, ItemRequestNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, ItemNotFoundException, InvalidEndTimeException, InterruptedException {
        User cBooker1 = userService.save(booker1);
        User cOwner1 = userService.save(owner1);
        User cRequestor1 = userService.save(requestor1);
        Item cItem1 = itemService.save(item1, cOwner1.getId());

        ItemRequest request = ItemRequestMapper.toItemRequest(itemRequestDtoForRequestBody1);
        User requestor = userService.findById(cRequestor1.getId());
        return requestor;
    }


    @Test
    void testSave() throws UserNotFoundException, ItemRequestNotFoundException, InterruptedException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, ItemNotFoundException, InvalidEndTimeException {

        User requestor = createForComment();

        ItemRequest request = ItemRequestMapper.toItemRequest(itemRequestDtoForRequestBody1);
        request.setRequestor(requestor);
        ItemRequest actual  = itemRequestService.save(request, requestor.getId());

        TypedQuery<ItemRequest> query = em.createQuery("Select it from ItemRequest it where it.id = :id", ItemRequest.class);
        ItemRequest expected = query.setParameter("id", actual.getId()).getSingleResult();

        assertThat(actual, samePropertyValuesAs(expected));
    }


    @Test
    void testFindById() throws UserNotFoundException, ItemRequestNotFoundException, ItemNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, InterruptedException, InvalidEndTimeException {

        User requestor = createForComment();

        ItemRequest request = ItemRequestMapper.toItemRequest(itemRequestDtoForRequestBody1);
        request.setRequestor(requestor);
        ItemRequest cItemRequest  = itemRequestService.save(request, requestor.getId());

        ItemRequestDtoForResponseBody actual = itemRequestService.findById(cItemRequest.getId(), requestor.getId());

        TypedQuery<ItemRequest> query = em.createQuery("Select it from ItemRequest it where it.id = :id", ItemRequest.class);
        ItemRequest expected1 = query.setParameter("id", actual.getId()).getSingleResult();
        ItemRequestDtoForResponseBody expected = ItemRequestMapper.toItemRequestDtoForResponseBody(expected1);
        expected.setItems(Collections.emptyList());

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    void testfindAllByRequestorId() throws UserNotFoundException, ItemRequestNotFoundException, ItemNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, InterruptedException, InvalidEndTimeException {

        User requestor = createForComment();

        ItemRequest request = ItemRequestMapper.toItemRequest(itemRequestDtoForRequestBody1);
        request.setRequestor(requestor);
        ItemRequest cItemRequest  = itemRequestService.save(request, requestor.getId());

        Collection<ItemRequestDtoForResponseBody> actual = itemRequestService.findAllByRequestorId(requestor.getId());

        TypedQuery<ItemRequest> query = em.createQuery("Select it from ItemRequest it where it.requestor.id = :id", ItemRequest.class);
        Collection <ItemRequest> expected1 = query.setParameter("id", requestor.getId()).getResultList();
        Collection<ItemRequestDtoForResponseBody> expected = expected1.stream()
                .map(ItemRequestMapper::toItemRequestDtoForResponseBody)
                .peek(ir-> ir.setItems(Collections.emptyList()))
                .collect(Collectors.toList());

        assertThat(actual, samePropertyValuesAs(expected));
    }

    @Test
    void testFindAll() throws UserNotFoundException, ItemRequestNotFoundException, ItemNotFoundException, InvalidBookerException, InvalidStartTimeException, BookingNotFoundException, ItemNotAvailableException, InterruptedException, InvalidEndTimeException {

        User requestor = createForComment();

        ItemRequest request = ItemRequestMapper.toItemRequest(itemRequestDtoForRequestBody1);
        request.setRequestor(requestor);
        ItemRequest cItemRequest  = itemRequestService.save(request, requestor.getId());

        Collection<ItemRequestDtoForResponseBody> actual = itemRequestService.findAll(requestor.getId(), 2, 1);

        TypedQuery<ItemRequest> query = em.createQuery("Select it from ItemRequest it where it.requestor.id = :id", ItemRequest.class);
        Collection <ItemRequest> expected1 = query.setParameter("id", requestor.getId()).getResultList();
        Collection<ItemRequestDtoForResponseBody> expected = expected1.stream()
                .map(ItemRequestMapper::toItemRequestDtoForResponseBody)
                .peek(ir-> ir.setItems(Collections.emptyList()))
                .collect(Collectors.toList());

        assertThat(actual, samePropertyValuesAs(new ArrayList<ItemRequestDtoForResponseBody>()));
    }
}