package com.sportygroup.ticketing.domain.service.tickets.impl;

import static com.sportygroup.ticketing.domain.service.constants.CommentConstants.PUBLIC_COMMENT_1;
import static com.sportygroup.ticketing.domain.service.constants.CommentConstants.PUBLIC_COMMENT_2;
import static com.sportygroup.ticketing.domain.service.constants.TicketConstants.CLOSED_TICKET;
import static com.sportygroup.ticketing.domain.service.constants.TicketConstants.IN_PROGRESS_TICKET;
import static com.sportygroup.ticketing.domain.service.constants.TicketConstants.OPEN_TICKET;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sportygroup.ticketing.domain.exception.InvalidStatusTransitionException;
import com.sportygroup.ticketing.domain.exception.TicketNotFoundException;
import com.sportygroup.ticketing.domain.model.comments.Comment;
import com.sportygroup.ticketing.domain.model.comments.Visibility;
import com.sportygroup.ticketing.domain.model.tickets.Ticket;
import com.sportygroup.ticketing.domain.model.tickets.TicketStatus;
import com.sportygroup.ticketing.domain.service.comments.out.CommentStorage;
import com.sportygroup.ticketing.domain.service.tickets.out.TicketingStorage;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DomainTicketingServiceImplTest {

  @Mock
  TicketingStorage ticketingStorage;
  @Mock
  CommentStorage commentStorage;

  @InjectMocks
  DomainTicketingServiceImpl service;

  @Test
  void createTicket_shouldSaveAndReturnId() {
    UUID expectedId = UUID.randomUUID();
    when(ticketingStorage.save(any())).thenReturn(expectedId);

    UUID result = service.createTicket("user1", "subject", "desc");

    assertEquals(expectedId, result);
    verify(ticketingStorage).save(any(Ticket.class));
  }

  @Test
  void updateStatus_shouldThrowIfTicketNotFound() {
    when(ticketingStorage.findById(any())).thenReturn(null);

    UUID ticketId = UUID.randomUUID();
    assertThrows(TicketNotFoundException.class, () -> service.updateStatus(ticketId, TicketStatus.CLOSED));
  }

  @Test
  void updateStatus_shouldThrowIfInvalidTransition() {
    when(ticketingStorage.findById(CLOSED_TICKET.getId())).thenReturn(CLOSED_TICKET);

    assertThrows(InvalidStatusTransitionException.class, () -> service.updateStatus(CLOSED_TICKET.getId(), TicketStatus.OPEN));
  }

  @Test
  void updateStatus_shouldUpdateStatusAndSave() {
    when(ticketingStorage.findById(OPEN_TICKET.getId())).thenReturn(OPEN_TICKET);

    service.updateStatus(OPEN_TICKET.getId(), TicketStatus.IN_PROGRESS);

    ArgumentCaptor<Ticket> captor = ArgumentCaptor.forClass(Ticket.class);
    verify(ticketingStorage).save(captor.capture());
    assertEquals(TicketStatus.IN_PROGRESS, captor.getValue().getStatus());
  }

  @Test
  void getTickets_shouldReturnTicketsWithCommentsForUser() {
    List<Ticket> tickets = List.of(OPEN_TICKET, IN_PROGRESS_TICKET, CLOSED_TICKET);
    when(ticketingStorage.findByStatusAndUserIdAndAssigneeId(any(), any(), any())).thenReturn(tickets);

    Map<UUID, List<Comment>> commentsMap = new HashMap<>();
    commentsMap.put(OPEN_TICKET.getId(), List.of(PUBLIC_COMMENT_1, PUBLIC_COMMENT_2));
    when(commentStorage.getCommentsByTicketIdAndVisibility(any(), any())).thenReturn(commentsMap);

    List<Ticket> result = service.getTickets(TicketStatus.OPEN, "user1", null);

    assertEquals(3, result.size());
    assertEquals(List.of(PUBLIC_COMMENT_1, PUBLIC_COMMENT_2), result.get(0).getComments());
  }

  @Test
  void canTransition_shouldReturnTrueForValidTransitions() {
    assertTrue(invokeCanTransition(TicketStatus.OPEN, TicketStatus.IN_PROGRESS));
    assertTrue(invokeCanTransition(TicketStatus.OPEN, TicketStatus.CLOSED));
    assertTrue(invokeCanTransition(TicketStatus.IN_PROGRESS, TicketStatus.RESOLVED));
    assertTrue(invokeCanTransition(TicketStatus.IN_PROGRESS, TicketStatus.OPEN));
    assertTrue(invokeCanTransition(TicketStatus.RESOLVED, TicketStatus.IN_PROGRESS));
    assertTrue(invokeCanTransition(TicketStatus.RESOLVED, TicketStatus.CLOSED));
  }

  @Test
  void canTransition_shouldReturnFalseForInvalidTransitions() {
    assertFalse(invokeCanTransition(TicketStatus.RESOLVED, TicketStatus.OPEN));
    assertFalse(invokeCanTransition(TicketStatus.CLOSED, TicketStatus.OPEN));
    assertFalse(invokeCanTransition(TicketStatus.CLOSED, TicketStatus.IN_PROGRESS));
    assertFalse(invokeCanTransition(TicketStatus.CLOSED, TicketStatus.RESOLVED));
  }

  @Test
  void getVisibilities_shouldReturnPublicForUser() {
    List<Visibility> result = invokeGetVisibilities("user1");
    assertEquals(List.of(Visibility.PUBLIC), result);
  }

  @Test
  void getVisibilities_shouldReturnPublicAndInternalForAgents() {
    List<Visibility> result = invokeGetVisibilities(null);
    assertEquals(List.of(Visibility.PUBLIC, Visibility.INTERNAL), result);
  }

  // Helper methods to invoke private methods via reflection
  private boolean invokeCanTransition(TicketStatus current, TicketStatus next) {
    try {
      var m = DomainTicketingServiceImpl.class.getDeclaredMethod("canTransition", TicketStatus.class, TicketStatus.class);
      m.setAccessible(true);
      return (boolean) m.invoke(service, current, next);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private List<Visibility> invokeGetVisibilities(String userId) {
    try {
      var m = DomainTicketingServiceImpl.class.getDeclaredMethod("getVisibilities", String.class);
      m.setAccessible(true);
      return (List<Visibility>) m.invoke(service, userId);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}