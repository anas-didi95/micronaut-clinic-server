package com.anasdidi.clinic.common;

import java.util.Map;

import com.anasdidi.clinic.client.AppClient;
import com.anasdidi.clinic.common.TestConstants.User;

import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

public final class TestUtils {

  public static final String getAccessToken(AppClient appClient) {
    Map<String, Object> testMap = TestConstants.testMap;
    String accessToken = (String) testMap.get("accessToken");
    if (accessToken == null) {
      User user = TestConstants.User.ADMIN1;
      UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user.id, user.password);
      BearerAccessRefreshToken loginRsp = appClient.login(creds);
      accessToken = loginRsp.getAccessToken();
      testMap.put("accessToken", accessToken);
    }
    return accessToken;
  }
}
