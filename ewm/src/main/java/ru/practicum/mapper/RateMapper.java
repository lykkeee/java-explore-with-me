package ru.practicum.mapper;

import ru.practicum.model.event.Event;
import ru.practicum.model.rate.Rate;
import ru.practicum.model.rate.RateRequestDto;
import ru.practicum.model.rate.RateResponseDto;
import ru.practicum.model.user.User;

public class RateMapper {

    public static Rate toModel(User user, Event event, RateRequestDto rateRequestDto) {
        Rate rate = new Rate();
        rate.setUser(user);
        rate.setEvent(event);
        rate.setRate(rateRequestDto.getRate());
        return rate;
    }

    public static Rate toModel(Rate rate, RateRequestDto rateRequestDto) {
        rate.setRate(rateRequestDto.getRate());
        return rate;
    }

    public static RateResponseDto toResponse(Rate rate) {
        RateResponseDto rateResponseDto = new RateResponseDto();
        rateResponseDto.setUser(rate.getUser().getId());
        rateResponseDto.setEvent(rate.getEvent().getId());
        rateResponseDto.setRate(rate.getRate());
        return rateResponseDto;
    }
}
