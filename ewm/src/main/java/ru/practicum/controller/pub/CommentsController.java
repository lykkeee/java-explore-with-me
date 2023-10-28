package ru.practicum.controller.pub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.CommentResponseDto;
import ru.practicum.service.comment.CommentServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/comments")
@Slf4j
@AllArgsConstructor
public class CommentsController {

    private final CommentServiceImpl commentService;

    @GetMapping
    public CommentResponseDto getComment(@RequestParam Long commentId) {
        log.info("Запрос на получение комментария с id={}", commentId);
        CommentResponseDto response = commentService.getComment(commentId);
        log.info("Комментарий получен: {}", response);
        return response;
    }

    @GetMapping("/users/{userId}")
    public List<CommentResponseDto> getUserComments (@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") Integer from,
                                                     @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка комментариев от пользователя id={} c параметрами from: {}, size: {}", userId, from, size);
        List<CommentResponseDto> response = commentService.getUserComments(userId, from, size);
        log.info("Список получен: {}", response);
        return response;
    }

    @GetMapping("/events/{eventId}")
    public List<CommentResponseDto> getEventComments (@PathVariable Long eventId,
                                                      @RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка комментариев к событию id={} c параметрами from: {}, size: {}", eventId, from, size);
        List<CommentResponseDto> response = commentService.getEventComments(eventId, from, size);
        log.info("Список получен: {}", response);
        return response;
    }

}
