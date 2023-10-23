package ru.practicum.model.compilation;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class CompilationRequestDto {
    private List<Long> events;
    private Boolean pinned = false;
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
