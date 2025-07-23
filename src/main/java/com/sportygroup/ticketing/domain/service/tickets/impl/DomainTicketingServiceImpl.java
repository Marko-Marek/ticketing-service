package com.sportygroup.ticketing.domain.service.tickets.impl;

import static com.sportygroup.ticketing.domain.model.tickets.TicketStatus.CLOSED;
import static com.sportygroup.ticketing.domain.model.tickets.TicketStatus.IN_PROGRESS;
import static com.sportygroup.ticketing.domain.model.tickets.TicketStatus.OPEN;
import static com.sportygroup.ticketing.domain.model.tickets.TicketStatus.RESOLVED;

import com.sportygroup.ticketing.domain.exception.InvalidStatusTransitionException;
import com.sportygroup.ticketing.domain.exception.TicketNotFoundException;
import com.sportygroup.ticketing.domain.model.comments.Visibility;
import com.sportygroup.ticketing.domain.model.tickets.Ticket;
import com.sportygroup.ticketing.domain.model.tickets.TicketStatus;
import com.sportygroup.ticketing.domain.service.comments.out.CommentStorage;
import com.sportygroup.ticketing.domain.service.tickets.in.DomainTicketingService;
import com.sportygroup.ticketing.domain.service.tickets.out.TicketingStorage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class DomainTicketingServiceImpl implements DomainTicketingService {

  private final TicketingStorage ticketingStorage;
  private final CommentStorage commentStorage;

  @Override
  public UUID createTicket(String userId, String subject, String description) {
    var ticket = createNewTicket(userId, subject, description);

    return ticketingStorage.save(ticket);
  }

  @Override
  public void updateStatus(UUID ticketId, TicketStatus status) {
    var ticket = Optional.ofNullable(ticketingStorage.findById(ticketId)).orElseThrow(() -> {
      log.error("Ticket with ID {} not found", ticketId);
      return new TicketNotFoundException(String.format("Ticket with ID %s not found", ticketId));
    });

    if (!canTransition(ticket.getStatus(), status)) {
      log.error("Cannot transition ticket from {} to {}", ticket.getStatus(), status);
      throw new InvalidStatusTransitionException(String.format("Cannot transition ticket from %s to %s", ticket.getStatus(), status));
    }

    ticket = ticket.withStatus(status).withModifiedAt(LocalDateTime.now());

    ticketingStorage.save(ticket);
  }

  @Override
  public List<Ticket> getTickets(TicketStatus status, String userId, String assigneeId) {
    var tickets = ticketingStorage.findByStatusAndUserIdAndAssigneeId(status, userId, assigneeId);

    var visibilities = getVisibilities(userId);
    var comments = commentStorage.getCommentsByTicketIdAndVisibility(tickets.stream().map(Ticket::getId).toList(), visibilities);

    tickets.forEach(x -> x.setComments(comments.getOrDefault(x.getId(), List.of())));

    return tickets;
  }

  private Ticket createNewTicket(String userId, String subject, String description) {
    return Ticket.builder().userId(userId).subject(subject).description(description).status(OPEN).createdAt(LocalDateTime.now()).build();
  }

  private boolean canTransition(TicketStatus currentStatus, TicketStatus newStatus) {
    return switch (currentStatus) {
      case OPEN -> newStatus == TicketStatus.IN_PROGRESS || newStatus == CLOSED;
      case IN_PROGRESS -> newStatus == RESOLVED || newStatus == OPEN;
      case RESOLVED -> newStatus == CLOSED || newStatus == IN_PROGRESS;
      case CLOSED -> false;
    };
  }

  private List<Visibility> getVisibilities(String userId) {
    return Objects.nonNull(userId) ? List.of(Visibility.PUBLIC) : List.of(Visibility.PUBLIC, Visibility.INTERNAL);
  }
}
