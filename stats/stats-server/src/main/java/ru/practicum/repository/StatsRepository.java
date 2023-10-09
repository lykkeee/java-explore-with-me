package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.HitResponseDto;
import ru.practicum.model.Stat;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Stat, Long> {
    @Query(value = "select new ru.practicum.HitResponseDto(s.app, s.uri, count(s.ip)) from Stat as s " +
            "where s.uri in :uris and s.timestamp between :start and :end " +
            "group by s.app, s.uri order by count(s.ip) desc")
    List<HitResponseDto> getStatsUri(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.HitResponseDto(s.app, s.uri, count(distinct s.ip)) from Stat as s " +
            "where s.uri in :uris and s.timestamp between :start and :end " +
            "group by s.app, s.uri order by count(distinct s.ip) desc")
    List<HitResponseDto> getStatsUriUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(value = "select new ru.practicum.HitResponseDto(s.app, s.uri, count(s.ip)) from Stat as s " +
            "where s.timestamp between :start and :end " +
            "group by s.app, s.uri order by count(s.ip) desc")
    List<HitResponseDto> getStats(LocalDateTime start, LocalDateTime end);

    @Query(value = "select new ru.practicum.HitResponseDto(s.app, s.uri, count(distinct s.ip)) from Stat as s " +
            "where s.uri in :uris and s.timestamp between :start and :end " +
            "group by s.app, s.uri order by count(distinct s.ip) desc")
    List<HitResponseDto> getStatsUnique(LocalDateTime start, LocalDateTime end);
}
