package ru.practicum.mapper;

import ru.practicum.model.enums.Status;
import ru.practicum.model.event.Event;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestResponseDto;
import ru.practicum.model.user.User;

import java.time.LocalDateTime;

public class RequestMapper {
    public static Request toModel(User requester, Event event) {
        Request request = new Request();
        request.setRequester(requester);
        request.setEvent(event);
        request.setCreated(LocalDateTime.now());
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            request.setStatus(Status.CONFIRMED.toString());
        } else {
            request.setStatus(Status.PENDING.toString());
        }
        return request;
    }

    public static RequestResponseDto toResponse(Request request) {
        RequestResponseDto responseDto = new RequestResponseDto();
        responseDto.setId(request.getId());
        responseDto.setEvent(request.getEvent().getId());
        responseDto.setRequester(request.getRequester().getId());
        responseDto.setCreated(request.getCreated());
        responseDto.setStatus(request.getStatus());
        return responseDto;
    }
}
