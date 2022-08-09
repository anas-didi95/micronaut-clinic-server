package com.anasdidi.clinic.exception;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidationException extends Exception {

  private final List<String> errorList;

  @Override
  public String getMessage() {
    return "errorList.size=%s".formatted(errorList.size());
  }
}
