package com.sportygroup.ticketing.api.rest.v1.response;

import java.util.UUID;
import lombok.Builder;

@Builder
public record CreateTicketResponse(
    UUID ticketId
) {

}
