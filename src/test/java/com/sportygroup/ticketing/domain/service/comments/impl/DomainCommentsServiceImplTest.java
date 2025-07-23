package com.sportygroup.ticketing.domain.service.comments.impl;

import static com.sportygroup.ticketing.domain.service.constants.TicketConstants.OPEN_TICKET;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sportygroup.ticketing.domain.exception.TicketNotFoundException;
import com.sportygroup.ticketing.domain.model.comments.Comment;
import com.sportygroup.ticketing.domain.model.comments.Visibility;
import com.sportygroup.ticketing.domain.service.comments.out.CommentStorage;
import com.sportygroup.ticketing.domain.service.tickets.out.TicketingStorage;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class DomainCommentsServiceImplTest {

  @Mock
  TicketingStorage ticketingStorage;

  @Mock
  CommentStorage commentStorage;

  @InjectMocks
  DomainCommentsServiceImpl service;


  @Test
  void addComment_shouldSaveComment_whenTicketExists() {
    var uuid = OPEN_TICKET.getId();

    when(ticketingStorage.findById(uuid)).thenReturn(OPEN_TICKET);

    service.addComment(uuid, "authorId", "content", Visibility.PUBLIC);

    ArgumentCaptor<Comment> captor = ArgumentCaptor.forClass(Comment.class);
    verify(commentStorage).saveComment(captor.capture());
    Comment saved = captor.getValue();

    assertNotNull(saved);

    assertAll(
        () -> assertEquals(uuid, saved.getTicketId()),
        () -> assertEquals("authorId", saved.getAuthorId()),
        () -> assertEquals("content", saved.getContent()),
        () -> assertEquals(Visibility.PUBLIC, saved.getVisibility()),
        () -> assertNotNull(saved.getCreatedAt())
    );
  }

  @Test
  void addComment_shouldThrowException_whenTicketNotFound() {
    UUID ticketId = UUID.randomUUID();
    when(ticketingStorage.findById(ticketId)).thenReturn(null);

    assertThrows(TicketNotFoundException.class, () ->
        service.addComment(ticketId, "author", "content", Visibility.PUBLIC)
    );
  }
}