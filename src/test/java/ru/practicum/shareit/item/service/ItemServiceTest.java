package ru.practicum.shareit.item.service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.shareit.booking.storage.BookingRepository;
import ru.practicum.shareit.comment.storage.CommentRepository;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.ItemServiceImpl;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectOwnerException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.request.exception.ItemRequestNotFoundException;
import ru.practicum.shareit.request.storage.ItemRequestRepository;
import ru.practicum.shareit.user.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@ExtendWith(MockitoExtension.class)
public class ItemServiceTest {

    @Mock
    ItemRepository itemRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BookingRepository bookingRepository;

    @Mock
    CommentRepository commentRepository;

    @Mock
    ItemRequestRepository itemRequestRepository;

    private Item item1;
    private Item item2;
    private Item item3;
    private ItemDto itemDto1;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void createItems() {
        user1 = User.builder().id(1L).name("John").email("john.doe@mail.com").build();
        user2 = User.builder().id(2L).name("name2").email("name2@mail.ru").build();
        user3 = User.builder().id(3L).name("name3").email("name3@mail.ru").build();

        item1 = Item.builder().id(1L).name("item1").description("description1").available(true).owner(user1).build();
        item2 = Item.builder().id(2L).name("item2").description("description2").available(true).build();
        item3 = Item.builder().id(3L).name("item3").description("description3").available(true).build();

        itemDto1 = ItemDto.builder().id(1L).name("item1").description("description1").available(true).build();
    }

    @Test
    public void testSave() throws UserNotFoundException, ItemRequestNotFoundException {
        ItemService service = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository);

        Mockito.
                when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item1);
        Mockito.
                when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));


        Item actual = service.save(item1, user1.getId());

        assertThat(actual, Matchers.hasProperty("id", equalTo(1L)));
        assertThat(actual, Matchers.hasProperty("name", equalTo("item1")));
        assertThat(actual, Matchers.hasProperty("description", equalTo("description1")));
        assertThat(actual, Matchers.hasProperty("owner", equalTo(user1)));
    }

    @Test
    public void testSaveShouldThrowUserNotFoundException() {
        ItemService service = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository);

        Mockito.
                when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(null));


        final UserNotFoundException exception = Assertions.assertThrows(
                UserNotFoundException.class,
                () -> service.save(item1, user1.getId())
        );

        assertThat("MASSAGE: a user with id { 1 } does not exist; ERROR CODE: null",
                equalTo(exception.getErrorMessage()));
    }

    @Test
    public void testUpdate() throws IncorrectOwnerException, ItemNotFoundException, UserNotFoundException, ItemRequestNotFoundException {
        ItemService service = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository);

        Mockito.
                when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        Mockito.
                when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));
        Mockito.
                when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item1);

        service.save(item1, user1.getId());
        Item actual = service.update(itemDto1, user1.getId());

        assertThat(actual, Matchers.hasProperty("id", equalTo(1L)));
        assertThat(actual, Matchers.hasProperty("name", equalTo("item1")));
        assertThat(actual, Matchers.hasProperty("description", equalTo("description1")));
        assertThat(actual, Matchers.hasProperty("owner", equalTo(user1)));
    }

    @Test
    public void testUpdateShouldThrowIncorrectOwnerException()
            throws UserNotFoundException, ItemRequestNotFoundException {
        ItemService service = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository);

        Mockito.
                when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(item1));
        Mockito.
                when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));
        Mockito.
                when(itemRepository.save(Mockito.any(Item.class)))
                .thenReturn(item1);

        service.save(item1, user1.getId());


        final IncorrectOwnerException exception = Assertions.assertThrows(
                IncorrectOwnerException.class,
                () -> service.update(itemDto1, 2L)
        );

        assertThat(exception.getErrorMessage(),
                equalTo("MASSAGE: a wrong owner exception; ERROR CODE: null"));
    }

    @Test
    public void testUpdateShouldThrowItemNotFoundException()
            throws UserNotFoundException, ItemRequestNotFoundException {
        ItemService service = new ItemServiceImpl(itemRepository, userRepository, bookingRepository, commentRepository, itemRequestRepository);

        Mockito.
                when(itemRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.ofNullable(null));
        Mockito.
                when(userRepository.findById(Mockito.anyLong()))
                .thenReturn(Optional.of(user1));

        service.save(item1, user1.getId());


        final ItemNotFoundException exception = Assertions.assertThrows(
                ItemNotFoundException.class,
                () -> service.update(itemDto1, user1.getId())
        );

        assertThat(exception.getErrorMessage(),
                equalTo("MASSAGE: an item with id { 1 } does not exist; ERROR CODE: null"));
    }
}
