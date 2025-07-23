package com.sportygroup.ticketing.domain.service.constants;

import com.sportygroup.ticketing.domain.model.tickets.Ticket;
import com.sportygroup.ticketing.domain.model.tickets.TicketStatus;
import java.util.UUID;

public class TicketConstants {

  public static final Ticket OPEN_TICKET = Ticket.builder()
      .id(UUID.randomUUID())
      .status(TicketStatus.OPEN)
      .subject("subject")
      .description("description")
      .userId("user")
      .build();

  public static final Ticket IN_PROGRESS_TICKET = Ticket.builder()
      .id(UUID.randomUUID())
      .status(TicketStatus.IN_PROGRESS)
      .subject("subject")
      .description("description")
      .userId("user")
      .build();

  public static final Ticket CLOSED_TICKET = Ticket.builder()
      .id(UUID.randomUUID())
      .status(TicketStatus.CLOSED)
      .subject("subject")
      .description("description")
      .userId("user")
      .build();

}
