package com.sportygroup.ticketing.api.rest.v1.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Validated
@Builder
public record CreateTicketRequest(
    @NotBlank @Size(min = 5, max = 50)
    String userId,
    @NotBlank @Size(min = 5, max = 150)
    String subject,
    @NotBlank @Size(min = 10, max = 250)
    String description
) {

}
