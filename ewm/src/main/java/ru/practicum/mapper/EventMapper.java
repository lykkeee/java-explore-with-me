package ru.practicum.mapper;

import org.modelmapper.ModelMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.CategoryResponseDto;
import ru.practicum.model.enums.State;
import ru.practicum.model.event.*;
import ru.practicum.model.user.User;
import ru.practicum.model.user.UserShortDto;

import java.time.LocalDateTime;

public class EventMapper {
    public static EventFullDto toFull(Event event, Integer confirmedRequests, Long views) {
        ModelMapper mapper = new ModelMapper();
        EventFullDto eventFullDto = mapper.map(event, EventFullDto.class);
        eventFullDto.setCategory(mapper.map(event.getCategory(), CategoryResponseDto.class));
        eventFullDto.setInitiator(mapper.map(event.getInitiator(), UserShortDto.class));
        eventFullDto.setConfirmedRequests(confirmedRequests);
        eventFullDto.setViews(views);
        eventFullDto.setLocation(mapper.map(event.getLocation(), LocationDto.class));
        return eventFullDto;
    }

    public static Event toModel(User user, EventRequestDto eventRequestDto, Category category, Location location) {
        ModelMapper mapper = new ModelMapper();
        Event event = mapper.map(eventRequestDto, Event.class);
        event.setCategory(category);
        event.setCreatedOn(LocalDateTime.now());
        event.setInitiator(user);
        event.setLocation(location);
        event.setState(State.PENDING.toString());
        return event;
    }

    public static EventShortDto toShort(Event event, Integer confirmedRequests, Long views) {
        ModelMapper mapper = new ModelMapper();
        EventShortDto eventShortDto = mapper.map(event, EventShortDto.class);
        eventShortDto.setCategory(mapper.map(event.getCategory(), CategoryResponseDto.class));
        eventShortDto.setInitiator(mapper.map(event.getInitiator(), UserShortDto.class));
        eventShortDto.setViews(views);
        eventShortDto.setConfirmedRequests(confirmedRequests);
        return eventShortDto;
    }

    public static Event toModel(EventUpdateDto eventUpdateDto, Category category, Event event, Location location) {
        if (eventUpdateDto.getAnnotation() != null) {
            event.setAnnotation(eventUpdateDto.getAnnotation());
        }
        if (category != null) {
            event.setCategory(category);
        }
        if (eventUpdateDto.getDescription() != null) {
            event.setDescription(eventUpdateDto.getDescription());
        }
        if (eventUpdateDto.getLocation() != null) {
            event.setLocation(location);
        }
        if (eventUpdateDto.getPaid() != null) {
            event.setPaid(eventUpdateDto.getPaid());
        }
        if (eventUpdateDto.getParticipantLimit() != null) {
            event.setParticipantLimit(eventUpdateDto.getParticipantLimit());
        }
        if (eventUpdateDto.getRequestModeration() != null) {
            event.setRequestModeration(eventUpdateDto.getRequestModeration());
        }
        switch (eventUpdateDto.getStateAction()) {
            case "REJECT_EVENT":
            case "CANCEL_REVIEW":
                event.setState(State.CANCELED.toString());
                break;
            case "PUBLISH_EVENT":
                event.setPublishedOn(LocalDateTime.now());
                event.setState(State.PUBLISHED.toString());
                break;
            case "SEND_TO_REVIEW":
                event.setState(State.PENDING.toString());
                break;
        }
        if (eventUpdateDto.getTitle() != null) {
            event.setTitle(eventUpdateDto.getTitle());
        }
        if (eventUpdateDto.getEventDate() != null) {
            event.setEventDate(eventUpdateDto.getEventDate());
        }
        return event;
    }
}
