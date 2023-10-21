package ru.practicum.model.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class EventRequestDto {
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Integer category;
    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    @NotNull
    private LocationDto location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
}
