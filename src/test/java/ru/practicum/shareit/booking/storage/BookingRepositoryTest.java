package ru.practicum.shareit.booking.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;

@Slf4j
@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingRepositoryTest {
    @Autowired
    private final EntityManager em;

    @Autowired
    private final ItemRepository itemRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final BookingRepository bookingRepository;


    private Item item1;
    private Item item2;
    private Item item3;
    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;
    private User user6;
    private User user7;
    private User user8;
    private User user9;
    private User user10;
    private User user11;
    private User user12;


    private Booking booking1;
    private Booking booking2;
    private Booking booking3;

    private Booking booking4;
    private Booking booking5;
    private Booking booking6;

    private Booking booking7;
    private Booking booking8;
    private Booking booking9;

    private Pageable sortedByStartDesc;


    public void create(LocalDateTime current) {
        sortedByStartDesc = PageRequest.of(0, 10, Sort.by("start").descending());

        user1 = User.builder().name("John").email("john.doe@mail.com").build();
        user2 = User.builder().name("John2").email("john2.doe@mail.com").build();
        user3 = User.builder().name("John3").email("john3.doe@mail.com").build();
        user4 = User.builder().name("John4").email("john4.doe@mail.com").build();
        user5 = User.builder().name("John5").email("john5.doe@mail.com").build();
        user6 = User.builder().name("John6").email("john6.doe@mail.com").build();
        user7 = User.builder().name("John7").email("john7.doe@mail.com").build();
        user8 = User.builder().name("John8").email("john8.doe@mail.com").build();
        user9 = User.builder().name("John9").email("john9.doe@mail.com").build();
        user10 = User.builder().name("John10").email("john10.doe@mail.com").build();
        user11 = User.builder().name("John11").email("john11.doe@mail.com").build();
        user12 = User.builder().name("John12").email("john12.doe@mail.com").build();

        item1 = Item.builder().name("item1").description("description1").available(true).owner(user1).build();
        item2 = Item.builder().name("ite2").description("description2").available(true).owner(user2).build();
        item3 = Item.builder().name("item3").description("description3").available(true).owner(user3).build();

        // item1 bookings OWNER user1
        booking1 = Booking.builder().start(current.plusSeconds(1)).end(current.plusSeconds(2)).build();
        booking2 = Booking.builder().start(current.plusSeconds(3)).end(current.plusSeconds(4)).build();
        booking3 = Booking.builder().start(current.plusSeconds(5)).end(current.plusSeconds(6)).build();

        // item2 bookings OWNER user2
        booking4 = Booking.builder().start(current.plusMinutes(10).plusHours(1)).end(current.plusMinutes(10).plusHours(2)).build();
        booking5 = Booking.builder().start(current.plusMinutes(15).plusHours(2)).end(current.plusMinutes(15).plusHours(3)).build();
        booking6 = Booking.builder().start(current.plusMinutes(20).plusHours(3)).end(current.plusMinutes(20).plusHours(4)).build();

        // item3 bookings OWNER user3
        booking7 = Booking.builder().start(current.plusHours(10).plusDays(2)).end(current.plusHours(10).plusDays(3)).build();
        booking8 = Booking.builder().start(current.plusHours(15).plusDays(3)).end(current.plusHours(15).plusDays(4)).build();
        booking9 = Booking.builder().start(current.plusHours(20).plusDays(4)).end(current.plusHours(20).plusDays(5)).build();


    }

    @Test
    public void testSaveBooking1() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdBooker2 = userRepository.save(user2);

        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        booking1.setBooker(createdBooker2);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Booking actual = bookingRepository.save(booking1);

        TypedQuery<User> queryForOwner = em.createQuery("Select u from User u where u.id = :id", User.class);
        User owner = queryForOwner.setParameter("id", createdOwner1.getId()).getSingleResult();
        TypedQuery<User> queryForBooker = em.createQuery("Select u from User u where u.id = :id", User.class);
        User booker = queryForBooker.setParameter("id", createdBooker2.getId()).getSingleResult();
        TypedQuery<Item> queryForItem = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item item = queryForItem.setParameter("id", createdItem1.getId()).getSingleResult();
        TypedQuery<Booking> queryForBooking = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = queryForBooking.setParameter("id", actual.getId()).getSingleResult();

        assertThat(actual, samePropertyValuesAs(booking));
        assertThat(actual, Matchers.hasProperty("id", equalTo(booking.getId())));
        assertThat(actual, Matchers.hasProperty("start", equalTo(booking.getStart())));
        assertThat(actual, Matchers.hasProperty("booker", equalTo(booker)));
        assertThat(actual, Matchers.hasProperty("booker", equalTo(booking.getBooker())));
        assertThat(actual, Matchers.hasProperty("item", equalTo(item)));
        assertThat(actual, Matchers.hasProperty("item", equalTo(booking.getItem())));
        assertThat(actual.getItem(), Matchers.hasProperty("owner", equalTo(booking.getItem().getOwner())));
        assertThat(actual.getItem(), Matchers.hasProperty("owner", equalTo(owner)));
        assertThat(actual, Matchers.hasProperty("status", equalTo(booking.getStatus())));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("1-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("1-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("1-users-----------{}", users);
    }

    @Test
    public void testFindByIdBooking1() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdBooker2 = userRepository.save(user2);

        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        booking1.setBooker(createdBooker2);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Booking createdBooking1 = bookingRepository.save(booking1);
        Booking actual = bookingRepository.findById(createdBooking1.getId()).get();

        TypedQuery<User> queryForOwner = em.createQuery("Select u from User u where u.id = :id", User.class);
        User owner = queryForOwner.setParameter("id", createdOwner1.getId()).getSingleResult();
        TypedQuery<User> queryForBooker = em.createQuery("Select u from User u where u.id = :id", User.class);
        User booker = queryForBooker.setParameter("id", createdBooker2.getId()).getSingleResult();
        TypedQuery<Item> queryForItem = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item item = queryForItem.setParameter("id", createdItem1.getId()).getSingleResult();
        TypedQuery<Booking> queryForBooking = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = queryForBooking.setParameter("id", createdBooking1.getId()).getSingleResult();

        assertThat(actual, samePropertyValuesAs(booking));
        assertThat(actual, Matchers.hasProperty("id", equalTo(booking.getId())));
        assertThat(actual, Matchers.hasProperty("start", equalTo(booking.getStart())));
        assertThat(actual, Matchers.hasProperty("booker", equalTo(booker)));
        assertThat(actual, Matchers.hasProperty("booker", equalTo(booking.getBooker())));
        assertThat(actual, Matchers.hasProperty("item", equalTo(item)));
        assertThat(actual, Matchers.hasProperty("item", equalTo(booking.getItem())));
        assertThat(actual.getItem(), Matchers.hasProperty("owner", equalTo(booking.getItem().getOwner())));
        assertThat(actual.getItem(), Matchers.hasProperty("owner", equalTo(owner)));
        assertThat(actual, Matchers.hasProperty("status", equalTo(booking.getStatus())));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("2-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("2-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("2-users-----------{}", users);
    }

    @Test
    public void testExistsByIdBooking1True() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdBooker2 = userRepository.save(user2);

        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        booking1.setBooker(createdBooker2);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Booking createdBooking1 = bookingRepository.save(booking1);
        Boolean actual = bookingRepository.existsById(createdBooking1.getId());

        TypedQuery<User> queryForOwner = em.createQuery("Select u from User u where u.id = :id", User.class);
        User owner = queryForOwner.setParameter("id", createdOwner1.getId()).getSingleResult();
        TypedQuery<User> queryForBooker = em.createQuery("Select u from User u where u.id = :id", User.class);
        User booker = queryForBooker.setParameter("id", createdBooker2.getId()).getSingleResult();
        TypedQuery<Item> queryForItem = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item item = queryForItem.setParameter("id", createdItem1.getId()).getSingleResult();
        TypedQuery<Booking> queryForBooking = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking = queryForBooking.setParameter("id", createdBooking1.getId()).getSingleResult();

        assertThat(actual, equalTo(booking != null));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("3-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("3-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("3-users-----------{}", users);
    }

    @Test
    public void testExistsByIdBooking1False() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdBooker2 = userRepository.save(user2);

        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        booking1.setBooker(createdBooker2);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Boolean actual = bookingRepository.existsById(4L);

        TypedQuery<User> queryForOwner = em.createQuery("Select u from User u where u.id = :id", User.class);
        User owner = queryForOwner.setParameter("id", createdOwner1.getId()).getSingleResult();
        TypedQuery<User> queryForBooker = em.createQuery("Select u from User u where u.id = :id", User.class);
        User booker = queryForBooker.setParameter("id", createdBooker2.getId()).getSingleResult();
        TypedQuery<Item> queryForItem = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item item = queryForItem.setParameter("id", createdItem1.getId()).getSingleResult();
        TypedQuery<Booking> queryForBooking = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking;
        try {
            booking = queryForBooking.setParameter("id", 4L).getSingleResult();
        } catch (NoResultException e) {
            booking = null;
        }

        assertThat(actual, equalTo(booking != null));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("4-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("4-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("4-users-----------{}", users);
    }


    @Test
    public void testFindAllByBookerIdOrderByStartDesc_WhereUser4BooksItem1() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1ForItem1 = userRepository.save(user4);
        User createdBooker2ForItem1 = userRepository.save(user5);
        User createdBooker3ForItem1 = userRepository.save(user6);

        //bookers of item2
        User createdBooker1ForItem2 = userRepository.save(user7);
        User createdBooker2ForItem2 = userRepository.save(user8);
        User createdBooker3ForItem2 = userRepository.save(user9);

        //bookers of item3
        User createdBooker1ForItem3 = userRepository.save(user10);
        User createdBooker2ForItem3 = userRepository.save(user11);
        User createdBooker3ForItem3 = userRepository.save(user12);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1ForItem1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker2ForItem1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.WAITING);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker3ForItem1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.WAITING);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1ForItem2);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.WAITING);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker2ForItem2);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.WAITING);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker3ForItem2);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.WAITING);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1ForItem3);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.WAITING);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker2ForItem3);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.WAITING);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker3ForItem3);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.WAITING);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerId(createdBooker1ForItem1.getId(), sortedByStartDesc)
                .stream().collect(Collectors.toList());

        TypedQuery<User> queryForOwner = em.createQuery("Select u from User u where u.id = :id", User.class);
        User ownerFromQuery1 = queryForOwner.setParameter("id", createdOwner1.getId()).getSingleResult();

        TypedQuery<User> queryForBooker = em.createQuery("Select u from User u where u.id = :id", User.class);
        User booker1ForItem1FromQuery = queryForBooker.setParameter("id", createdBooker1ForItem1.getId()).getSingleResult();

        TypedQuery<Item> queryForItem = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item item1FromQuery = queryForItem.setParameter("id", createdItem1.getId()).getSingleResult();

        TypedQuery<Booking> queryForBooking = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking booking1FromQuery = queryForBooking.setParameter("id", createdBooking1.getId()).getSingleResult();

        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id", Booking.class);
        List<Booking> bookingListForItem1Booker1FromQuery = queryForBookingList.setParameter("id", createdBooker1ForItem1.getId()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListForItem1Booker1FromQuery));
        assertThat(actual1.get(0), Matchers.hasProperty("id", equalTo(booking1FromQuery.getId())));
        assertThat(actual1.get(0), Matchers.hasProperty("start", equalTo(booking1FromQuery.getStart())));
        assertThat(actual1.get(0), Matchers.hasProperty("booker", equalTo(booker1ForItem1FromQuery)));
        assertThat(actual1.get(0), Matchers.hasProperty("booker", equalTo(booking1FromQuery.getBooker())));
        assertThat(actual1.get(0), Matchers.hasProperty("item", equalTo(item1FromQuery)));
        assertThat(actual1.get(0), Matchers.hasProperty("item", equalTo(booking1FromQuery.getItem())));
        assertThat(actual1.get(0).getItem(), Matchers.hasProperty("owner", equalTo(booking1FromQuery.getItem().getOwner())));
        assertThat(actual1.get(0).getItem(), Matchers.hasProperty("owner", equalTo(ownerFromQuery1)));
        assertThat(actual1.get(0), Matchers.hasProperty("status", equalTo(booking1FromQuery.getStatus())));

        List<Booking> bookings = bookingRepository.findAll();
        log.info("5-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("5-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("5-users-----------{}", users);
    }

    @Test
    public void testFindAllByBookerIdOrderByStartDesc_WhereUser5BooksItem1() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1ForItem1 = userRepository.save(user4);
        User createdBooker2ForItem1 = userRepository.save(user5);
        User createdBooker3ForItem1 = userRepository.save(user6);

        //bookers of item2
        User createdBooker1ForItem2 = userRepository.save(user7);
        User createdBooker2ForItem2 = userRepository.save(user8);
        User createdBooker3ForItem2 = userRepository.save(user9);

        //bookers of item3
        User createdBooker1ForItem3 = userRepository.save(user10);
        User createdBooker2ForItem3 = userRepository.save(user11);
        User createdBooker3ForItem3 = userRepository.save(user12);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1ForItem1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker2ForItem1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.WAITING);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker3ForItem1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.WAITING);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1ForItem2);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.WAITING);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker2ForItem2);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.WAITING);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker3ForItem2);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.WAITING);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1ForItem3);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.WAITING);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker2ForItem3);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.WAITING);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker3ForItem3);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.WAITING);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerId(createdBooker2ForItem1.getId(), sortedByStartDesc)
                .stream().collect(Collectors.toList());

        TypedQuery<User> queryForOwner = em.createQuery("Select u from User u where u.id = :id", User.class);
        User owner1FromQuery = queryForOwner.setParameter("id", createdOwner1.getId()).getSingleResult();

        TypedQuery<User> queryForBooker = em.createQuery("Select u from User u where u.id = :id", User.class);
        User bookerForItem1FromQuery = queryForBooker.setParameter("id", createdBooker2ForItem1.getId()).getSingleResult();

        TypedQuery<Item> queryForItem = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item item1FromQuery = queryForItem.setParameter("id", createdItem1.getId()).getSingleResult();

        TypedQuery<Booking> queryForBooking = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking bookingFromQuery = queryForBooking.setParameter("id", createdBooking2.getId()).getSingleResult();

        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id", Booking.class);
        List<Booking> bookingListForItem1Booker2FromQuery = queryForBookingList.setParameter("id", createdBooker2ForItem1.getId()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListForItem1Booker2FromQuery));
        assertThat(actual1.get(0), Matchers.hasProperty("id", equalTo(bookingFromQuery.getId())));
        assertThat(actual1.get(0), Matchers.hasProperty("start", equalTo(bookingFromQuery.getStart())));
        assertThat(actual1.get(0), Matchers.hasProperty("booker", equalTo(bookerForItem1FromQuery)));
        assertThat(actual1.get(0), Matchers.hasProperty("booker", equalTo(bookingFromQuery.getBooker())));
        assertThat(actual1.get(0), Matchers.hasProperty("item", equalTo(item1FromQuery)));
        assertThat(actual1.get(0), Matchers.hasProperty("item", equalTo(bookingFromQuery.getItem())));
        assertThat(actual1.get(0).getItem(), Matchers.hasProperty("owner", equalTo(bookingFromQuery.getItem().getOwner())));
        assertThat(actual1.get(0).getItem(), Matchers.hasProperty("owner", equalTo(owner1FromQuery)));
        assertThat(actual1.get(0), Matchers.hasProperty("status", equalTo(bookingFromQuery.getStatus())));

        List<Booking> bookings = bookingRepository.findAll();
        log.info("6-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("6-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("6-users-----------{}", users);
    }

    @Test
    public void testFindAllByBookerIdOrderByStartDesc_WhereUser6BooksItem1() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1ForItem1 = userRepository.save(user4);
        User createdBooker2ForItem1 = userRepository.save(user5);
        User createdBooker3ForItem1 = userRepository.save(user6);

        //bookers of item2
        User createdBooker1ForItem2 = userRepository.save(user7);
        User createdBooker2ForItem2 = userRepository.save(user8);
        User createdBooker3ForItem2 = userRepository.save(user9);

        //bookers of item3
        User createdBooker1ForItem3 = userRepository.save(user10);
        User createdBooker2ForItem3 = userRepository.save(user11);
        User createdBooker3ForItem3 = userRepository.save(user12);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1ForItem1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker2ForItem1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.WAITING);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker3ForItem1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.WAITING);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1ForItem2);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.WAITING);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker2ForItem2);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.WAITING);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker3ForItem2);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.WAITING);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1ForItem3);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.WAITING);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker2ForItem3);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.WAITING);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker3ForItem3);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.WAITING);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerId(createdBooker3ForItem1.getId(), sortedByStartDesc)
                .stream().collect(Collectors.toList());

        TypedQuery<User> queryForOwner = em.createQuery("Select u from User u where u.id = :id", User.class);
        User owner1FromQuery = queryForOwner.setParameter("id", createdOwner1.getId()).getSingleResult();

        TypedQuery<User> queryForBooker = em.createQuery("Select u from User u where u.id = :id", User.class);
        User bookerForItem1FromQuery = queryForBooker.setParameter("id", createdBooker3ForItem1.getId()).getSingleResult();

        TypedQuery<Item> queryForItem = em.createQuery("Select it from Item it where it.id = :id", Item.class);
        Item item1FromQuery = queryForItem.setParameter("id", createdItem1.getId()).getSingleResult();

        TypedQuery<Booking> queryForBooking = em.createQuery("Select b from Booking b where b.id = :id", Booking.class);
        Booking bookingFromQuery = queryForBooking.setParameter("id", createdBooking3.getId()).getSingleResult();

        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id", Booking.class);
        List<Booking> bookingListForItem1Booker2FromQuery = queryForBookingList.setParameter("id", createdBooker3ForItem1.getId()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListForItem1Booker2FromQuery));
        assertThat(actual1.get(0), Matchers.hasProperty("id", equalTo(bookingFromQuery.getId())));
        assertThat(actual1.get(0), Matchers.hasProperty("start", equalTo(bookingFromQuery.getStart())));
        assertThat(actual1.get(0), Matchers.hasProperty("booker", equalTo(bookerForItem1FromQuery)));
        assertThat(actual1.get(0), Matchers.hasProperty("booker", equalTo(bookingFromQuery.getBooker())));
        assertThat(actual1.get(0), Matchers.hasProperty("item", equalTo(item1FromQuery)));
        assertThat(actual1.get(0), Matchers.hasProperty("item", equalTo(bookingFromQuery.getItem())));
        assertThat(actual1.get(0).getItem(), Matchers.hasProperty("owner", equalTo(bookingFromQuery.getItem().getOwner())));
        assertThat(actual1.get(0).getItem(), Matchers.hasProperty("owner", equalTo(owner1FromQuery)));
        assertThat(actual1.get(0), Matchers.hasProperty("status", equalTo(bookingFromQuery.getStatus())));

        List<Booking> bookings = bookingRepository.findAll();
        log.info("7-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("7-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("7-users-----------{}", users);
    }

    @Test
    public void testFindAllByBookerIdOrderByStartDesc_WhereUser4BooksItem1Item2Item3AllStateWaiting() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1 = userRepository.save(user4);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.WAITING);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.WAITING);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.WAITING);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker1);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.WAITING);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker1);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.WAITING);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.WAITING);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker1);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.WAITING);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker1);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.WAITING);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerId(createdBooker1.getId(), sortedByStartDesc)
                .stream().collect(Collectors.toList());


        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id order by b.start desc", Booking.class);
        List<Booking> bookingListFromQuery = queryForBookingList.setParameter("id", createdBooker1.getId()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListFromQuery));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("8-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("8-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("8-users-----------{}", users);
    }

    @Test
    public void testFindAllByBookerIdOrderByStartDesc_WhereUser4BooksItem1Item2Item3AllStateRejected() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1 = userRepository.save(user4);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.REJECTED);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.REJECTED);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.REJECTED);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.REJECTED);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker1);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.REJECTED);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker1);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.REJECTED);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.REJECTED);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker1);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.REJECTED);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker1);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.REJECTED);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerId(createdBooker1.getId(), sortedByStartDesc)
                .stream().collect(Collectors.toList());


        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id  order by b.start desc", Booking.class);
        List<Booking> bookingListFromQuery = queryForBookingList.setParameter("id", createdBooker1.getId()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListFromQuery));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("8-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("8-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("8-users-----------{}", users);
    }

    @Test
    public void testFindAllByBookerIdOrderByStartDesc_WhereUser4BooksItem1Item2Item3AllStateApproved() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1 = userRepository.save(user4);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.APPROVED);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.APPROVED);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.APPROVED);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.APPROVED);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker1);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.APPROVED);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker1);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.APPROVED);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.APPROVED);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker1);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.APPROVED);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker1);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.APPROVED);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerId(createdBooker1.getId(), sortedByStartDesc)
                .stream().collect(Collectors.toList());


        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id order by b.start desc", Booking.class);
        List<Booking> bookingListFromQuery = queryForBookingList.setParameter("id", createdBooker1.getId()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListFromQuery));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("8-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("8-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("8-users-----------{}", users);
    }

    @Test
    public void testFindAllByBookerIdOrderByStartDesc_WhereUser4BooksItem1Item2Item3DifferentStates() {
        create(LocalDateTime.now());

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1 = userRepository.save(user4);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.APPROVED);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.REJECTED);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.WAITING);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker1);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.APPROVED);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker1);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.REJECTED);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.WAITING);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker1);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.APPROVED);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker1);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.REJECTED);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerId(createdBooker1.getId(), sortedByStartDesc)
                .stream().collect(Collectors.toList());


        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id order by b.start desc", Booking.class);
        List<Booking> bookingListFromQuery = queryForBookingList.setParameter("id", createdBooker1.getId()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListFromQuery));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("8-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("8-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("8-users-----------{}", users);
    }

    @Test
    public void testFindAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc_WhereUser4BooksItem1Item2Item3AllStateWaiting() {
        create(LocalDateTime.now().minusDays(3));

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1 = userRepository.save(user4);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.WAITING);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.WAITING);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.WAITING);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker1);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.WAITING);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker1);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.WAITING);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.WAITING);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker1);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.WAITING);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker1);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.WAITING);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(createdBooker1.getId(), LocalDateTime.now(), LocalDateTime.now(), sortedByStartDesc)
                .stream().collect(Collectors.toList());


        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id and b.start < :start and b.end > :end order by b.start desc", Booking.class);
        List<Booking> bookingListFromQuery = queryForBookingList.setParameter("id", createdBooker1.getId()).setParameter("start", LocalDateTime.now()).setParameter("end", LocalDateTime.now()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListFromQuery));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("8-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("8-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("8-users-----------{}", users);
    }

    @Test
    public void testFindAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc_WhereUser4BooksItem1Item2Item3AllStateRejected() {
        create(LocalDateTime.now().minusDays(3));

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1 = userRepository.save(user4);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.REJECTED);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.REJECTED);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.REJECTED);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.REJECTED);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker1);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.REJECTED);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker1);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.REJECTED);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.REJECTED);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker1);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.REJECTED);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker1);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.REJECTED);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerId(createdBooker1.getId(), sortedByStartDesc)
                .stream()
                .collect(Collectors.toList());


        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id  order by b.start desc", Booking.class);
        List<Booking> bookingListFromQuery = queryForBookingList.setParameter("id", createdBooker1.getId()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListFromQuery));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("8-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("8-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("8-users-----------{}", users);
    }

    @Test
    public void testFindAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc_WhereUser4BooksItem1Item2Item3AllStateApproved() {
        create(LocalDateTime.now().minusDays(3));

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1 = userRepository.save(user4);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.APPROVED);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.APPROVED);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.APPROVED);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.APPROVED);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker1);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.APPROVED);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker1);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.APPROVED);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.APPROVED);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker1);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.APPROVED);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker1);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.APPROVED);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerId(createdBooker1.getId(), sortedByStartDesc)
                .stream()
                .collect(Collectors.toList());


        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id order by b.start desc", Booking.class);
        List<Booking> bookingListFromQuery = queryForBookingList.setParameter("id", createdBooker1.getId()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListFromQuery));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("8-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("8-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("8-users-----------{}", users);
    }

    @Test
    public void testFindAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc_WhereUser4BooksItem1Item2Item3DifferentStates() {
        create(LocalDateTime.now().minusDays(3));

        User createdOwner1 = userRepository.save(user1);
        User createdOwner2 = userRepository.save(user2);
        User createdOwner3 = userRepository.save(user3);

        //bookers of item1
        User createdBooker1 = userRepository.save(user4);


        item1.setOwner(createdOwner1);
        Item createdItem1 = itemRepository.save(item1);

        item2.setOwner(createdOwner2);
        Item createdItem2 = itemRepository.save(item2);

        item3.setOwner(createdOwner3);
        Item createdItem3 = itemRepository.save(item3);

        //saving item1 bookings
        booking1.setBooker(createdBooker1);
        booking1.setItem(createdItem1);
        booking1.setStatus(Status.WAITING);
        Booking createdBooking1 = bookingRepository.save(booking1);

        booking2.setBooker(createdBooker1);
        booking2.setItem(createdItem1);
        booking2.setStatus(Status.APPROVED);
        Booking createdBooking2 = bookingRepository.save(booking2);

        booking3.setBooker(createdBooker1);
        booking3.setItem(createdItem1);
        booking3.setStatus(Status.REJECTED);
        Booking createdBooking3 = bookingRepository.save(booking3);


        //saving item2 bookings
        booking4.setBooker(createdBooker1);
        booking4.setItem(createdItem2);
        booking4.setStatus(Status.WAITING);
        Booking createdBooking4 = bookingRepository.save(booking4);

        booking5.setBooker(createdBooker1);
        booking5.setItem(createdItem2);
        booking5.setStatus(Status.APPROVED);
        Booking createdBooking5 = bookingRepository.save(booking5);

        booking6.setBooker(createdBooker1);
        booking6.setItem(createdItem2);
        booking6.setStatus(Status.REJECTED);
        Booking createdBooking6 = bookingRepository.save(booking6);

        //saving item3 bookings
        booking7.setBooker(createdBooker1);
        booking7.setItem(createdItem3);
        booking7.setStatus(Status.WAITING);
        Booking createdBooking7 = bookingRepository.save(booking7);

        booking8.setBooker(createdBooker1);
        booking8.setItem(createdItem3);
        booking8.setStatus(Status.APPROVED);
        Booking createdBooking8 = bookingRepository.save(booking8);

        booking9.setBooker(createdBooker1);
        booking9.setItem(createdItem3);
        booking9.setStatus(Status.REJECTED);
        Booking createdBooking9 = bookingRepository.save(booking9);

        List<Booking> actual1 = bookingRepository.findAllByBookerId(createdBooker1.getId(), sortedByStartDesc)
                .stream()
                .collect(Collectors.toList());


        TypedQuery<Booking> queryForBookingList = em.createQuery("Select b from Booking b where b.booker.id = :id order by b.start desc", Booking.class);
        List<Booking> bookingListFromQuery = queryForBookingList.setParameter("id", createdBooker1.getId()).getResultList();

        assertThat(actual1, samePropertyValuesAs(bookingListFromQuery));


        List<Booking> bookings = bookingRepository.findAll();
        log.info("8-bookings-----------{}", bookings);
        List<Item> items = itemRepository.findAll();
        log.info("8-items-----------{}", items);
        List<User> users = userRepository.findAll();
        log.info("8-users-----------{}", users);
    }
}