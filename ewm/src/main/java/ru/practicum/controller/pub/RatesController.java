package ru.practicum.controller.pub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.rate.RateServiceImpl;

@RestController
@RequestMapping("/rates")
@Slf4j
@AllArgsConstructor
public class RatesController {

    private final RateServiceImpl rateService;

    @GetMapping("/{eventId}")
    public Float getRateByEvent(@PathVariable Long eventId) {
        log.info("Запрос на получение рейтинга события id={}", eventId);
        Float response = rateService.getRateByEvent(eventId);
        log.info("Рейтинг получен: {}", response);
        return response;
    }

}
