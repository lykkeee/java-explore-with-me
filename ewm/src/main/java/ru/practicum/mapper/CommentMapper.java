package ru.practicum.mapper;

import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.CommentRequestDto;
import ru.practicum.model.comment.CommentResponseDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

public class CommentMapper {

    public static Comment toModel(Event event, User author, CommentRequestDto commentRequestDto) {
        Comment comment = new Comment();
        comment.setAuthor(author);
        comment.setEvent(event);
        comment.setText(commentRequestDto.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setEdited(false);
        return comment;
    }

    public static Comment toModel(CommentRequestDto commentRequestDto, Comment comment) {
        comment.setText(commentRequestDto.getText());
        comment.setEdited(true);
        return comment;
    }

    public static CommentResponseDto toResponse(Comment comment) {
        CommentResponseDto commentResponseDto = new CommentResponseDto();
        commentResponseDto.setAuthor(comment.getAuthor().getId());
        commentResponseDto.setEvent(comment.getEvent().getId());
        commentResponseDto.setText(comment.getText());
        commentResponseDto.setCreated(comment.getCreated());
        commentResponseDto.setEdited(comment.getEdited());
        return commentResponseDto;
    }
}
