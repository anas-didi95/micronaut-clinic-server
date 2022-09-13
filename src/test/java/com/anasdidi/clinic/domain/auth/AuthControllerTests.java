package com.anasdidi.clinic.domain.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;

import org.junit.jupiter.api.Test;

import com.nimbusds.jwt.JWTParser;
import com.nimbusds.jwt.SignedJWT;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class AuthControllerTests {

  @Inject
  @Client("/")
  HttpClient client;

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
}
