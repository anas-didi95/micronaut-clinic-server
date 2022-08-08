package com.anasdidi.clinic.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RecordNotFoundException extends Exception {

  private final String id;
}
