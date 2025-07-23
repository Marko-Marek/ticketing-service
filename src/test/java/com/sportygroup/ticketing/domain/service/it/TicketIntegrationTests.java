package com.sportygroup.ticketing.domain.service.it;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sportygroup.ticketing.api.rest.v1.common.Visibility;
import com.sportygroup.ticketing.api.rest.v1.request.AddCommentRequest;
import com.sportygroup.ticketing.api.rest.v1.request.CreateTicketRequest;
import com.sportygroup.ticketing.api.rest.v1.request.UpdateStatusRequest;
import com.sportygroup.ticketing.api.rest.v1.response.Comment;
import com.sportygroup.ticketing.api.rest.v1.response.CreateTicketResponse;
import com.sportygroup.ticketing.api.rest.v1.response.Ticket;
import com.sportygroup.ticketing.api.rest.v1.response.TicketStatus;
import com.sportygroup.ticketing.application.rest.controller.TicketController;
import com.sportygroup.ticketing.application.service.ApplicationTicketingService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(TicketController.class)
class TicketIntegrationTests {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ApplicationTicketingService applicationTicketingService;

  @Test
  void testTicketCreation() throws Exception {
    UUID ticketId = UUID.randomUUID();

    CreateTicketRequest createTicketRequest = CreateTicketRequest.builder()
        .userId("user-123")
        .subject("Login Issue")
        .description("Cannot log in")
        .build();

    CreateTicketResponse createTicketResponse = CreateTicketResponse.builder()
        .ticketId(ticketId)
        .build();

    given(applicationTicketingService.createTicket(createTicketRequest))
        .willReturn(createTicketResponse);

    String requestBody = """
            {
              "userId": "user-123",
              "subject": "Login Issue",
              "description": "Cannot log in"
            }
        """;

    mockMvc.perform(post("/api/v1/tickets")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.ticketId", is(ticketId.toString())));
  }

  @Test
  void testGetTicketWithComments() throws Exception {
    Comment comment = Comment.builder()
        .commentId(UUID.randomUUID())
        .content("Initial comment")
        .authorId("Author")
        .visibility(Visibility.PUBLIC)
        .createdAt(LocalDateTime.now())
        .build();

    var sampleTicket = Ticket.builder()
        .ticketId(UUID.randomUUID())
        .userId("user-123")
        .subject("Login Issue")
        .description("Cannot log in")
        .status(TicketStatus.OPEN)
        .createdAt(java.time.LocalDateTime.now())
        .updatedAt(java.time.LocalDateTime.now())
        .comments(List.of(comment))
        .build();

    given(applicationTicketingService.getTickets(Optional.of("OPEN"), Optional.empty(), Optional.empty()))
        .willReturn(List.of(sampleTicket));

    mockMvc.perform(get("/api/v1/tickets")
            .param("status", "OPEN")
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$[0].ticketId", is(sampleTicket.ticketId().toString())))
        .andExpect(jsonPath("$[0].userId", is(sampleTicket.userId())))
        .andExpect(jsonPath("$[0].subject", is(sampleTicket.subject())))
        .andExpect(jsonPath("$[0].description", is(sampleTicket.description())))
        .andExpect(jsonPath("$[0].status", is(sampleTicket.status().toString())))
        .andExpect(jsonPath("$[0].createdAt", notNullValue()))
        .andExpect(jsonPath("$[0].updatedAt", notNullValue()))

        .andExpect(jsonPath("$[0].comments", hasSize(1)))
        .andExpect(jsonPath("$[0].comments[0].commentId", is(comment.commentId().toString())))
        .andExpect(jsonPath("$[0].comments[0].authorId", is(comment.authorId())))
        .andExpect(jsonPath("$[0].comments[0].content", is(comment.content())))
        .andExpect(jsonPath("$[0].comments[0].visibility", is(comment.visibility().toString())))
        .andExpect(jsonPath("$[0].comments[0].createdAt", notNullValue()));
  }

  @Test
  void testStatusTransitionValid() throws Exception {
    Comment comment = Comment.builder()
        .commentId(UUID.randomUUID())
        .content("Initial comment")
        .authorId("Author")
        .visibility(Visibility.PUBLIC)
        .createdAt(LocalDateTime.now())
        .build();

    var sampleTicket = Ticket.builder()
        .ticketId(UUID.randomUUID())
        .userId("user-123")
        .subject("Login Issue")
        .description("Cannot log in")
        .status(TicketStatus.IN_PROGRESS)
        .createdAt(java.time.LocalDateTime.now())
        .updatedAt(java.time.LocalDateTime.now())
        .comments(List.of(comment))
        .build();

    UpdateStatusRequest request = UpdateStatusRequest.builder()
        .status(TicketStatus.IN_PROGRESS)
        .build();

    ObjectMapper objectMapper = new ObjectMapper();

    mockMvc.perform(patch("/api/v1/tickets/{ticketId}/status", sampleTicket.ticketId())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk());
  }

  @Test
  void addComment_shouldReturnCreated() throws Exception {
    UUID ticketId = UUID.randomUUID();
    AddCommentRequest request = AddCommentRequest.builder()
        .content("This is a comment")
        .authorId("Author")
        .visibility(Visibility.PUBLIC)
        .build();

    ObjectMapper objectMapper = new ObjectMapper();

    mockMvc.perform(post("/api/v1/tickets/" + ticketId + "/comments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isCreated());
  }
}