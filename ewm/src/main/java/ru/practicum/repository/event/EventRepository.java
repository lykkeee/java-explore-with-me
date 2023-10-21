package ru.practicum.repository.event;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByInitiatorId(Long initiator, Pageable pageable);

    List<Event> findByCategory(Category category);

    @Query(value = "select e from Event as e where (:users is null or e.initiator.id in :users) and (:states is null or e.state in :states) " +
            "and (:categories is null or e.category.id in :categories) and (coalesce(:rangeStart, null) is null or e.eventDate >= :rangeStart) and " +
            "(coalesce(:rangeEnd, null) is null or e.eventDate <= :rangeEnd)")
    Page<Event> getAdminEvents(List<Long> users, List<String> states, List<Integer> categories, LocalDateTime rangeStart,
                               LocalDateTime rangeEnd, Pageable pageable);

    @Query(value = "select e from Event as e where (:categories is null or e.category.id in :categories) and (:paid is null or e.paid = :paid) " +
            "and (coalesce(:rangeStart, null) is null or e.eventDate >= :rangeStart) " +
            "and (coalesce(:rangeEnd, null) is null or e.eventDate <= :rangeEnd) and (e.state = :state) and " +
            "((:text is null) or (upper(e.annotation) like upper(concat('%', :text, '%')) " +
            " or upper(e.description) like upper(concat('%', :text, '%'))))")
    Page<Event> getEvents(String text, List<Integer> categories, Boolean paid, LocalDateTime rangeStart,
                          LocalDateTime rangeEnd, String state, Pageable pageable);

}
