package com.contemporaryparking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoAvailableParkingSlot extends Exception {

  public NoAvailableParkingSlot(String message) {
    super(message);
  }
}
