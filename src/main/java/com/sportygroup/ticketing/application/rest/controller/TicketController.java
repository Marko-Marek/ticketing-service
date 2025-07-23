package com.sportygroup.ticketing.application.rest.controller;

import com.sportygroup.ticketing.api.rest.v1.request.AddCommentRequest;
import com.sportygroup.ticketing.api.rest.v1.request.CreateTicketRequest;
import com.sportygroup.ticketing.api.rest.v1.request.UpdateStatusRequest;
import com.sportygroup.ticketing.api.rest.v1.response.CreateTicketResponse;
import com.sportygroup.ticketing.api.rest.v1.response.Ticket;
import com.sportygroup.ticketing.application.service.ApplicationTicketingService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/tickets")
@AllArgsConstructor
@Slf4j
public class TicketController {

  private final ApplicationTicketingService applicationTicketingService;

  @PostMapping
  public ResponseEntity<CreateTicketResponse> createTickets(@RequestBody @Valid CreateTicketRequest request) {
    log.debug("Creating a new ticket with request: {}", request);

    var result = applicationTicketingService.createTicket(request);

    log.debug("Created a new ticket: {}", result);

    return ResponseEntity.status(HttpStatus.CREATED).body(result);
  }

  @GetMapping
  public ResponseEntity<List<Ticket>> getTickets(@RequestParam Optional<String> status, @RequestParam Optional<String> userId,
      @RequestParam Optional<String> assigneeId) {

    log.debug("Fetching tickets with status: {}, userId: {}, assigneeId: {}", status, userId, assigneeId);

    var result = applicationTicketingService.getTickets(status, userId, assigneeId);

    log.debug("Fetched tickets: {}", result);

    return ResponseEntity.ok(result);
  }

  @PatchMapping(value = "/{ticketId}/status")
  public ResponseEntity<Void> updateStatus(@PathVariable UUID ticketId, @RequestBody @Valid UpdateStatusRequest request) {
    log.debug("Updating ticket {} with request: {}", ticketId, request);

    applicationTicketingService.updateStatus(ticketId, request);

    log.debug("Updated ticket {} with request: {}", ticketId, request);

    return ResponseEntity.ok().build();
  }

  @PostMapping(value = "/{ticketId}/comments")
  public ResponseEntity<Void> addComment(@PathVariable UUID ticketId, @RequestBody @Valid AddCommentRequest request) {
    log.debug("Adding comment to ticket {}: {}", ticketId, request);

    applicationTicketingService.addComment(ticketId, request);

    log.debug("Added comment to ticket {}: {}", ticketId, request);

    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

}
