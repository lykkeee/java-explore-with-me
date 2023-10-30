package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.service.comment.CommentServiceImpl;

@RestController
@RequestMapping("/admin/comments")
@Slf4j
@AllArgsConstructor
public class AdminCommentsController {

    private final CommentServiceImpl commentService;

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void adminDeleteComment(@PathVariable Long commentId) {
        log.info("Запрос на удаление комментария с id={}", commentId);
        commentService.adminDeleteComment(commentId);
        log.info("Комментарий удален");
    }

}
