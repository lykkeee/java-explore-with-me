package ru.practicum.service;

import ru.practicum.HitRequestDto;
import ru.practicum.HitResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    HitResponseDto addStat(HitRequestDto hitRequestDto);

    List<HitResponseDto> getStats(String start, String end, List<String> uris, Boolean unique);

}
