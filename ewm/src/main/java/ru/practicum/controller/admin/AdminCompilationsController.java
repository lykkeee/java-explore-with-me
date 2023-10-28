package ru.practicum.controller.admin;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.compilation.CompilationRequestDto;
import ru.practicum.model.compilation.CompilationResponseDto;
import ru.practicum.model.compilation.CompilationUpdateDto;
import ru.practicum.service.compilation.CompilationServiceImpl;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
@Slf4j
@AllArgsConstructor
public class AdminCompilationsController {

    private final CompilationServiceImpl compilationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationResponseDto addCompilation(@RequestBody @Valid CompilationRequestDto compilationRequestDto) {
        log.info("Запрос на добавление новой подборки: {}", compilationRequestDto);
        CompilationResponseDto response = compilationService.addCompilation(compilationRequestDto);
        log.info("Подборка добавлена: {}", response);
        return response;
    }

    @DeleteMapping("/{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("Запрос на удаление подборки с id: {}", compId);
        compilationService.deleteCompilation(compId);
        log.info("Подборка удалена");
    }

    @PatchMapping("/{compId}")
    public CompilationResponseDto updateCompilation(@PathVariable Long compId,
                                                    @RequestBody @Valid CompilationUpdateDto compilationUpdateDto) {
        log.info("Запрос на обновление подборки с id: {}, {}", compId, compilationUpdateDto);
        CompilationResponseDto response = compilationService.updateCompilation(compId, compilationUpdateDto);
        log.info("Подборка обновлена: {}", response);
        return response;
    }
}
