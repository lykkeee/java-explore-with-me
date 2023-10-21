package ru.practicum.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
public class RequestRequestDto {
    private List<Long> requestIds;
    @NotBlank
    private String status;
}
