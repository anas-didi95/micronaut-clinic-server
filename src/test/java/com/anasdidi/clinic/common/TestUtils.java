package com.anasdidi.clinic.common;

import java.util.Map;

import com.anasdidi.clinic.client.AppClient;

import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

public final class TestUtils {

  public static final String getAccessToken(AppClient appClient) {
    return getAccessToken(appClient, TestConstants.User.ADMIN1);
  }

  public static final String getAccessToken(AppClient appClient, TestConstants.User user) {
    Map<String, Object> testMap = TestConstants.testMap;
    return (String) testMap.computeIfAbsent("accessToken" + user.id, value -> {
      UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user.id, user.password);
      BearerAccessRefreshToken loginRsp = appClient.login(creds);
      return loginRsp.getAccessToken();
    });
  }
}
