package ru.practicum.service.compilation;

import ru.practicum.model.compilation.CompilationRequestDto;
import ru.practicum.model.compilation.CompilationResponseDto;
import ru.practicum.model.compilation.CompilationUpdateDto;

import java.util.List;

public interface CompilationService {

    CompilationResponseDto addCompilation(CompilationRequestDto compilationRequestDto);

    void deleteCompilation(Long compId);

    CompilationResponseDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto);

    List<CompilationResponseDto> getCompilations(Boolean pinned, Integer from, Integer size);

    CompilationResponseDto getCompilation(Long compId);

}
