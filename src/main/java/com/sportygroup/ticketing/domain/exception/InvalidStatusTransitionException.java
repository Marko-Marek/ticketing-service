package com.sportygroup.ticketing.domain.exception;

public class InvalidStatusTransitionException extends RuntimeException {

  public InvalidStatusTransitionException(String message) {
    super(message);
  }
}
