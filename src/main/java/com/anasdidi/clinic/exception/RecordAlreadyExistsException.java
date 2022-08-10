package com.anasdidi.clinic.exception;

import com.anasdidi.clinic.common.BaseException;

import lombok.Getter;

@Getter
public class RecordAlreadyExistsException extends BaseException {

  private final String id;

  public RecordAlreadyExistsException(String traceId, String id) {
    super(traceId);
    this.id = id;
  }

  @Override
  public String getMessage() {
    return "traceId=%s, id=%s".formatted(traceId, id);
  }
}
