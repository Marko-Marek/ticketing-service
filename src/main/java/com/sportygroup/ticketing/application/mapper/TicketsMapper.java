package com.sportygroup.ticketing.application.mapper;

import com.sportygroup.ticketing.api.rest.v1.response.Ticket;
import com.sportygroup.ticketing.api.rest.v1.response.TicketStatus;
import com.sportygroup.ticketing.application.mapper.comments.CommentsMapper;
import java.util.List;
import java.util.Optional;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TicketsMapper {

  public static Ticket toResponse(com.sportygroup.ticketing.domain.model.tickets.Ticket domainTicket) {
    return Ticket.builder()
        .ticketId(domainTicket.getId())
        .description(domainTicket.getDescription())
        .subject(domainTicket.getSubject())
        .createdAt(domainTicket.getCreatedAt())
        .updatedAt(domainTicket.getModifiedAt())
        .status(TicketStatus.valueOf(domainTicket.getStatus().name()))
        .userId(domainTicket.getUserId())
        .assigneeId(domainTicket.getAssigneeId())
        .comments(Optional.ofNullable(domainTicket.getComments()).orElse(List.of()).stream().map(CommentsMapper::toResponse).toList())
        .build();
  }
}
