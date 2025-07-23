package com.sportygroup.ticketing.domain.service.tickets.in;

import com.sportygroup.ticketing.domain.model.tickets.Ticket;
import com.sportygroup.ticketing.domain.model.tickets.TicketStatus;
import java.util.List;
import java.util.UUID;

public interface DomainTicketingService {

  /**
   * Creates a new ticket with the given parameters.
   *
   * @param userId      ID of the user creating the ticket
   * @param subject     Subject of the ticket
   * @param description Description of the ticket
   * @return UUID of the created ticket
   */
  UUID createTicket(String userId, String subject, String description);

  /**
   * Returns list of tickets by the given search params.
   *
   * @param status     Ticket status {@link TicketStatus}
   * @param userId     User ID to filter tickets by creator
   * @param assigneeId Assignee ID to filter tickets by assignee
   * @return List of tickets matching the search criteria
   */
  List<Ticket> getTickets(TicketStatus status, String userId, String assigneeId);

  /**
   * Updates the status of a ticket.
   *
   * @param ticketId UUID of the ticket to update
   * @param status   the new status {@link TicketStatus} to set for the ticket
   */
  void updateStatus(UUID ticketId, TicketStatus status);
}
