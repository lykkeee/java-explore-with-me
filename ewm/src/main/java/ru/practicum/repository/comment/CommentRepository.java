package ru.practicum.repository.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.comment.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByAuthorId(Long authorId, Pageable pageable);

    Page<Comment> findByEventId(Long userId, Pageable pageable);

}
