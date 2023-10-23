package ru.practicum.model.compilation;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CompilationUpdateDto {
    private List<Long> events;
    private Boolean pinned;
    @Length(min = 1, max = 50)
    private String title;
}