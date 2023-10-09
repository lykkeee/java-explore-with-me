package ru.practicum.mapper;

import ru.practicum.HitRequestDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ToStatMapper {
    public static Stat mapper(HitRequestDto hitRequestDto) {
        return Stat.builder()
                .app(hitRequestDto.getApp())
                .uri(hitRequestDto.getUri())
                .ip(hitRequestDto.getIp())
                .timestamp(LocalDateTime.parse(hitRequestDto.getTimestamp(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .build();
    }
}
