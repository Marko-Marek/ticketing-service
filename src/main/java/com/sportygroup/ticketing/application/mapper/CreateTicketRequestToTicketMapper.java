package com.sportygroup.ticketing.application.mapper;

import com.sportygroup.ticketing.api.rest.v1.response.CreateTicketResponse;
import java.util.UUID;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CreateTicketRequestToTicketMapper {

  public static CreateTicketResponse toResponse(UUID ticketId) {
    return CreateTicketResponse.builder()
        .ticketId(ticketId)
        .build();
  }

}
