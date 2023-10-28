package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.event.EventFullDto;
import ru.practicum.model.event.EventUpdateDto;
import ru.practicum.service.event.EventServiceImpl;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/events")
@Slf4j
@AllArgsConstructor
public class AdminEventsController {

    private final EventServiceImpl eventService;

    @GetMapping
    public List<EventFullDto> getAdminEvents(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<String> states,
                                             @RequestParam(required = false) List<Integer> categories,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                             @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка полной информации событий по параметрам userId: {}, states: {}, categoryId: {}, " +
                "rangeStart: {}, rangeEnd: {}, from: {}, size: {}", users, states, categories, rangeStart, rangeEnd, from, size);
        List<EventFullDto> response = eventService.getAdminEvents(users, states, categories,
                rangeStart, rangeEnd, from, size);
        log.info("Список получен: {}", response);
        return response;
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateAdminEvent(@PathVariable Long eventId,
                                         @RequestBody @Valid EventUpdateDto eventUpdateDto) {
        log.info("Запрос на обновление события с id: {}; {}", eventId, eventUpdateDto);
        EventFullDto response = eventService.updateAdminEvent(eventId, eventUpdateDto);
        log.info("Событие обновлено: {}", response);
        return response;
    }
}
