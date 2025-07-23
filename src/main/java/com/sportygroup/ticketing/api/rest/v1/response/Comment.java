package com.sportygroup.ticketing.api.rest.v1.response;

import com.sportygroup.ticketing.api.rest.v1.common.Visibility;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;

@Builder
public record Comment(
    UUID commentId,
    String authorId,
    String content,
    Visibility visibility,
    LocalDateTime createdAt
) {

}