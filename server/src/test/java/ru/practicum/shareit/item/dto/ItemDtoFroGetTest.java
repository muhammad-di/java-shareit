package ru.practicum.shareit.item.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDtoForItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ItemDtoFroGetTest {


    LocalDateTime testStart;
    LocalDateTime testEnd;
    LocalDateTime comment1Created;
    Item item1;
    Item itemCopy1;
    Item item2;
    ItemDtoForGet itemDtoForGet1;
    ItemDtoForGet itemDtoForGetCopy1;


    @BeforeEach
    public void create() {
        testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
        testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);
        comment1Created = testEnd.plusHours(1);

        User user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        User booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        item1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();
        itemCopy1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();

        itemDtoForGet1 = ItemMapper.toItemDtoForGet(item1);
        itemDtoForGetCopy1 = ItemMapper.toItemDtoForGet(itemCopy1);

        item2 = Item.builder().id(2).name("item2").description("description2").available(true).owner(user1).build();
    }

    @Test
    void testEqual() throws Exception {
        assertThat(itemDtoForGet1, equalTo(itemDtoForGetCopy1));
    }

    @Test
    void testHash() throws Exception {
        assertThat(itemDtoForGet1.hashCode(), equalTo(itemDtoForGetCopy1.hashCode()));
    }

    @Test
    void testToString() throws Exception {
        assertThat(itemDtoForGet1.toString(), equalTo(itemDtoForGetCopy1.toString()));
    }

    @Test
    void testGetters() throws Exception {
        assertThat(itemDtoForGet1.getId(), equalTo(itemDtoForGetCopy1.getId()));
        assertThat(itemDtoForGet1.getName(), equalTo(itemDtoForGetCopy1.getName()));
        assertThat(itemDtoForGet1.getDescription(), equalTo(itemDtoForGetCopy1.getDescription()));
        assertThat(itemDtoForGet1.getAvailable(), equalTo(itemDtoForGetCopy1.getAvailable()));
        assertThat(itemDtoForGet1.getComments(), equalTo(itemDtoForGetCopy1.getComments()));
        assertThat(itemDtoForGet1.getLastBooking(), equalTo(itemDtoForGetCopy1.getLastBooking()));
        assertThat(itemDtoForGet1.getNextBooking(), equalTo(itemDtoForGetCopy1.getNextBooking()));
    }

    @Test
    void testSetters() throws Exception {


        itemDtoForGet1.setId(2);
        itemDtoForGet1.setAvailable(false);
        itemDtoForGet1.setComments(Collections.emptyList());
        itemDtoForGet1.setNextBooking(BookingDtoForItemDto.builder().id(3).build());
        itemDtoForGet1.setLastBooking(BookingDtoForItemDto.builder().id(2).build());


        itemDtoForGetCopy1.setId(2);
        itemDtoForGetCopy1.setAvailable(false);
        itemDtoForGetCopy1.setComments(Collections.emptyList());
        itemDtoForGetCopy1.setNextBooking(BookingDtoForItemDto.builder().id(3).build());
        itemDtoForGetCopy1.setLastBooking(BookingDtoForItemDto.builder().id(2).build());

        assertThat(itemDtoForGet1.getId(), equalTo(itemDtoForGetCopy1.getId()));
        assertThat(itemDtoForGet1.getName(), equalTo(itemDtoForGetCopy1.getName()));
        assertThat(itemDtoForGet1.getDescription(), equalTo(itemDtoForGetCopy1.getDescription()));
        assertThat(itemDtoForGet1.getAvailable(), equalTo(itemDtoForGetCopy1.getAvailable()));
        assertThat(itemDtoForGet1.getComments(), equalTo(itemDtoForGetCopy1.getComments()));
        assertThat(itemDtoForGet1.getLastBooking(), equalTo(itemDtoForGetCopy1.getLastBooking()));
        assertThat(itemDtoForGet1.getNextBooking(), equalTo(itemDtoForGetCopy1.getNextBooking()));
    }
}
