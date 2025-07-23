package com.sportygroup.ticketing.infrastructure.comments;

import com.sportygroup.ticketing.domain.model.comments.Comment;
import com.sportygroup.ticketing.domain.model.comments.Visibility;
import com.sportygroup.ticketing.domain.service.comments.out.CommentStorage;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CommentStorageImpl implements CommentStorage {

  private Map<UUID, List<Comment>> storage;

  @PostConstruct
  public void init() {
    storage = new ConcurrentHashMap<>();
  }

  @Override
  public void saveComment(Comment comment) {
    final var commentToSave = comment.withCommentId(UUID.randomUUID());

    Optional.ofNullable(storage.get(commentToSave.getTicketId()))
        .ifPresentOrElse(
            comments -> comments.add(commentToSave),
            () -> storage.put(commentToSave.getTicketId(), new ArrayList<>(List.of(commentToSave)))
        );
  }

  @Override
  public Map<UUID, List<Comment>> getCommentsByTicketIdAndVisibility(List<UUID> ticketIds, List<Visibility> visibilities) {
    return storage.values().stream()
        .flatMap(List::stream)
        .filter(x -> ticketIds.contains(x.getTicketId()))
        .filter(x -> visibilities.contains(x.getVisibility()))
        .collect(Collectors.groupingBy(Comment::getTicketId, Collectors.toList())
        );
  }
}
