package ru.practicum.service.rate;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.exception.ConflictConditionsNotMetException;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.mapper.RateMapper;
import ru.practicum.model.enums.Status;
import ru.practicum.model.event.Event;
import ru.practicum.model.rate.Rate;
import ru.practicum.model.rate.RateRequestDto;
import ru.practicum.model.rate.RateResponseDto;
import ru.practicum.model.user.User;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.rate.RateRepository;
import ru.practicum.repository.request.RequestRepository;
import ru.practicum.repository.user.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class RateServiceImpl implements RateService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RateRepository rateRepository;
    private final RequestRepository requestRepository;

    @Override
    public RateResponseDto addRate(Long userId, Long eventId, RateRequestDto requestDto) {
        Event event = getEventById(eventId);
        if (event.getEventDate().isBefore(LocalDateTime.now())) {
            throw new ConflictConditionsNotMetException("User cannot rate if event has not started");
        }
        if (requestRepository.findByEventIdAndRequesterIdAndStatus(eventId, userId, Status.CONFIRMED.toString()).isEmpty()) {
            throw new ConflictConditionsNotMetException("User id=" + userId + " cannot rate because he has not attended event id=" + eventId);
        }
        if (!rateRepository.findByUserId(userId).isEmpty()) {
            throw new ConflictConditionsNotMetException("User cannot rate more than 1 time");
        }
        Rate rate = RateMapper.toModel(getUserById(userId), event, requestDto);
        return RateMapper.toResponse(rateRepository.save(rate));
    }

    @Override
    public RateResponseDto changeRate(Long userId, Long rateId, RateRequestDto requestDto) {
        Rate rate = getRateById(rateId);
        checkRateOwner(rate, userId);
        RateMapper.toModel(rate, requestDto);
        return RateMapper.toResponse(rateRepository.save(rate));
    }

    @Override
    public void deleteRate(Long userId, Long rateId) {
        Rate rate = getRateById(rateId);
        checkRateOwner(rate, userId);
        rateRepository.deleteById(rateId);
    }

    @Override
    public Float getRateByEvent(Long eventId) {
        getEventById(eventId);
        return rateRepository.findByEventId(eventId);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User with id=" + userId + " was not found"));
    }

    private Event getEventById(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new DataNotFoundException("Event with id=" + eventId + " was not found"));
    }

    private Rate getRateById(Long rateId) {
        return rateRepository.findById(rateId)
                .orElseThrow(() -> new DataNotFoundException("Rate with id=" + rateId + " was not found"));
    }

    private void checkRateOwner(Rate rate, Long userId) {
        if (!userId.equals(rate.getUser().getId())) {
            throw new ConflictConditionsNotMetException("User id=" + userId + " is not owner of rate id=" + rate.getId());
        }
    }
}
