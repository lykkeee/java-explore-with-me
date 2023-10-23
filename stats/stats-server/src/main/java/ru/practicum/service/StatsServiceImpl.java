package ru.practicum.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.HitRequestDto;
import ru.practicum.HitResponseDto;
import ru.practicum.exception.IncorrectTimeException;
import ru.practicum.mapper.ToHitResponseDtoMapper;
import ru.practicum.mapper.ToStatMapper;
import ru.practicum.model.Stat;
import ru.practicum.repository.StatsRepository;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@AllArgsConstructor
public class StatsServiceImpl implements StatsService {
    private final StatsRepository statsRepository;

    @Override
    public HitResponseDto addStat(HitRequestDto hitRequestDto) {
        Stat stat = statsRepository.save(ToStatMapper.mapper(hitRequestDto));
        return ToHitResponseDtoMapper.mapper(stat);
    }

    @Override
    public List<HitResponseDto> getStats(String start, String end, List<String> uris, Boolean unique) {
        LocalDateTime startTime = decodeDateTime(start);
        LocalDateTime endTime = decodeDateTime(end);
        if (endTime.isBefore(startTime)) {
            throw new IncorrectTimeException("Дата конца периода должна быть позже даты начала периода");
        }
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return statsRepository.getStatsUnique(startTime, endTime);
            } else {
                return statsRepository.getStats(startTime, endTime);
            }
        } else {
            if (unique) {
                return statsRepository.getStatsUriUnique(startTime, endTime, uris);
            } else {
                return statsRepository.getStatsUri(startTime, endTime, uris);
            }
        }
    }

    private LocalDateTime decodeDateTime(String dateTime) {
        String strDateTime = URLDecoder.decode(dateTime, StandardCharsets.UTF_8);
        return LocalDateTime.parse(strDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
