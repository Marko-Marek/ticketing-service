package com.sportygroup.ticketing.infrastructure.tickets;

import com.sportygroup.ticketing.domain.model.tickets.Ticket;
import com.sportygroup.ticketing.domain.model.tickets.TicketStatus;
import com.sportygroup.ticketing.domain.service.tickets.out.TicketingStorage;
import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketingStorageImpl implements TicketingStorage {

  private Map<UUID, Ticket> storage;

  @PostConstruct
  public void init() {
    storage = new ConcurrentHashMap<>();
  }

  @Override
  public UUID save(Ticket ticket) {
    return (Objects.nonNull(ticket.getId())) ? updateExisting(ticket) : saveNew(ticket);
  }

  @Override
  public Ticket findById(UUID ticketId) {
    return storage.get(ticketId);
  }

  @Override
  public List<Ticket> findByStatusAndUserIdAndAssigneeId(TicketStatus status, String userId, String assigneeId) {
    return storage.values().stream()
        .filter(ticket ->
            (Objects.isNull(status) || ticket.getStatus().equals(status))
                && (Objects.isNull(userId) || ticket.getUserId().equals(userId))
                && (Objects.isNull(assigneeId) || (Objects.nonNull(ticket.getAssigneeId()) && ticket.getAssigneeId().equals(assigneeId))))
        .toList();
  }

  private UUID saveNew(Ticket ticket) {
    var id = UUID.randomUUID();
    ticket = ticket.withId(id);

    storage.put(id, ticket);

    return id;
  }

  private UUID updateExisting(Ticket ticket) {
    storage.put(ticket.getId(), ticket);

    return ticket.getId();
  }
}
