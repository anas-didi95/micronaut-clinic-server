package com.anasdidi.clinic.exception;

import com.anasdidi.clinic.common.BaseException;

import lombok.Getter;

@Getter
public class RecordNotFoundException extends BaseException {

  private final String id;

  public RecordNotFoundException(String traceId, String id) {
    super(traceId);
    this.id = id;
  }

  @Override
  public String getMessage() {
    return "traceId=%s, id=%s".formatted(traceId, id);
  }
}
