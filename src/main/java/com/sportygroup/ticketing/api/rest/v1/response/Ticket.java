package com.sportygroup.ticketing.api.rest.v1.response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Ticket(
    UUID ticketId,
    String subject,
    String description,
    TicketStatus status,
    String userId,
    String assigneeId,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<Comment> comments
) {

}
