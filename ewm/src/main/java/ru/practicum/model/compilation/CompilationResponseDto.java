package ru.practicum.model.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.event.EventShortDto;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationResponseDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
