package com.anasdidi.clinic.common;

public final class CommonConstants {

  public enum Error {
    RECORD_ALREADY_EXISTS("E002", "Record [%s] already exists!");

    public final String code;
    public final String message;

    private Error(String code, String message) {
      this.code = code;
      this.message = message;
    }
  }
}
