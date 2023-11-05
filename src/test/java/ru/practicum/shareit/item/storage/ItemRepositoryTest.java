package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRepositoryTest {
    @Autowired
    private final EntityManager em;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final UserRepository userRepository;

    private Item item1;
    private Item item2;
    private Item item3;
    private User user1;


    @BeforeEach
    public void createItems() {
        user1 = User.builder().name("John").email("john.doe@mail.com").build();

        item1 = Item.builder().name("item1").description("description1").available(true).owner(user1).build();
        item2 = Item.builder().name("item2").description("description2").available(true).owner(user1).build();
        item3 = Item.builder().name("item3").description("description3").available(true).owner(user1).build();
    }

    @Test
    public void testSaveItem1() {
        User createdUser = userRepository.saveAndFlush(user1);
        item1.setOwner(createdUser);
        Item actual = itemRepository.save(item1);
        TypedQuery<User> query = em.createQuery("Select u from User u where u.email = :email", User.class);
        User user = query.setParameter("email", user1.getEmail()).getSingleResult();
        TypedQuery<Item> queryForItem = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item item = queryForItem.setParameter("id", actual.getId()).getSingleResult();

        assertThat(actual, samePropertyValuesAs(item));
        assertThat(actual, Matchers.hasProperty("id", notNullValue()));
        assertThat(actual, Matchers.hasProperty("name", equalTo(item.getName())));
        assertThat(actual, Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual, Matchers.hasProperty("available", equalTo(item.getAvailable())));
        assertThat(actual, Matchers.hasProperty("description", equalTo(item.getDescription())));


        List<Item> items = itemRepository.findAll();
        log.info("1-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("1-users-----------{}", users);
    }


    @Test
    public void testFindByIdItem1() {
        User createdUser = userRepository.save(user1);
        Item createdItem = itemRepository.save(item1);
        TypedQuery<User> queryForUser = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = queryForUser.setParameter("id", createdUser.getId()).getSingleResult();
        TypedQuery<Item> queryForItem = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item item = queryForItem.setParameter("id", createdItem.getId()).getSingleResult();

        Optional<Item> optional = itemRepository.findById(createdItem.getId());
        Item actual = optional.get();

        assertThat(actual, samePropertyValuesAs(item));
        assertThat(actual, Matchers.hasProperty("id", notNullValue()));
        assertThat(actual, Matchers.hasProperty("name", equalTo(item.getName())));
        assertThat(actual, Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual, Matchers.hasProperty("available", equalTo(item.getAvailable())));
        assertThat(actual, Matchers.hasProperty("description", equalTo(item.getDescription())));


        List<Item> items = itemRepository.findAll();
        log.info("2-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("2-users-----------{}", users);
    }


    @Test
    public void testFindAllByOwnerIdOrderByIdUser1() {
        User createdUser = userRepository.save(user1);
        itemRepository.save(item1);
        itemRepository.save(item2);
        itemRepository.save(item3);

        TypedQuery<User> queryForUser = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = queryForUser.setParameter("id", createdUser.getId()).getSingleResult();
        TypedQuery<Item> queryForItemList = em.createQuery("Select it from Item it where it.owner.id = :id order by it.id asc", Item.class);
        List<Item> itemList = queryForItemList.setParameter("id", createdUser.getId()).getResultList();

        log.info("3-itemList-----------{}", itemList);

        List<Item> actual = itemRepository.findAllByOwnerIdOrderById(createdUser.getId());

        assertThat(actual, samePropertyValuesAs(itemList));
        assertThat(actual.get(0), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(0), Matchers.hasProperty("name", equalTo(itemList.get(0).getName())));
        assertThat(actual.get(0), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(0), Matchers.hasProperty("available", equalTo(itemList.get(0).getAvailable())));
        assertThat(actual.get(0), Matchers.hasProperty("description", equalTo(itemList.get(0).getDescription())));

        assertThat(actual.get(1), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(1), Matchers.hasProperty("name", equalTo(itemList.get(1).getName())));
        assertThat(actual.get(1), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(1), Matchers.hasProperty("available", equalTo(itemList.get(1).getAvailable())));
        assertThat(actual.get(1), Matchers.hasProperty("description", equalTo(itemList.get(1).getDescription())));

        assertThat(actual.get(2), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(2), Matchers.hasProperty("name", equalTo(itemList.get(2).getName())));
        assertThat(actual.get(2), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(2), Matchers.hasProperty("available", equalTo(itemList.get(2).getAvailable())));
        assertThat(actual.get(2), Matchers.hasProperty("description", equalTo(itemList.get(2).getDescription())));


        List<Item> items = itemRepository.findAll();
        log.info("3-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("3-users-----------{}", users);
    }


    @Test
    public void testFindAllByDescriptionContainsIgnoreCaseAndAvailableTrue_ShouldReturnThreeItemsWhenSameDescription() {
        User createdUser = userRepository.save(user1);
        item1.setDescription("item1 DeScRIPTIOn ");
        itemRepository.save(item1);
        item2.setDescription("item2 description ");
        itemRepository.save(item2);
        item3.setDescription("item3 DESCRIPTION ");
        itemRepository.save(item3);

        TypedQuery<User> queryForUser = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = queryForUser.setParameter("id", createdUser.getId()).getSingleResult();
        TypedQuery<Item> queryForItemList = em.createQuery("Select it from Item it " +
                "where upper(it.description) like upper(concat('%', :description, '%'))  order by it.id asc", Item.class);
        List<Item> itemList = queryForItemList.setParameter("description", "description").getResultList();

        log.info("4-itemList-----------{}", itemList);

        List<Item> actual = itemRepository.findAllByDescriptionContainsIgnoreCaseAndAvailableTrue("description");

        assertThat(actual, samePropertyValuesAs(itemList));
        assertThat(actual.get(0), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(0), Matchers.hasProperty("name", equalTo(itemList.get(0).getName())));
        assertThat(actual.get(0), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(0), Matchers.hasProperty("available", equalTo(itemList.get(0).getAvailable())));
        assertThat(actual.get(0), Matchers.hasProperty("description", equalTo(itemList.get(0).getDescription())));

        assertThat(actual.get(1), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(1), Matchers.hasProperty("name", equalTo(itemList.get(1).getName())));
        assertThat(actual.get(1), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(1), Matchers.hasProperty("available", equalTo(itemList.get(1).getAvailable())));
        assertThat(actual.get(1), Matchers.hasProperty("description", equalTo(itemList.get(1).getDescription())));

        assertThat(actual.get(2), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(2), Matchers.hasProperty("name", equalTo(itemList.get(2).getName())));
        assertThat(actual.get(2), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(2), Matchers.hasProperty("available", equalTo(itemList.get(2).getAvailable())));
        assertThat(actual.get(2), Matchers.hasProperty("description", equalTo(itemList.get(2).getDescription())));


        List<Item> items = itemRepository.findAll();
        log.info("4-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("4-users-----------{}", users);
    }

    @Test
    public void testFindAllByDescriptionContainsIgnoreCaseAndAvailableTrue_ShouldReturnItem2AndItem3WhenAvailableOfItem1False() {
        User createdUser = userRepository.save(user1);
        item1.setDescription("item1 DeScRIPTIOn ");
        item1.setAvailable(false);
        itemRepository.save(item1);
        item2.setDescription("item2 description ");
        itemRepository.save(item2);
        item3.setDescription("item3 DESCRIPTION ");
        itemRepository.save(item3);

        TypedQuery<User> queryForUser = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = queryForUser.setParameter("id", createdUser.getId()).getSingleResult();
        TypedQuery<Item> queryForItemList = em.createQuery("Select it from Item it " +
                "where it.available = :available and upper(it.description) like upper(concat('%', :description, '%'))  order by it.id asc", Item.class);
        List<Item> itemList = queryForItemList
                .setParameter("available", true)
                .setParameter("description", "description")
                 .getResultList();

        log.info("4-itemList-----------{}", itemList);

        List<Item> actual = itemRepository.findAllByDescriptionContainsIgnoreCaseAndAvailableTrue("description");

        log.info("4-actual-----------{}", actual);

        assertThat(actual, samePropertyValuesAs(itemList));
        assertThat(actual.get(0), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(0), Matchers.hasProperty("name", equalTo(itemList.get(0).getName())));
        assertThat(actual.get(0), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(0), Matchers.hasProperty("available", equalTo(itemList.get(0).getAvailable())));
        assertThat(actual.get(0), Matchers.hasProperty("description", equalTo(itemList.get(0).getDescription())));

        assertThat(actual.get(1), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(1), Matchers.hasProperty("name", equalTo(itemList.get(1).getName())));
        assertThat(actual.get(1), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(1), Matchers.hasProperty("available", equalTo(itemList.get(1).getAvailable())));
        assertThat(actual.get(1), Matchers.hasProperty("description", equalTo(itemList.get(1).getDescription())));

        List<Item> items = itemRepository.findAll();
        log.info("4-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("4-users-----------{}", users);
    }

    @Test
    public void testFindAllByDescriptionContainsIgnoreCaseAndAvailableTrue_ShouldReturnItem1Item3WhenAvailableOfItem1False() {
        User createdUser = userRepository.save(user1);
        item1.setDescription("item1 DeScRIPTIOn ");
        itemRepository.save(item1);
        item2.setDescription("item2 description ");
        item2.setAvailable(false);
        itemRepository.save(item2);
        item3.setDescription("item3 DESCRIPTION ");
        itemRepository.save(item3);

        TypedQuery<User> queryForUser = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = queryForUser.setParameter("id", createdUser.getId()).getSingleResult();
        TypedQuery<Item> queryForItemList = em.createQuery("Select it from Item it " +
                "where it.available = :available and upper(it.description) like upper(concat('%', :description, '%'))  order by it.id asc", Item.class);
        List<Item> itemList = queryForItemList
                .setParameter("available", true)
                .setParameter("description", "description")
                .getResultList();

        log.info("4-itemList-----------{}", itemList);

        List<Item> actual = itemRepository.findAllByDescriptionContainsIgnoreCaseAndAvailableTrue("description");

        log.info("4-actual-----------{}", actual);

        assertThat(actual, samePropertyValuesAs(itemList));
        assertThat(actual.get(0), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(0), Matchers.hasProperty("name", equalTo(itemList.get(0).getName())));
        assertThat(actual.get(0), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(0), Matchers.hasProperty("available", equalTo(itemList.get(0).getAvailable())));
        assertThat(actual.get(0), Matchers.hasProperty("description", equalTo(itemList.get(0).getDescription())));

        assertThat(actual.get(1), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(1), Matchers.hasProperty("name", equalTo(itemList.get(1).getName())));
        assertThat(actual.get(1), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(1), Matchers.hasProperty("available", equalTo(itemList.get(1).getAvailable())));
        assertThat(actual.get(1), Matchers.hasProperty("description", equalTo(itemList.get(1).getDescription())));

        List<Item> items = itemRepository.findAll();
        log.info("4-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("4-users-----------{}", users);
    }

    @Test
    public void testFindAllByDescriptionContainsIgnoreCaseAndAvailableTrue_ShouldReturnItem2Item3WhenAvailableOfItem1False() {
        User createdUser = userRepository.save(user1);
        item1.setDescription("item1 DeScRIPTIOn ");
        itemRepository.save(item1);
        item2.setDescription("item2 description ");
        itemRepository.save(item2);
        item3.setDescription("item3 DESCRIPTION ");
        item3.setAvailable(false);
        itemRepository.save(item3);

        TypedQuery<User> queryForUser = em.createQuery("Select u from User u where u.id = :id", User.class);
        User user = queryForUser.setParameter("id", createdUser.getId()).getSingleResult();
        TypedQuery<Item> queryForItemList = em.createQuery("Select it from Item it " +
                "where it.available = :available and upper(it.description) like upper(concat('%', :description, '%'))  order by it.id asc", Item.class);
        List<Item> itemList = queryForItemList
                .setParameter("available", true)
                .setParameter("description", "description")
                .getResultList();

        log.info("5-itemList-----------{}", itemList);

        List<Item> actual = itemRepository.findAllByDescriptionContainsIgnoreCaseAndAvailableTrue("description");

        log.info("5-actual-----------{}", actual);

        assertThat(actual, samePropertyValuesAs(itemList));
        assertThat(actual.get(0), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(0), Matchers.hasProperty("name", equalTo(itemList.get(0).getName())));
        assertThat(actual.get(0), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(0), Matchers.hasProperty("available", equalTo(itemList.get(0).getAvailable())));
        assertThat(actual.get(0), Matchers.hasProperty("description", equalTo(itemList.get(0).getDescription())));

        assertThat(actual.get(1), Matchers.hasProperty("id", notNullValue()));
        assertThat(actual.get(1), Matchers.hasProperty("name", equalTo(itemList.get(1).getName())));
        assertThat(actual.get(1), Matchers.hasProperty("owner", equalTo(user)));
        assertThat(actual.get(1), Matchers.hasProperty("available", equalTo(itemList.get(1).getAvailable())));
        assertThat(actual.get(1), Matchers.hasProperty("description", equalTo(itemList.get(1).getDescription())));

        List<Item> items = itemRepository.findAll();
        log.info("5-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("5-users-----------{}", users);
    }
}