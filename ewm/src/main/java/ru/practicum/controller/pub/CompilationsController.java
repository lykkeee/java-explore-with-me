package ru.practicum.controller.pub;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.compilation.CompilationResponseDto;
import ru.practicum.service.compilation.CompilationServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/compilations")
@Slf4j
@AllArgsConstructor
public class CompilationsController {

    private final CompilationServiceImpl compilationService;

    @GetMapping
    public List<CompilationResponseDto> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                        @RequestParam(defaultValue = "0") Integer from,
                                                        @RequestParam(defaultValue = "10") Integer size) {
        log.info("Запрос на получение списка подборок c параметрами pinned: {}, from: {}, size: {}", pinned, from, size);
        List<CompilationResponseDto> response = compilationService.getCompilations(pinned, from, size);
        log.info("Список получен: {}", response);
        return response;
    }

    @GetMapping("/{compId}")
    public CompilationResponseDto getCompilation(@PathVariable Long compId) {
        log.info("Запрос на получение подборки с id: {}", compId);
        CompilationResponseDto response = compilationService.getCompilation(compId);
        log.info("Подборка получена: {}", response);
        return response;
    }
}
