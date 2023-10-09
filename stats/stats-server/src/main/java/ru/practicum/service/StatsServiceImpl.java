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

import java.time.LocalDateTime;
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
    public List<HitResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (end.isBefore(start)) {
            throw new IncorrectTimeException("Дата конца периода должна быть позже даты начала периода");
        }
        if (uris == null || uris.isEmpty()) {
            if (unique) {
                return statsRepository.getStatsUnique(start, end);
            } else {
                return statsRepository.getStats(start, end);
            }
        } else {
            if (unique) {
                return statsRepository.getStatsUriUnique(start, end, uris);
            } else {
                return statsRepository.getStatsUri(start, end, uris);
            }
        }
    }
}
