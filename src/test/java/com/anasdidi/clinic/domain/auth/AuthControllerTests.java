package com.anasdidi.clinic.domain.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.generator.RefreshTokenGenerator;
import io.micronaut.security.token.jwt.endpoints.TokenRefreshRequest;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class AuthControllerTests {

  private final HttpClient client;
  private final RefreshTokenGenerator refreshTokenGenerator;

  @Inject
  AuthControllerTests(@Client("/") HttpClient client, RefreshTokenGenerator refreshTokenGenerator) {
    this.client = client;
    this.refreshTokenGenerator = refreshTokenGenerator;
  }

  @Test
  void accessingASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
    HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
      client.toBlocking().exchange(HttpRequest.GET("/clinic/").accept(MediaType.TEXT_PLAIN));
    });

    assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
  }

  @Test
  void uponSuccessfulAuthenticationAJsonWebTokenIsIssuedToTheUser() throws ParseException {
    UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin1", "p@ssw0rd");
    HttpRequest<?> request = HttpRequest.POST("/clinic/login", creds);
    HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
    assertEquals(HttpStatus.OK, rsp.getStatus());

    BearerAccessRefreshToken bearerAccessRefreshToken = rsp.body();
    assertEquals("admin1", bearerAccessRefreshToken.getUsername());
    assertNotNull(bearerAccessRefreshToken.getAccessToken());
    assertTrue(JWTParser.parse(bearerAccessRefreshToken.getAccessToken()) instanceof SignedJWT);

    String accessToken = bearerAccessRefreshToken.getAccessToken();
    HttpRequest<?> requestWithAuthorization = HttpRequest.GET("/clinic/auth/username")
        .accept(MediaType.TEXT_PLAIN)
        .bearerAuth(accessToken);
    HttpResponse<String> response = client.toBlocking().exchange(requestWithAuthorization, String.class);

    assertEquals(HttpStatus.OK, rsp.getStatus());
    assertEquals("admin1", response.body());
  }

  @Test
  void uponSuccessfulAuthenticationUserGetsAccessTokenAndRefreshToken() throws ParseException {
    UsernamePasswordCredentials creds = new UsernamePasswordCredentials("admin1", "p@ssw0rd");
    HttpRequest<?> request = HttpRequest.POST("/clinic/login", creds);
    BearerAccessRefreshToken rsp = client.toBlocking().retrieve(request, BearerAccessRefreshToken.class);

    assertEquals("admin1", rsp.getUsername());
    assertNotNull(rsp.getAccessToken());
    assertNotNull(rsp.getRefreshToken());

    assertTrue(JWTParser.parse(rsp.getAccessToken()) instanceof SignedJWT);
  }

  @Test
  @SuppressWarnings("rawtypes")
  void accessingSecuredURLWithoutAuthenticatingReturnsUnauthorized_UnsignedRefreshTokenTest() {
    String unsignedRefreshedToken = "foo";
    Argument<BearerAccessRefreshToken> bodyArgument = Argument.of(BearerAccessRefreshToken.class);
    Argument<Map> errorArgument = Argument.of(Map.class);

    HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
      client.toBlocking().exchange(
          HttpRequest.POST("/clinic/oauth/access_token", new TokenRefreshRequest(unsignedRefreshedToken)),
          bodyArgument,
          errorArgument);
    });
    assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());

    Optional<Map> mapOptional = e.getResponse().getBody(Map.class);
    assertTrue(mapOptional.isPresent());

    Map m = mapOptional.get();
    assertEquals("invalid_grant", m.get("error"));
    assertEquals("Refresh token is invalid", m.get("error_description"));
  }

  @Test
  @SuppressWarnings("rawtypes")
  void accessingSecuredURLWithoutAuthenticatingReturnsUnauthorized_RefreshTokenNotFoundTest() {
    Authentication user = Authentication.build("admin1");

    String refreshToken = refreshTokenGenerator.createKey(user);
    Optional<String> refreshTokenOptional = refreshTokenGenerator.generate(user, refreshToken);
    assertTrue(refreshTokenOptional.isPresent());

    String signedRefreshToken = refreshTokenOptional.get();
    Argument<BearerAccessRefreshToken> bodyArgument = Argument.of(BearerAccessRefreshToken.class);
    Argument<Map> errorArgument = Argument.of(Map.class);
    HttpRequest<?> req = HttpRequest.POST("/clinic/oauth/access_token", new TokenRefreshRequest(signedRefreshToken));

    HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
      client.toBlocking().exchange(req, bodyArgument, errorArgument);
    });
    assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());

    Optional<Map> mapOptional = e.getResponse().getBody(Map.class);
    assertTrue(mapOptional.isPresent());

    Map m = mapOptional.get();
    assertEquals("invalid_grant", m.get("error"));
    assertEquals("refresh token not found", m.get("error_description"));
  }
}
