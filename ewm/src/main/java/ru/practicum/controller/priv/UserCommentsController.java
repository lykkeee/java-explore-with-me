package ru.practicum.controller.priv;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.CommentRequestDto;
import ru.practicum.model.comment.CommentResponseDto;
import ru.practicum.service.comment.CommentServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/comments")
@Slf4j
@AllArgsConstructor
public class UserCommentsController {

    private final CommentServiceImpl commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto addComment(@RequestParam Long eventId,
                                         @PathVariable Long userId,
                                         @RequestBody @Valid CommentRequestDto commentRequestDto) {
        log.info("Запрос на создание комментария пользователем с id={} к событию с id={}", userId, eventId);
        CommentResponseDto response = commentService.addComment(eventId, userId, commentRequestDto);
        log.info("Комментарий добавлен: {}", response);
        return response;
    }

    @PatchMapping
    public CommentResponseDto editComment(@RequestParam Long commentId,
                                          @PathVariable Long userId,
                                          @RequestBody @Valid CommentRequestDto commentRequestDto) {
        log.info("Запрос на обновление комментария пользователем с id={} к событию с id={}", userId, commentId);
        CommentResponseDto response = commentService.editComment(commentId, userId, commentRequestDto);
        log.info("Комментарий обновлен: {}", response);
        return response;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment (@RequestParam Long commentId,
                               @PathVariable Long userId) {
        log.info("Запрос на удаление комментария с id={} пользователем с id={}", commentId, userId);
        commentService.deleteComment(commentId, userId);
        log.info("Комментарий удален");
    }
}
