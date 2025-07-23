package com.sportygroup.ticketing.domain.service.tickets.out;

import com.sportygroup.ticketing.domain.model.tickets.Ticket;
import com.sportygroup.ticketing.domain.model.tickets.TicketStatus;
import java.util.List;
import java.util.UUID;

public interface TicketingStorage {

  /**
   * Saves a ticket to the storage.
   *
   * @param ticket the ticket to save
   * @return the UUID of the saved ticket
   */
  UUID save(Ticket ticket);

  /**
   * Retrieves a ticket by its ID from the storage.
   *
   * @param ticketId the UUID of the ticket to retrieve
   * @return Ticket object if found, null otherwise
   */
  Ticket findById(UUID ticketId);

  /**
   * Finds tickets by its status, userId, and assigneeId.
   *
   * @param status     Status of the ticket
   * @param userId     Identifier of the user who created the ticket
   * @param assigneeId Identifier of the assignee of the ticket
   * @return list of tickets matching the criteria
   */
  List<Ticket> findByStatusAndUserIdAndAssigneeId(TicketStatus status, String userId, String assigneeId);
}
