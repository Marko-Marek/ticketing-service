package com.sportygroup.ticketing.domain.model.tickets;

import com.sportygroup.ticketing.domain.model.comments.Comment;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.With;

@Builder
@With
@Getter
public class Ticket {

  private UUID id;
  private String subject;
  private String description;
  private TicketStatus status;
  private String userId;
  private String assigneeId;
  private LocalDateTime createdAt;
  private LocalDateTime modifiedAt;
  @Setter
  private List<Comment> comments;

}
