package com.sportygroup.ticketing.domain.exception;

public class TicketNotFoundException extends RuntimeException {

  public TicketNotFoundException(String message) {
    super(message);
  }
}