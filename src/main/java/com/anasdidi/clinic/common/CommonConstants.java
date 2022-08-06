package com.anasdidi.clinic.common;

public final class CommonConstants {

  public enum Error {
    VALIDATION_ERROR("E001"),
    RECORD_ALREADY_EXISTS("E002");

    public final String code;

    private Error(String code) {
      this.code = code;
    }
  }
}
