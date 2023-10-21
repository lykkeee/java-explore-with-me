package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.CompilationRequestDto;
import ru.practicum.model.compilation.CompilationResponseDto;
import ru.practicum.model.compilation.CompilationUpdateDto;
import ru.practicum.model.event.Event;
import ru.practicum.repository.event.EventRepository;

import java.util.HashSet;
import java.util.List;

@AllArgsConstructor
public class CompilationMapper {

    public static Compilation toModel(CompilationRequestDto compilationRequestDto, EventRepository eventRepository) {
        Compilation compilation = new Compilation();
        if (compilationRequestDto.getEvents() != null) {
            List<Event> events = eventRepository.findAllById(compilationRequestDto.getEvents());
            compilation.setEvents(new HashSet<>(events));
        } else {
            compilation.setEvents(new HashSet<>());
        }
        compilation.setPinned(compilationRequestDto.getPinned());
        compilation.setTitle(compilationRequestDto.getTitle());
        return compilation;
    }

    public static Compilation toModel(CompilationUpdateDto compilationUpdateDto, EventRepository eventRepository, Compilation compilation) {
        if (compilationUpdateDto.getEvents() != null) {
            if (!compilationUpdateDto.getEvents().isEmpty()) {
                List<Event> events = eventRepository.findAllById(compilationUpdateDto.getEvents());
                compilation.setEvents(new HashSet<>(events));
            }
        }
        if (compilationUpdateDto.getPinned() != null) {
            compilation.setPinned(compilationUpdateDto.getPinned());
        }
        if (compilationUpdateDto.getTitle() != null) {
            compilation.setTitle(compilationUpdateDto.getTitle());
        }
        return compilation;
    }

    public static CompilationResponseDto toResponse(Compilation compilation) {
        CompilationResponseDto compilationResponseDto = new CompilationResponseDto();
        compilationResponseDto.setId(compilation.getId());
        compilationResponseDto.setPinned(compilation.getPinned());
        compilationResponseDto.setTitle(compilation.getTitle());
        return compilationResponseDto;
    }

}
