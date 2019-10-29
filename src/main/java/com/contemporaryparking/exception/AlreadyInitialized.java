package com.contemporaryparking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class AlreadyInitialized extends Exception {

  public AlreadyInitialized(String message) {
    super(message);
  }
}
