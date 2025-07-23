package com.sportygroup.ticketing.domain.model.comments;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;
import lombok.With;

@Builder
@With
@Getter
public class Comment {

  private UUID commentId;
  private UUID ticketId;
  private String authorId;
  private String content;
  private Visibility visibility;
  private LocalDateTime createdAt;
}
