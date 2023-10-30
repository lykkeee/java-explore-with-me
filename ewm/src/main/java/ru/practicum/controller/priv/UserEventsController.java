package ru.practicum.controller.priv;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.event.EventFullDto;
import ru.practicum.model.event.EventRequestDto;
import ru.practicum.model.event.EventShortDto;
import ru.practicum.model.event.EventUpdateDto;
import ru.practicum.model.request.RequestRequestDto;
import ru.practicum.model.request.RequestResponseDto;
import ru.practicum.model.request.RequestStatusUpdateDto;
import ru.practicum.service.event.EventServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
@Slf4j
@AllArgsConstructor
public class UserEventsController {
    private final EventServiceImpl eventService;

    @GetMapping
    public List<EventShortDto> getUsersEvents(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка событий, добавленных пользователем с id: {}, параметры from {}, size {}", userId, from, size);
        List<EventShortDto> response = eventService.getUsersEvents(userId, from, size);
        log.info("Список получен: {}", response);
        return response;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                  @RequestBody @Valid EventRequestDto eventRequestDto) {
        log.info("Запрос на добавление события пользователем с id {}: {}", userId, eventRequestDto);
        EventFullDto response = eventService.addEvent(userId, eventRequestDto);
        log.info("Событие добавлено: {}", response);
        return response;
    }

    @GetMapping("/{eventId}")
    public EventFullDto getUsersEvent(@PathVariable Long userId,
                                      @PathVariable Long eventId) {
        log.info("Запрос на получение полной информации о событии с id: {}, добавленном  пользователем с id: {}", eventId, userId);
        EventFullDto response = eventService.getUsersEvent(userId, eventId);
        log.info("Событие получено: {}", response);
        return response;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateUsersEvent(@PathVariable Long userId,
                                          @PathVariable Long eventId,
                                          @RequestBody @Valid EventUpdateDto eventUpdateDto) {
        log.info("Запрос от пользователя с id {} на изменение события с id: {}, {}", userId, eventId, eventUpdateDto);
        EventFullDto response = eventService.updateUsersEvent(userId, eventId, eventUpdateDto);
        log.info("Событие изменено: {}", response);
        return response;
    }

    @GetMapping("/{eventId}/requests")
    public List<RequestResponseDto> getUsersEventsRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        log.info("Запрос на получение списка запросов на участии в событии с id {} текущего пользователя: {}", eventId, userId);
        List<RequestResponseDto> response = eventService.getUsersEventsRequests(userId, eventId);
        log.info("Список получен: {}", response);
        return response;
    }

    @PatchMapping("/{eventId}/requests")
    public RequestStatusUpdateDto updateRequestsStatus(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody @Valid RequestRequestDto requestDto) {
        log.info("Запрос от пользователя с id {} на обновление статусов заявок с id: {}, {} для события с id {}", userId, requestDto.getRequestIds(), requestDto, eventId);
        RequestStatusUpdateDto response = eventService.updateRequestsStatus(userId, eventId, requestDto);
        log.info("Статусы обновлены: {}", response);
        return response;
    }
}
