package ru.practicum.controller.privacy_user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.request.RequestResponseDto;
import ru.practicum.service.event.EventServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@Slf4j
@AllArgsConstructor
public class UserRequestsController {

    private final EventServiceImpl eventService;

    @GetMapping
    public List<RequestResponseDto> getUsersRequests(@PathVariable Long userId) {
        log.info("Запрос на получение списка заявок пользователя с id: {}", userId);
        List<RequestResponseDto> response = eventService.getUsersRequests(userId);
        log.info("Список получен");
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RequestResponseDto addRequest(@PathVariable Long userId,
                                         @RequestParam Long eventId) {
        log.info("Запрос на добавление запроса от пользователя с id {} к событию с id {}", userId, eventId);
        RequestResponseDto response = eventService.addRequest(userId, eventId);
        log.info("Запрос добавлен: {}", response);
        return response;
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestResponseDto cancelRequest(@PathVariable Long userId,
                                            @PathVariable Long requestId) {
        log.info("Запрос на отмену запроса с id {}", requestId);
        RequestResponseDto response = eventService.cancelRequest(userId, requestId);
        log.info("Запрос отменён: {}", response);
        return response;
    }
}
