package ru.practicum.service.compilation;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.HitResponseDto;
import ru.practicum.StatsClient;
import ru.practicum.exception.DataNotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.CompilationRequestDto;
import ru.practicum.model.compilation.CompilationResponseDto;
import ru.practicum.model.compilation.CompilationUpdateDto;
import ru.practicum.model.enums.Status;
import ru.practicum.model.event.EventShortDto;
import ru.practicum.repository.compilation.CompilationRepository;
import ru.practicum.repository.event.EventRepository;
import ru.practicum.repository.request.RequestRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationServiceImpl implements CompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final RequestRepository requestRepository;
    private final StatsClient statsClient;

    @Override
    public CompilationResponseDto addCompilation(CompilationRequestDto compilationRequestDto) {
        Compilation compilation = CompilationMapper.toModel(compilationRequestDto, eventRepository);
        CompilationResponseDto response = CompilationMapper.toResponse(compilationRepository.save(compilation));
        response.setEvents(setEvents(compilation));
        return  response;
    }

    @Override
    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Compilation with id=" + compId + " was not found"));
        compilationRepository.deleteById(compId);
    }

    @Override
    public CompilationResponseDto updateCompilation(Long compId, CompilationUpdateDto compilationUpdateDto) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Compilation with id=" + compId + " was not found"));
        CompilationMapper.toModel(compilationUpdateDto, eventRepository, compilation);
        CompilationResponseDto response = CompilationMapper.toResponse(compilationRepository.save(compilation));
        response.setEvents(setEvents(compilation));
        return  response;
    }

    @Override
    public List<CompilationResponseDto> getCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from/ size, size);
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll(pageable).toList();
        } else {
            compilations = compilationRepository.findByPinned(pinned, pageable).toList();
        }
        List<CompilationResponseDto> response = new ArrayList<>();
        for (Compilation compilation : compilations) {
            CompilationResponseDto compilationResponseDto = CompilationMapper.toResponse(compilation);
            compilationResponseDto.setEvents(setEvents(compilation));
            response.add(compilationResponseDto);
        }
        return response;
    }

    @Override
    public CompilationResponseDto getCompilation(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> new DataNotFoundException("Compilation with id=" + compId + " was not found"));
        CompilationResponseDto response = CompilationMapper.toResponse(compilation);
        response.setEvents(setEvents(compilation));
        return response;
    }

    private List<EventShortDto> setEvents(Compilation compilation) {
        return compilation.getEvents().stream().map(event -> EventMapper
                        .toShort(event, requestRepository.findByEventIdAndStatus(event.getId(), Status.CONFIRMED.toString()).size(),
                                getViews(event.getId()))).collect(Collectors.toList());
    }

    private Long getViews(Long eventId) {
        List<String> uris = Collections.singletonList("/events/" + eventId);
        List<HitResponseDto> response = statsClient.getStats(
                LocalDateTime.of(2020, 1, 1, 1, 1, 1), LocalDateTime.now().plusMinutes(1), uris, true);
        Long views = 0L;
        for (HitResponseDto stat : response) {
            if (stat.getApp().equals("ewm")) {
                views += stat.getHits();
            }
        }
        return views;
    }
}
