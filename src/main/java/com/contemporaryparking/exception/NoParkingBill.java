package com.contemporaryparking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NoParkingBill extends Exception {

  public NoParkingBill(String message) {
    super(message);
  }
}
