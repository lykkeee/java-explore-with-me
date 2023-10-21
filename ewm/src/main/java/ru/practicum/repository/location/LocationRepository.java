package ru.practicum.repository.location;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.event.Location;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Optional<Location> findByLatAndLon(Float lat, Float lon);
}
