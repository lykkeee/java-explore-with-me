package ru.practicum.service.comment;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictConditionsNotMetException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.CommentRequestDto;
import ru.practicum.model.comment.CommentResponseDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;
import ru.practicum.repository.comment.CommentRepository;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final CommentRepository commentRepository;

    @Override
    public CommentResponseDto addComment(Long eventId, Long userId, CommentRequestDto commentRequestDto) {
        Comment comment = CommentMapper.toModel(getEventById(eventId), getUserById(userId), commentRequestDto);
        return CommentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public CommentResponseDto editComment(Long commentId, Long userId, CommentRequestDto commentRequestDto) {
        Comment comment = getCommentById(commentId);
        getUserById(userId);
        checkCommentOwner(comment, userId);
        CommentMapper.toModel(commentRequestDto, comment);
        return CommentMapper.toResponse(commentRepository.save(comment));
    }

    @Override
    public void deleteComment(Long commentId, Long userId) {
        Comment comment = getCommentById(commentId);
        getUserById(userId);
        checkCommentOwner(comment, userId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public void adminDeleteComment(Long commentId) {
        getComment(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentResponseDto getComment(Long commentId) {
        return CommentMapper.toResponse(getCommentById(commentId));
    }

    @Override
    public List<CommentResponseDto> getUserComments(Long userId, Integer from, Integer size) {
        getUserById(userId);
        List<Comment> comments = commentRepository.findByAuthorId(userId, PageRequest.of(from / size, size)).toList();
        return comments.stream().map(CommentMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<CommentResponseDto> getEventComments(Long eventId, Integer from, Integer size) {
        getEventById(eventId);
        List<Comment> comments = commentRepository.findByAuthorId(eventId, PageRequest.of(from / size, size)).toList();
        return comments.stream().map(CommentMapper::toResponse).collect(Collectors.toList());
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id=" + userId + " was not found"));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event with id=" + eventId + " was not found"));
    }

    private Comment getCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new DataNotFoundException("Comment with id=" + commentId + " was not found"));
    }

    private void checkCommentOwner(Comment comment, Long userId) {
        if (!userId.equals(comment.getAuthor().getId())) {
            throw new ConflictConditionsNotMetException("User id=" + userId + " is not owner of comment id=" + comment.getId());
        }
    }
}
