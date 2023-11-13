package ru.practicum.shareit.item.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoForGet;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemMapperTest {


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
    void testToItemDto() throws Exception {
        itemDto1 = ItemMapper.toItemDto(item1);
        assertThat(itemDto1, equalTo(itemDtoCopy1));
    }

    @Test
    void testToItemDtoForGet() throws Exception {
        ItemDtoForGet dto1 = ItemMapper.toItemDtoForGet(item1);
        ItemDtoForGet dto2 = ItemMapper.toItemDtoForGet(itemCopy1);

        assertThat(dto1, equalTo(dto2));
    }

    @Test
    void testToItem() throws Exception {
        Item item11 = ItemMapper.toItem(itemDto1);
        Item item22 = ItemMapper.toItem(itemDtoCopy1);

        assertThat(item11, equalTo(item22));
    }

    @Test
    void testToItemForUpdate() throws Exception {
        Item item11 = ItemMapper.toItemForUpdate(itemDto1, item1);
        Item item22 = ItemMapper.toItemForUpdate(itemDtoCopy1, itemCopy1);

        assertThat(item11, equalTo(item22));
    }

    @Test
    void testToItemForUpdate1() throws Exception {
        ItemDto dto = ItemDto.builder().id(itemDto1.getId()).description(itemDto1.getDescription()).available(true).build();
        ItemDto dto1 = ItemDto.builder().id(itemDto1.getId()).description(itemDto1.getDescription()).available(true).build();

        Item item11 = ItemMapper.toItemForUpdate(dto, item1);
        Item item22 = ItemMapper.toItemForUpdate(dto1, itemCopy1);

        assertThat(item11, equalTo(item22));
    }

    @Test
    void testToItemForUpdate2() throws Exception {
        ItemDto dto = ItemDto.builder().id(itemDto1.getId()).name(itemDto1.getName()).available(true).build();
        ItemDto dto1 = ItemDto.builder().id(itemDto1.getId()).name(itemDto1.getName()).available(true).build();

        Item item11 = ItemMapper.toItemForUpdate(dto, item1);
        Item item22 = ItemMapper.toItemForUpdate(dto1, itemCopy1);

        assertThat(item11, equalTo(item22));
    }

    @Test
    void testToItemForUpdate3() throws Exception {
        ItemDto dto = ItemDto.builder().id(itemDto1.getId()).name(itemDto1.getName()).description(itemDto1.getDescription()).build();
        ItemDto dto1 = ItemDto.builder().id(itemDto1.getId()).name(itemDto1.getName()).description(itemDto1.getDescription()).build();

        Item item11 = ItemMapper.toItemForUpdate(dto, item1);
        Item item22 = ItemMapper.toItemForUpdate(dto1, itemCopy1);

        assertThat(item11, equalTo(item22));
    }
}
