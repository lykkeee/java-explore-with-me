package ru.practicum.controller.priv;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.rate.RateRequestDto;
import ru.practicum.model.rate.RateResponseDto;
import ru.practicum.service.rate.RateServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/rates")
@Slf4j
@AllArgsConstructor
public class UserRatesController {

    private final RateServiceImpl rateService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RateResponseDto addRate(@PathVariable Long userId,
                                   @RequestParam Long eventId,
                                   @RequestBody @Valid RateRequestDto requestDto) {
        log.info("Запрос на добавление оценки от пользователя id={} к событию id={}, {}",userId, eventId, requestDto);
        RateResponseDto response = rateService.addRate(userId, eventId, requestDto);
        log.info("Оценка добавлена: {}", response);
        return response;
    }

    @PatchMapping
    public RateResponseDto changeRate(@PathVariable Long userId,
                                      @RequestParam Long rateId,
                                      @RequestBody @Valid RateRequestDto requestDto) {
        log.info("Запрос на обновление оценки от пользователя id={} к событию id={}, {}",userId, rateId, requestDto);
        RateResponseDto response = rateService.changeRate(userId, rateId, requestDto);
        log.info("Оценка обновлена: {}", response);
        return response;
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRate(@PathVariable Long userId,
                           @RequestParam Long rateId) {
        log.info("Запрос на удаление оценки от пользователя id={} к событию id={}", userId, rateId);
        rateService.deleteRate(userId, rateId);
        log.info("Оценка удалена");
    }

}
