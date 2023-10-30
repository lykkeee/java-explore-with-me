package ru.practicum.model.rate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RateRequestDto {
    @Min(value = 1, message = "Rate cannot be less then 1")
    @Max(value = 5, message = "Rate cannot be more then 5")
    private float rate;
}
