package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemDtoTest {


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
    void testEqual() throws Exception {
        assertThat(itemDto1, equalTo(itemDtoCopy1));
    }

    @Test
    void testHash() throws Exception {
        assertThat(itemDto1.hashCode(), equalTo(itemDtoCopy1.hashCode()));
    }

    @Test
    void testToString() throws Exception {
        assertThat(itemDto1.toString(), equalTo(itemDtoCopy1.toString()));
    }

    @Test
    void testGetters() throws Exception {
        assertThat(itemDto1.getId(), equalTo(itemDtoCopy1.getId()));
        assertThat(itemDto1.getName(), equalTo(itemDtoCopy1.getName()));
        assertThat(itemDto1.getDescription(), equalTo(itemDtoCopy1.getDescription()));
        assertThat(itemDto1.getAvailable(), equalTo(itemDtoCopy1.getAvailable()));
        assertThat(itemDto1.getRequestId(), equalTo(itemDtoCopy1.getRequestId()));

    }

    @Test
    void testSetters() throws Exception {


        itemDto1.setId(2);
        itemDto1.setAvailable(false);
        itemDto1.setRequestId(5L);

        itemDtoCopy1.setId(2);
        itemDtoCopy1.setAvailable(false);
        itemDtoCopy1.setRequestId(5L);

        assertThat(itemDto1.getId(), equalTo(itemDtoCopy1.getId()));
        assertThat(itemDto1.getName(), equalTo(itemDtoCopy1.getName()));
        assertThat(itemDto1.getDescription(), equalTo(itemDtoCopy1.getDescription()));
        assertThat(itemDto1.getAvailable(), equalTo(itemDtoCopy1.getAvailable()));
        assertThat(itemDto1.getRequestId(), equalTo(itemDtoCopy1.getRequestId()));
    }
}
