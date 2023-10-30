package ru.practicum.service.event;

import ru.practicum.model.event.*;
import ru.practicum.model.request.RequestRequestDto;
import ru.practicum.model.request.RequestResponseDto;
import ru.practicum.model.request.RequestStatusUpdateDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {

    List<EventShortDto> getUsersEvents(Long userId, Integer from, Integer size);

    EventFullDto addEvent(Long userId, EventRequestDto eventRequestDto);

    EventFullDto getUsersEvent(Long userId, Long eventId);

    EventFullDto updateUsersEvent(Long userId, Long eventId, EventUpdateDto eventUpdateDto);

    List<EventFullDto> getAdminEvents(List<Long> users, List<String> states, List<Integer> categories,
                                      LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size);

    EventFullDto updateAdminEvent(Long eventId, EventUpdateDto eventUpdateDto);

    List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request);

    EventFullDto getEvent(Long eventId, HttpServletRequest request);

    List<RequestResponseDto> getUsersEventsRequests(Long userId, Long eventId);

    RequestStatusUpdateDto updateRequestsStatus(Long userId, Long eventId, RequestRequestDto requestRequestDto);

    List<RequestResponseDto> getUsersRequests(Long userId);

    RequestResponseDto addRequest(Long userId, Long eventId);

    RequestResponseDto cancelRequest(Long userId, Long requestId);

}
