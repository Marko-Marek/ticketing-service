package com.sportygroup.ticketing.domain.service.constants;

import com.sportygroup.ticketing.domain.model.comments.Comment;
import com.sportygroup.ticketing.domain.model.comments.Visibility;
import java.util.UUID;

public class CommentConstants {

  public static final Comment PUBLIC_COMMENT_1 = Comment.builder()
      .commentId(UUID.randomUUID())
      .ticketId(UUID.randomUUID())
      .content("This is a public comment.")
      .authorId("user123")
      .visibility(Visibility.PUBLIC)
      .build();

  public static final Comment PUBLIC_COMMENT_2 = Comment.builder()
      .commentId(UUID.randomUUID())
      .ticketId(UUID.randomUUID())
      .content("This is a public comment 2.")
      .authorId("user123")
      .visibility(Visibility.PUBLIC)
      .build();

  public static final Comment INTERNAL_COMMENT_1 = Comment.builder()
      .commentId(UUID.randomUUID())
      .ticketId(UUID.randomUUID())
      .content("This is a internal comment 1.")
      .authorId("user123")
      .visibility(Visibility.INTERNAL)
      .build();

  public static final Comment INTERNAL_COMMENT_2 = Comment.builder()
      .commentId(UUID.randomUUID())
      .ticketId(UUID.randomUUID())
      .content("This is a internal comment 2.")
      .authorId("user123")
      .visibility(Visibility.INTERNAL)
      .build();

}
