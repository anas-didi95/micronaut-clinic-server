package com.anasdidi.clinic.common;

public final class CommonConstants {

  public enum Error {
    VALIDATION_ERROR("E001"),
    RECORD_ALREADY_EXISTS("E002"),
    RECORD_NOT_FOUND("E003"),
    RECORD_METADATA_NOT_MATCHED("E004");

    public final String code;

    private Error(String code) {
      this.code = code;
    }
  }

  public enum GQLContext {
    TRACE_ID("traceId");

    public final String key;

    private GQLContext(String key) {
      this.key = key;
    }
  }
}
