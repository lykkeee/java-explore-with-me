package ru.practicum.service.event;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.HitResponseDto;
import ru.practicum.StatsClient;
import ru.practicum.exception.*;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.HitMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.category.Category;
import ru.practicum.model.enums.EventSort;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.Status;
import ru.practicum.model.event.*;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.RequestRequestDto;
import ru.practicum.model.request.RequestResponseDto;
import ru.practicum.model.request.RequestStatusUpdateDto;
import ru.practicum.model.user.User;
import ru.practicum.repository.category.CategoryRepository;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.location.LocationRepository;
import ru.practicum.repository.request.RequestRepository;
import ru.practicum.repository.user.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventServiceImpl implements EventService {
    private static final String APP_NAME = "ewm";
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final LocationRepository locationRepository;
    private final StatsClient statsClient;

    private final ModelMapper mapper;

    @Override
    public List<EventShortDto> getUsersEvents(Long userId, Integer from, Integer size) {
        getUserById(userId);
        List<Event> response = eventRepository.findByInitiatorId(userId, PageRequest.of(from / size, size)).toList();
        return response.stream().map(event -> (EventMapper.toShort(event, getRequestsByEventAndStatus(event.getId(),
                Status.CONFIRMED).size(), getViews(event.getId())))).collect(Collectors.toList());
    }

    @Override
    public EventFullDto addEvent(Long userId, EventRequestDto eventRequestDto) {
        isEventDateCorrect(eventRequestDto.getEventDate());
        Event event = eventRepository.save(EventMapper.toModel(getUserById(userId), eventRequestDto,
                getCategoryById(eventRequestDto.getCategory()), addLocation(eventRequestDto.getLocation())));
        return EventMapper.toFull(event, getRequestsByEventAndStatus(event.getId(), Status.CONFIRMED).size(), getViews(event.getId()));
    }

    @Override
    public EventFullDto getUsersEvent(Long userId, Long eventId) {
        getUserById(userId);
        Event event = getEventById(eventId);
        isUserOwner(event, userId);
        return EventMapper.toFull(event, getRequestsByEventAndStatus(event.getId(), Status.CONFIRMED).size(), getViews(eventId));
    }

    @Override
    public EventFullDto updateUsersEvent(Long userId, Long eventId, EventUpdateDto eventUpdateDto) {
        Event event = getEventById(eventId);
        if (event.getState().equals(State.PUBLISHED.toString())) {
            throw new ConflictConditionsNotMetException("Only pending or canceled events can be changed");
        }
        if (eventUpdateDto.getEventDate() != null) {
            isEventDateCorrect(eventUpdateDto.getEventDate());
        }
        Category category = null;
        if (eventUpdateDto.getCategory() != null) {
            category = getCategoryById(eventUpdateDto.getCategory());
        }
        Location location = null;
        if (eventUpdateDto.getLocation() != null) {
            location = addLocation(eventUpdateDto.getLocation());
        }
        event = eventRepository.save(EventMapper.toModel(eventUpdateDto, category, event, location));
        return EventMapper.toFull(event, getRequestsByEventAndStatus(event.getId(), Status.CONFIRMED).size(), getViews(eventId));
    }

    @Override
    public List<EventFullDto> getAdminEvents(List<Long> users, List<String> states, List<Integer> categories,
                                             LocalDateTime rangeStart, LocalDateTime rangeEnd, Integer from, Integer size) {
        if (rangeEnd != null && rangeStart != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidateException("Time");
            }
        }
        List<Event> response = eventRepository.getAdminEvents(users, states, categories, rangeStart, rangeEnd, PageRequest.of(from / size, size)).toList();
        return response.stream()
                .map(event -> (EventMapper.toFull(event, getRequestsByEventAndStatus(event.getId(),
                        Status.CONFIRMED).size(), getViews(event.getId())))).collect(Collectors.toList());
    }

    @Override
    public EventFullDto updateAdminEvent(Long eventId, EventUpdateDto eventUpdateDto) {
        Event event = getEventById(eventId);
        if (eventUpdateDto.getStateAction().equals("REJECT_EVENT") && event.getState().equals(State.PUBLISHED.toString())) {
            throw new ConflictConditionsNotMetException("Cannot publish the event because it's not in the right state: " + event.getState());
        }
        if (eventUpdateDto.getStateAction().equals("PUBLISH_EVENT") && !event.getState().equals(State.PENDING.toString())) {
            throw new ConflictConditionsNotMetException("Cannot publish the event because it's not in the right state: " + event.getState());
        }
        if (eventUpdateDto.getEventDate() != null) {
            isEventDateCorrect(eventUpdateDto.getEventDate());
        }
        Category category = null;
        if (eventUpdateDto.getCategory() != null) {
            category = getCategoryById(eventUpdateDto.getCategory());
        }
        Location location = null;
        if (eventUpdateDto.getLocation() != null) {
            location = addLocation(eventUpdateDto.getLocation());
        }
        event = eventRepository.save(EventMapper.toModel(eventUpdateDto, category, event, location));
        return EventMapper.toFull(event, getRequestsByEventAndStatus(eventId, Status.CONFIRMED).size(), getViews(eventId));
    }

    @Override
    public List<EventShortDto> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,

                                         LocalDateTime rangeEnd, Boolean onlyAvailable, String sort, Integer from, Integer size, HttpServletRequest request) {
        if (rangeEnd != null && rangeStart != null) {
            if (rangeEnd.isBefore(rangeStart)) {
                throw new ValidateException("Time");
            }
        }
        List<Event> events = eventRepository.getEvents(text, categories, paid, rangeStart, rangeEnd, State.PUBLISHED.toString(), PageRequest.of(from / size, size)).toList();
        if (onlyAvailable) {
            List<Event> onlyAvailableEvents = new ArrayList<>();
            for (Event event : events) {
                if (event.getParticipantLimit() > 0) {
                    onlyAvailableEvents.add(event);
                }
            }
            events = onlyAvailableEvents;
        }
        List<EventShortDto> response = events.stream().map(event -> (EventMapper.toShort(event, getRequestsByEventAndStatus(event.getId(),
                Status.CONFIRMED).size(), getViews(event.getId())))).collect(Collectors.toList());
        if (sort != null) {
            if (sort.equals(EventSort.EVENT_DATE.toString())) {
                response.sort(Comparator.comparing(EventShortDto::getEventDate));
            }
            if (sort.equals(EventSort.VIEWS.toString())) {
                response.sort(Comparator.comparing(EventShortDto::getViews));
            }
        }
        HitMapper.addHit(request, statsClient, APP_NAME);
        return response;
    }

    @Override
    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        Event event = getEventById(eventId);
        if (!event.getState().equals(State.PUBLISHED.toString())) {
            throw new DataNotFoundException("Event with id=" + eventId + " was not found");
        }
        HitMapper.addHit(request, statsClient, APP_NAME);
        return EventMapper.toFull(event, getRequestsByEventAndStatus(eventId, Status.CONFIRMED).size(), getViews(eventId));
    }

    @Override
    public List<RequestResponseDto> getUsersEventsRequests(Long userId, Long eventId) {
        getUserById(userId);
        Event event = getEventById(eventId);
        isUserOwner(event, userId);
        List<Request> response = requestRepository.findByEventAndInitiator(eventId, userId);
        return response.stream().map(RequestMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public RequestStatusUpdateDto updateRequestsStatus(Long userId, Long eventId, RequestRequestDto requestRequestDto) {
        Event event = getEventById(eventId);
        isUserOwner(event, userId);
        RequestStatusUpdateDto requestStatusUpdateDto = new RequestStatusUpdateDto();
        if (!event.getRequestModeration() || event.getParticipantLimit() == 0) {
            return requestStatusUpdateDto;
        }
        List<RequestResponseDto> confirmed = new ArrayList<>();
        List<RequestResponseDto> rejected = new ArrayList<>();
        List<Request> requests = requestRepository.findByList(requestRequestDto.getRequestIds());
        if (requestRequestDto.getStatus().equals(Status.CONFIRMED.toString())) {
            if (event.getParticipantLimit() == (getRequestsByEventAndStatus(eventId, Status.CONFIRMED).size())) {
                throw new ConflictConditionsNotMetException("The participant limit has been reached");
            }
            for (Request request : requests) {
                if (event.getParticipantLimit().equals(getRequestsByEventAndStatus(eventId, Status.CONFIRMED).size())) {
                    if (request.getStatus().equals(Status.PENDING.toString())) {
                        request.setStatus(Status.REJECTED.toString());
                        requestRepository.save(request);
                        rejected.add(RequestMapper.toResponse(request));
                    }
                } else if (request.getStatus().equals(Status.PENDING.toString())) {
                    request.setStatus(Status.CONFIRMED.toString());
                    requestRepository.save(request);
                    confirmed.add(RequestMapper.toResponse(request));
                }
            }
        } else if (requestRequestDto.getStatus().equals(Status.REJECTED.toString())) {
            for (Request request : requests) {
                if (request.getStatus().equals(Status.CONFIRMED.toString())) {
                    throw new ConflictConditionsNotMetException("The request status has been confirmed");
                }
                if (request.getStatus().equals(Status.PENDING.toString())) {
                    request.setStatus(Status.REJECTED.toString());
                    requestRepository.save(request);
                    rejected.add(RequestMapper.toResponse(request));
                }
            }
        }
        requestStatusUpdateDto.setConfirmedRequests(confirmed);
        requestStatusUpdateDto.setRejectedRequests(rejected);
        return requestStatusUpdateDto;
    }

    @Override
    public List<RequestResponseDto> getUsersRequests(Long userId) {
        getUserById(userId);
        List<Request> requests = requestRepository.findByRequesterId(userId);
        return requests.stream().map(RequestMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public RequestResponseDto addRequest(Long userId, Long eventId) {
        User user = getUserById(userId);
        Event event = getEventById(eventId);
        if (requestRepository.findByEventIdAndRequesterId(eventId, userId).orElse(null) != null) {
            throw new ConflictConditionsNotMetException("Request from user id=" + userId + " to event id=" + eventId + " has been created");
        }
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictConditionsNotMetException("User id= " + userId + " is initiator and does not add request to event");
        }
        if (!event.getState().equals(State.PUBLISHED.toString())) {
            throw new ConflictConditionsNotMetException("Event id=" + eventId + " is not published");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit() == getRequestsByEventAndStatus(eventId, Status.CONFIRMED).size()) {
            throw new ConflictConditionsNotMetException("Event id=" + eventId + " has full participant limit");
        }
        Request request = requestRepository.save(RequestMapper.toModel(user, event));
        return RequestMapper.toResponse(request);
    }

    @Override
    public RequestResponseDto cancelRequest(Long userId, Long requestId) {
        User user = getUserById(userId);
        Request request = getRequestById(requestId);
        if (!request.getRequester().equals(user)) {
            throw new ConflictConditionsNotMetException("User id=" + userId + " is not requests owner id=" + requestId);
        }
        request.setStatus(Status.CANCELED.toString());
        return RequestMapper.toResponse(requestRepository.save(request));
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id=" + userId + " was not found"));
    }

    private Category getCategoryById(Integer catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new DataNotFoundException("Category with id=" + catId + " was not found"));
    }

    private void isEventDateCorrect(LocalDateTime eventDate) {
        if (eventDate.isBefore(LocalDateTime.now().plusHours(2L))) {
            throw new ValidateException("Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + eventDate);
        }
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event with id=" + eventId + " was not found"));
    }

    private Location addLocation(LocationDto location) {
        return locationRepository.findByLatAndLon(location.getLat(), location.getLon())
                .orElse(locationRepository.save(mapper.map(location, Location.class)));
    }

    private void isUserOwner(Event event, Long userId) {
        if (!event.getInitiator().getId().equals(userId)) {
            throw new ConflictConditionsNotMetException("User id=" + userId + " is not events owner");
        }
    }

    private Request getRequestById(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new DataNotFoundException("Request with id" + requestId + " was not found"));
    }

    private List<Request> getRequestsByEventAndStatus(Long eventId, Status status) {
        return requestRepository.findByEventIdAndStatus(eventId, status.toString());
    }

    private Long getViews(Long eventId) {
        List<String> uris = Collections.singletonList("/events/" + eventId);
        List<HitResponseDto> response = statsClient.getStats(
                LocalDateTime.of(2020, 1, 1, 1, 1, 1), LocalDateTime.now().plusMinutes(1), uris, true);
        Long views = 0L;
        for (HitResponseDto stat : response) {
            if (stat.getApp().equals(APP_NAME)) {
                views += stat.getHits();
            }
        }
        return views;
    }

}
