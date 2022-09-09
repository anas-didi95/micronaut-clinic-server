package com.anasdidi.clinic.common;

import java.util.HashMap;
import java.util.Map;

import com.anasdidi.clinic.client.AppClient;

import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

public final class TestUtils {

  private static final Map<String, Object> testMap = new HashMap<>();

  public static final String getAccessToken(AppClient appClient) {
    String accessToken = (String) testMap.get("accessToken");
    if (accessToken == null) {
      UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin1", "p@ssw0rd");
      BearerAccessRefreshToken loginRsp = appClient.login(creds);
      accessToken = loginRsp.getAccessToken();
      testMap.put("accessToken", accessToken);
    }
    return accessToken;
  }
}
