package com.anasdidi.clinic.common;

import java.util.HashMap;
import java.util.Map;

public final class TestConstants {

  public static final Map<String, Object> testMap = new HashMap<>();

  public enum User {
    ADMIN1("admin1", "p@ssw0rd");

    public final String id;
    public final String password;

    User(String id, String password) {
      this.id = id;
      this.password = password;
    }
  }
}
