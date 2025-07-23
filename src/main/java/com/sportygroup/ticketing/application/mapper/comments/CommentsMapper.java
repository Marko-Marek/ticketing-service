package com.sportygroup.ticketing.application.mapper.comments;

import com.sportygroup.ticketing.api.rest.v1.common.Visibility;
import com.sportygroup.ticketing.api.rest.v1.response.Comment;
import java.util.Objects;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CommentsMapper {

  public static Comment toResponse(com.sportygroup.ticketing.domain.model.comments.Comment domainComment) {
    if (Objects.isNull(domainComment)) {
      return null;
    }

    return Comment.builder()
        .commentId(domainComment.getCommentId())
        .authorId(domainComment.getAuthorId())
        .content(domainComment.getContent())
        .visibility(Visibility.valueOf(domainComment.getVisibility().name()))
        .createdAt(domainComment.getCreatedAt())
        .build();

  }

}
