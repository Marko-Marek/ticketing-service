package com.sportygroup.ticketing.domain.service.comments.in;

import com.sportygroup.ticketing.domain.model.comments.Visibility;
import java.util.UUID;

public interface DomainCommentsService {

  /**
   * Adds a comment to the given ticket.
   *
   * @param ticketId   the ID of the ticket to which the comment will be added
   * @param authorId   the ID of the user adding the comment
   * @param content    the content of the comment
   * @param visibility the visibility of the comment {@link Visibility}
   */
  void addComment(UUID ticketId, String authorId, String content, Visibility visibility);

}
