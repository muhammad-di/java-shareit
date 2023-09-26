package ru.practicum.shareit.item.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.impl.UserDbRepository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;

@Slf4j
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemDbRepositoryTest {
    private final ItemDbRepository itemDbRepository;
    private final UserDbRepository userDbRepository;
    private Item item1;
    private Item item2;
    private Item item3;
    private Collection<Item> itemList;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void createItems() {
        user1 = User.builder()
                .name("theUser1")
                .email("theUser1@mail.org")
                .build();
        user2 = User.builder()
                .name("theUser2")
                .email("theUser2@mail.org")
                .build();
        user3 = User.builder()
                .name("theUser3")
                .email("theUser3@mail.org")
                .build();
        userDbRepository.create(user1);
        userDbRepository.create(user2);
        userDbRepository.create(user3);

        item1 = Item.builder()
                .name("item")
                .description("a description of an item")
                .owner(user1.getId())
                .available(true)
                .request(ItemRequest.builder().build())
                .build();
        item2 = Item.builder()
                .name("item2")
                .description("a description of an item2")
                .owner(user2.getId())
                .available(true)
                .request(ItemRequest.builder().build())
                .build();
        item3 = Item.builder()
                .name("item3")
                .description("a description of an item3")
                .owner(user3.getId())
                .available(true)
                .request(ItemRequest.builder().build())
                .build();
        itemDbRepository.create(item1);
        itemDbRepository.create(item2);
        itemDbRepository.create(item3);

        itemList = List.of(item1, item2, item3);
    }

    @AfterEach
    public void clean() {
        userDbRepository.delete(user1.getId());
        userDbRepository.delete(user2.getId());
        userDbRepository.delete(user3.getId());
//        itemDbRepository.delete(item1.getId());
//        itemDbRepository.delete(item2.getId());
//        itemDbRepository.delete(item3.getId());
    }

    @Test
    public void testContainsReturnsTrue() {
        boolean isItem = itemDbRepository.contains(user1.getId());
        assertTrue(isItem);
    }

    @Test
    public void testContainsReturnsFalseForId4() {
        boolean isItem = itemDbRepository.contains(4);
        assertTrue(isItem);
    }

    @Test
    public void testContainsReturnsFalseForId10() {
        boolean isItem = itemDbRepository.contains(10);
        assertFalse(isItem);
    }

    @Test
    public void testContainsReturnsFalseForId40() {
        boolean isItem = itemDbRepository.contains(40);
        assertFalse(isItem);
    }
}
