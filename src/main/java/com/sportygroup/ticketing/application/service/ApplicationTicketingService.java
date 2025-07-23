package com.sportygroup.ticketing.application.service;

import com.sportygroup.ticketing.api.rest.v1.request.AddCommentRequest;
import com.sportygroup.ticketing.api.rest.v1.request.CreateTicketRequest;
import com.sportygroup.ticketing.api.rest.v1.request.UpdateStatusRequest;
import com.sportygroup.ticketing.api.rest.v1.response.CreateTicketResponse;
import com.sportygroup.ticketing.api.rest.v1.response.Ticket;
import com.sportygroup.ticketing.application.mapper.CreateTicketRequestToTicketMapper;
import com.sportygroup.ticketing.application.mapper.TicketsMapper;
import com.sportygroup.ticketing.domain.model.tickets.TicketStatus;
import com.sportygroup.ticketing.domain.model.comments.Visibility;
import com.sportygroup.ticketing.domain.service.comments.in.DomainCommentsService;
import com.sportygroup.ticketing.domain.service.tickets.in.DomainTicketingService;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ApplicationTicketingService {

  private final DomainTicketingService domainTicketingService;
  private final DomainCommentsService domainCommentsService;

  public CreateTicketResponse createTicket(CreateTicketRequest request) {
    var ticketId = domainTicketingService.createTicket(request.userId(), request.subject(), request.description());

    return CreateTicketRequestToTicketMapper.toResponse(ticketId);
  }

  public List<Ticket> getTickets(Optional<String> status, Optional<String> userId, Optional<String> assigneeId) {
    var domainStatus = status.map(TicketStatus::valueOf).orElse(null);

    var tickets = domainTicketingService.getTickets(domainStatus, userId.orElse(null), assigneeId.orElse(null));

    return tickets.stream().map(TicketsMapper::toResponse).toList();
  }

  public void addComment(UUID ticketId, AddCommentRequest request) {
    domainCommentsService.addComment(ticketId, request.authorId(), request.content(), Visibility.valueOf(request.visibility().name()));
  }

  public void updateStatus(UUID ticketId, UpdateStatusRequest request) {
    var status = TicketStatus.valueOf(request.status().name());

    domainTicketingService.updateStatus(ticketId, status);
  }
}
