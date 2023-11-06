package ru.practicum.shareit.item.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemTest {


    LocalDateTime testStart;
    LocalDateTime testEnd;
    LocalDateTime comment1Created;
    Item item1;
    Item itemCopy1;
    Item item2;
    ItemDto itemDto1;
    ItemDto itemDtoCopy1;


    @BeforeEach
    public void create() {
        testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
        testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);
        comment1Created = testEnd.plusHours(1);

        User user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        User booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        item1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();
        itemCopy1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();

        itemDto1 = ItemMapper.toItemDto(item1);
        itemDtoCopy1 = ItemMapper.toItemDto(itemCopy1);

        item2 = Item.builder().id(2).name("item2").description("description2").available(true).owner(user1).build();
    }

    @Test
    void testConstructor() throws Exception {
        Item itemCopy2 = new Item(itemCopy1.getId(),
                itemCopy1.getName(),
                itemCopy1.getDescription(),
                itemCopy1.getRequest(),
                itemCopy1.getAvailable(),
                itemCopy1.getOwner());
        assertThat(item1, equalTo(itemCopy2));
    }

    @Test
    void testEqual() throws Exception {
        assertThat(item1, equalTo(itemCopy1));
    }

    @Test
    void testHash() throws Exception {
        assertThat(item1.hashCode(), equalTo(itemCopy1.hashCode()));
    }

    @Test
    void testToString() throws Exception {
        assertThat(item1.toString(), equalTo(itemCopy1.toString()));
    }

    @Test
    void testGetters() throws Exception {
        assertThat(item1.getId(), equalTo(itemCopy1.getId()));
        assertThat(item1.getName(), equalTo(itemCopy1.getName()));
        assertThat(item1.getOwner(), equalTo(itemCopy1.getOwner()));
        assertThat(item1.getDescription(), equalTo(itemCopy1.getDescription()));
        assertThat(item1.getAvailable(), equalTo(itemCopy1.getAvailable()));
        assertThat(item1.getRequest(), equalTo(itemCopy1.getRequest()));
    }

    @Test
    void testSetters() throws Exception {


        item1.setId(2);
        item1.setName("New Name");
        item1.setOwner(User.builder().id(10).build());
        item1.setDescription("New description");
        item1.setAvailable(false);
        item1.setRequest(ItemRequest.builder().id(1).build());

        itemCopy1.setId(2);
        itemCopy1.setName("New Name");
        itemCopy1.setOwner(User.builder().id(10).build());
        itemCopy1.setDescription("New description");
        itemCopy1.setAvailable(false);
        itemCopy1.setRequest(ItemRequest.builder().id(1).build());

        assertThat(item1.getId(), equalTo(itemCopy1.getId()));
        assertThat(item1.getName(), equalTo(itemCopy1.getName()));
        assertThat(item1.getOwner(), equalTo(itemCopy1.getOwner()));
        assertThat(item1.getDescription(), equalTo(itemCopy1.getDescription()));
        assertThat(item1.getAvailable(), equalTo(itemCopy1.getAvailable()));
        assertThat(item1.getRequest(), equalTo(itemCopy1.getRequest()));
    }
}
