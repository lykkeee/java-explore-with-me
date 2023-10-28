package ru.practicum.service.rate;

import ru.practicum.model.rate.RateRequestDto;
import ru.practicum.model.rate.RateResponseDto;

public interface RateService {

    RateResponseDto addRate(Long userId, Long eventId, RateRequestDto requestDto);

    RateResponseDto changeRate(Long userId, Long rateId, RateRequestDto requestDto);

    void deleteRate(Long userId, Long rateId);

    Float getRateByEvent(Long eventId);

}
