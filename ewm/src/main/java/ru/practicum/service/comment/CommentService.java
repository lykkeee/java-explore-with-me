package ru.practicum.service.comment;

import ru.practicum.model.comment.CommentRequestDto;
import ru.practicum.model.comment.CommentResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto addComment(Long eventId, Long userId, CommentRequestDto commentRequestDto);

    CommentResponseDto editComment(Long commentId, Long userId, CommentRequestDto commentRequestDto);

    void deleteComment(Long commentId, Long userId);

    void adminDeleteComment(Long commentId);

    CommentResponseDto getComment(Long commentId);

    List<CommentResponseDto> getUserComments(Long userId, Integer from, Integer size);

    List<CommentResponseDto> getEventComments(Long eventId, Integer from, Integer size);

}
