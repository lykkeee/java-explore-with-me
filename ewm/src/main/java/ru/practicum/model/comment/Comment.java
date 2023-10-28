package ru.practicum.model.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "author", referencedColumnName = "id")
    private User author;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "event", referencedColumnName = "id")
    private Event event;
    private String text;
    private LocalDateTime created;
    private Boolean edited;
}
