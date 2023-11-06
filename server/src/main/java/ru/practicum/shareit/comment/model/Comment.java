package ru.practicum.shareit.comment.model;

import lombok.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private User author;

    @Column(name = "created")
    private LocalDateTime created;


    public void setItem(Item item) {
        this.item = item;
    }

    public void setItem(long itemId) {
        this.item = Item.builder().id(itemId).build();
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public void setAuthor(long authorId) {
        this.author = User.builder().id(authorId).build();
    }
}
