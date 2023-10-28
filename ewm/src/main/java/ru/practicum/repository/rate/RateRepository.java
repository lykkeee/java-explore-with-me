package ru.practicum.repository.rate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.rate.Rate;

import java.util.List;

public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query("select AVG(r.rate) as rating from Rate as r where r.event.id=:eventId")
    Float findByEventId(Long eventId);

    @Query("select r from Rate as r where r.user.id=:userId")
    List<Rate> findByUserId(Long userId);

}
