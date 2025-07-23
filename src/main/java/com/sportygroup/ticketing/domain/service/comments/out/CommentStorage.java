package com.sportygroup.ticketing.domain.service.comments.out;

import com.sportygroup.ticketing.domain.model.comments.Comment;
import com.sportygroup.ticketing.domain.model.comments.Visibility;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface CommentStorage {

  /**
   * Saves a comment to the storage.
   *
   * @param comment {@link Comment} to be saved
   */
  void saveComment(Comment comment);

  /**
   * Retrieves comments for given ticket IDs and visibilities.
   *
   * @param ticketIds    list of ticket IDs for which comments are to be retrieved
   * @param visibilities list of visibilities to filter comments
   * @return a map where the key is the ticket ID and the value is a list of comments associated with that ticket
   */
  Map<UUID, List<Comment>> getCommentsByTicketIdAndVisibility(List<UUID> ticketIds, List<Visibility> visibilities);

}
