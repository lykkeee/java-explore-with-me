package ru.practicum.model.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateResponseDto {
    private Long user;
    private Long event;
    private float rate;
}
