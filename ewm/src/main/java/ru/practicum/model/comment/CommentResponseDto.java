package ru.practicum.model.comment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long author;
    private Long event;
    private String text;
    private LocalDateTime created;
    private Boolean edited;
}
