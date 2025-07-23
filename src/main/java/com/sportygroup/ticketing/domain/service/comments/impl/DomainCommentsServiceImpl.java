package com.sportygroup.ticketing.domain.service.comments.impl;

import com.sportygroup.ticketing.domain.exception.TicketNotFoundException;
import com.sportygroup.ticketing.domain.model.comments.Comment;
import com.sportygroup.ticketing.domain.model.comments.Visibility;
import com.sportygroup.ticketing.domain.service.comments.in.DomainCommentsService;
import com.sportygroup.ticketing.domain.service.comments.out.CommentStorage;
import com.sportygroup.ticketing.domain.service.tickets.out.TicketingStorage;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class DomainCommentsServiceImpl implements DomainCommentsService {

  private final TicketingStorage ticketingStorage;
  private final CommentStorage commentStorage;

  // Example method
  @Override
  public void addComment(UUID ticketId, String authorId, String content, Visibility visibility) {
    //validations
    var ticket = Optional.ofNullable(ticketingStorage.findById(ticketId))
        .orElseThrow(() -> new TicketNotFoundException(String.format("Ticket not found with id: %s", ticketId)));

    var comment = createComment(ticket.getId(), authorId, content, visibility);

    commentStorage.saveComment(comment);
  }

  private Comment createComment(UUID ticketId, String authorId, String content, Visibility visibility) {
    return Comment.builder().
        ticketId(ticketId)
        .authorId(authorId)
        .content(content)
        .visibility(visibility)
        .createdAt(LocalDateTime.now())
        .build();
  }
}
