package ru.practicum.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.HitRequestDto;
import ru.practicum.HitResponseDto;
import ru.practicum.service.StatsServiceImpl;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class StatsController {
    private final StatsServiceImpl statsService;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public HitResponseDto addStat(@RequestBody @Valid HitRequestDto hitRequestDto) {
        log.info("Запрос на добавление статистики: {}", hitRequestDto);
        HitResponseDto hitResponseDto = statsService.addStat(hitRequestDto);
        log.info("Статистика добавлена: {}", hitResponseDto);
        return hitResponseDto;
    }

    @GetMapping("/stats")
    public List<HitResponseDto> getStats(@RequestParam String start,
                                         @RequestParam String end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Запрос на получение списка статистики с " + start + " по " + end);
        List<HitResponseDto> hitResponseDtos = statsService.getStats(start, end, uris, unique);
        log.info("Список статистики получен");
        return hitResponseDtos;
    }
}
