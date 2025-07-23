package com.sportygroup.ticketing.api.rest.v1.request;

import com.sportygroup.ticketing.api.rest.v1.common.Visibility;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.validation.annotation.Validated;

@Validated
@Builder
public record AddCommentRequest(
    @NotBlank @Size(min = 5, max = 50)
    String authorId,
    @NotBlank @Size(min = 10, max = 250)
    String content,
    @NotNull
    Visibility visibility
) {

}
