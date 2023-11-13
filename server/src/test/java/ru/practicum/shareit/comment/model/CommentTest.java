package ru.practicum.shareit.comment.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CommentTest {


    private User booker2;
    private Comment comment1;
    private Comment commentCopy1;
    LocalDateTime testStart;
    LocalDateTime testEnd;
    LocalDateTime comment1Created;
    Item item1;
    Item item2;


    @BeforeEach
    public void create() {
        testStart = LocalDateTime.of(2023, 11, 2, 3, 15);
        testEnd = LocalDateTime.of(2023, 11, 2, 5, 15);
        comment1Created = testEnd.plusHours(1);

        User user1 = User.builder().id(1).name("John").email("john.doe@mail.com").build();
        User booker1 = User.builder().id(2).name("John2").email("john2.doe@mail.com").build();
        booker2 = User.builder().id(3).name("John2").email("john2.doe@mail.com").build();
        item1 = Item.builder().id(1).name("item1").description("description1").available(true).owner(user1).build();
        item2 = Item.builder().id(2).name("item2").description("description2").available(true).owner(user1).build();

        comment1 = Comment.builder()
                .id(1)
                .text("test text")
                .item(item1)
                .author(booker1)
                .created(comment1Created)
                .build();

        commentCopy1 = Comment.builder()
                .id(1)
                .text("test text")
                .item(item1)
                .author(booker1)
                .created(comment1Created)
                .build();
    }

    @Test
    void testEqual() throws Exception {
        assertThat(comment1, equalTo(commentCopy1));
    }

    @Test
    void testHash() throws Exception {
        assertThat(comment1.hashCode(), equalTo(commentCopy1.hashCode()));
    }

    @Test
    void testToString() throws Exception {
        assertThat(comment1.toString(), equalTo(commentCopy1.toString()));
    }

    @Test
    void testGetters() throws Exception {
        assertThat(comment1.getId(), equalTo(commentCopy1.getId()));
        assertThat(comment1.getText(), equalTo(commentCopy1.getText()));
        assertThat(comment1.getItem(), equalTo(commentCopy1.getItem()));
        assertThat(comment1.getAuthor(), equalTo(commentCopy1.getAuthor()));
        assertThat(comment1.getCreated(), equalTo(commentCopy1.getCreated()));

    }

    @Test
    void testSetters() throws Exception {
        LocalDateTime c = comment1Created;
        c.plusHours(2);

        comment1.setId(2);
        comment1.setText("NEW TEXT");
        comment1.setItem(item2);
        comment1.setAuthor(booker2);
        comment1.setCreated(c);

        commentCopy1.setId(2);
        commentCopy1.setText("NEW TEXT");
        commentCopy1.setItem(item2);
        commentCopy1.setAuthor(booker2);
        commentCopy1.setCreated(c);

        assertThat(comment1.getId(), equalTo(commentCopy1.getId()));
        assertThat(comment1.getText(), equalTo(commentCopy1.getText()));
        assertThat(comment1.getItem(), equalTo(commentCopy1.getItem()));
        assertThat(comment1.getAuthor(), equalTo(commentCopy1.getAuthor()));
        assertThat(comment1.getCreated(), equalTo(commentCopy1.getCreated()));
    }
}
