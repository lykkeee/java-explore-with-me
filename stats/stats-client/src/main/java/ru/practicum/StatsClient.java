package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class StatsClient {
    private final RestTemplate restTemplate;
    @Value("${stats-client.url}")
    private final String url;

    @Autowired
    public StatsClient() {
        this.restTemplate = new RestTemplate();
        this.url = "";
    }

    public void addStat(HitRequestDto hitRequestDto) {
        restTemplate.postForObject(url + "/hit", hitRequestDto, HitResponseDto.class);
    }

    public List<HitResponseDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        ResponseEntity<HitResponseDto[]> responseEntity = restTemplate.getForEntity(url + "/stats?start=" + encodeDateTime(start) +
                        "&end=" + encodeDateTime(end) + "&unique=" + unique + "&uris=" + uris.get(0),
                HitResponseDto[].class);
        return Arrays.asList(Objects.requireNonNull(responseEntity.getBody()));
    }

    private String encodeDateTime(LocalDateTime dateTime) {
        return URLEncoder.encode(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")), StandardCharsets.UTF_8);
    }
}
