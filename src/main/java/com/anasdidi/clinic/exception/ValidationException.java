package com.anasdidi.clinic.exception;

import java.util.List;

import com.anasdidi.clinic.common.BaseException;

import lombok.Getter;

@Getter
public class ValidationException extends BaseException {

  private final List<String> errorList;

  public ValidationException(String traceId, List<String> errorList) {
    super(traceId);
    this.errorList = errorList;
  }

  @Override
  public String getMessage() {
    return "traceId=%s, errorList.size=%s".formatted(traceId, errorList.size());
  }
}
