package ru.practicum.mapper;

import ru.practicum.HitRequestDto;
import ru.practicum.StatsClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HitMapper {
    public static void addHit(HttpServletRequest request, StatsClient statsClient, String appName) {
        HitRequestDto requestDto = new HitRequestDto();
        requestDto.setApp(appName);
        requestDto.setUri(request.getRequestURI());
        requestDto.setIp(request.getRemoteAddr());
        requestDto.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        statsClient.addStat(requestDto);
    }
}
