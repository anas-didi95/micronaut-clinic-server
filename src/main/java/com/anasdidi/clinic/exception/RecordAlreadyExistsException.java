package com.anasdidi.clinic.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecordAlreadyExistsException extends Exception {

  private final String id;

  @Override
  public String getMessage() {
    return "id=%s".formatted(id);
  }
}
