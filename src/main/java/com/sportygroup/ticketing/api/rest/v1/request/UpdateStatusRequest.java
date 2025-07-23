package com.sportygroup.ticketing.api.rest.v1.request;

import com.sportygroup.ticketing.api.rest.v1.response.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Validated
@Builder
public record UpdateStatusRequest(
    @NotNull
    TicketStatus status
) {

}