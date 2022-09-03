package com.anasdidi.clinic.common;

public abstract class BaseException extends Exception {

  protected final String traceId;

  public BaseException(String traceId) {
    this.traceId = traceId;
  }

  public String getTraceId() {
    return traceId;
  }
}
