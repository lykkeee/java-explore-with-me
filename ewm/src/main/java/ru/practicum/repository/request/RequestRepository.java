package ru.practicum.repository.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.request.Request;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {

    Optional<Request> findByEventIdAndRequesterId(Long eventId, Long requesterId);

    List<Request> findByEventIdAndStatus(Long eventId, String status);

    List<Request> findByRequesterId(Long requesterId);

    @Query(value = "select r from Request as r where r.event.id = :eventId and r.event.initiator.id = :initiatorId")
    List<Request> findByEventAndInitiator(Long eventId, Long initiatorId);

    @Query(value = "select r from Request as r where r.id in :ids")
    List<Request> findByList(List<Long> ids);

}
