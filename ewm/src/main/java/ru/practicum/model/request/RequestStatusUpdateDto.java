package ru.practicum.model.request;

import lombok.Data;

import java.util.List;

@Data
public class RequestStatusUpdateDto {
    private List<RequestResponseDto> confirmedRequests;
    private List<RequestResponseDto> rejectedRequests;
}
