package ru.practicum.mapper;

import ru.practicum.HitResponseDto;
import ru.practicum.model.Stat;

public class ToHitResponseDtoMapper {
    public static HitResponseDto mapper(Stat stat) {
        return HitResponseDto.builder()
                .app(stat.getApp())
                .uri(stat.getUri())
                .build();
    }
}
