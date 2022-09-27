package com.anasdidi.clinic.domain.auth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.anasdidi.clinic.client.AppClient;
import com.anasdidi.clinic.common.TestConstants;
import com.anasdidi.clinic.common.TestConstants.User;
import com.anasdidi.clinic.common.TestUtils;
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
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;

@MicronautTest
public class AuthControllerTests {

  private final String baseURI = "/clinic";
  private final String loginURI = baseURI + "/login";
  private final String refreshTokenURI = baseURI + "/oauth/access_token";
  private final HttpClient client;
  private final RefreshTokenGenerator refreshTokenGenerator;
  private final AuthRepository authRepository;
  private final AppClient appClient;

  @Inject
  AuthControllerTests(@Client("/") HttpClient client, RefreshTokenGenerator refreshTokenGenerator,
      AuthRepository authRepository, AppClient appClient) {
    this.client = client;
    this.refreshTokenGenerator = refreshTokenGenerator;
    this.authRepository = authRepository;
    this.appClient = appClient;
  }

  public String getAccessToken() {
    return TestUtils.getAccessToken(appClient);
  }

  @Test
  void accessingASecuredUrlWithoutAuthenticatingReturnsUnauthorized() {
    HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
      client.toBlocking().exchange(HttpRequest.GET(baseURI).accept(MediaType.TEXT_PLAIN));
    });

    assertEquals(HttpStatus.UNAUTHORIZED, e.getStatus());
  }

  @Test
  void uponSuccessfulAuthenticationAJsonWebTokenIsIssuedToTheUser() throws ParseException {
    User user = TestConstants.User.ADMIN1;
    UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user.id, user.password);
    HttpRequest<?> request = HttpRequest.POST(loginURI, creds);
    HttpResponse<BearerAccessRefreshToken> rsp = client.toBlocking().exchange(request, BearerAccessRefreshToken.class);
    assertEquals(HttpStatus.OK, rsp.getStatus());

    BearerAccessRefreshToken bearerAccessRefreshToken = rsp.body();
    assertEquals(user.id, bearerAccessRefreshToken.getUsername());
    assertNotNull(bearerAccessRefreshToken.getAccessToken());
    assertTrue(JWTParser.parse(bearerAccessRefreshToken.getAccessToken()) instanceof SignedJWT);

    String accessToken = bearerAccessRefreshToken.getAccessToken();
    HttpRequest<?> requestWithAuthorization = HttpRequest.GET(baseURI + "/auth/username")
        .accept(MediaType.TEXT_PLAIN)
        .bearerAuth(accessToken);
    HttpResponse<String> response = client.toBlocking().exchange(requestWithAuthorization, String.class);

    assertEquals(HttpStatus.OK, rsp.getStatus());
    assertEquals(TestConstants.User.ADMIN1.id, response.body());
  }

  @Test
  void uponSuccessfulAuthenticationUserGetsAccessTokenAndRefreshToken() throws ParseException {
    User user = TestConstants.User.ADMIN1;
    UsernamePasswordCredentials creds = new UsernamePasswordCredentials(user.id, user.password);
    HttpRequest<?> request = HttpRequest.POST(loginURI, creds);
    BearerAccessRefreshToken rsp = client.toBlocking().retrieve(request, BearerAccessRefreshToken.class);

    assertEquals(user.id, rsp.getUsername());
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
          HttpRequest.POST(refreshTokenURI, new TokenRefreshRequest(unsignedRefreshedToken)),
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
    Authentication user = Authentication.build(TestConstants.User.ADMIN1.id);

    String refreshToken = refreshTokenGenerator.createKey(user);
    Optional<String> refreshTokenOptional = refreshTokenGenerator.generate(user, refreshToken);
    assertTrue(refreshTokenOptional.isPresent());

    String signedRefreshToken = refreshTokenOptional.get();
    Argument<BearerAccessRefreshToken> bodyArgument = Argument.of(BearerAccessRefreshToken.class);
    Argument<Map> errorArgument = Argument.of(Map.class);
    HttpRequest<?> req = HttpRequest.POST(refreshTokenURI, new TokenRefreshRequest(signedRefreshToken));

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

  @Test
  @SuppressWarnings("rawtypes")
  void accessingSecuredURLWithoutAuthenticatingReturnsUnauthorized_RefreshTokenRevokedTest() {
    Authentication user = Authentication.build(TestConstants.User.ADMIN1.id);

    String refreshToken = refreshTokenGenerator.createKey(user);
    Optional<String> refreshTokenOptional = refreshTokenGenerator.generate(user, refreshToken);
    assertTrue(refreshTokenOptional.isPresent());

    authRepository.deleteAll().block();
    long oldTokenCount = authRepository.count().block();
    String signedRefreshToken = refreshTokenOptional.get();
    AuthDAO dao = AuthDAO.builder().userId(user.getName()).refreshToken(refreshToken).build();
    dao.setIsDeleted(true);
    dao = authRepository.save(dao).block();
    assertEquals(oldTokenCount + 1, authRepository.count().block());

    Argument<BearerAccessRefreshToken> bodyArgument = Argument.of(BearerAccessRefreshToken.class);
    Argument<Map> errorArgument = Argument.of(Map.class);
    HttpClientResponseException e = assertThrows(HttpClientResponseException.class, () -> {
      client.toBlocking().exchange(
          HttpRequest.POST(refreshTokenURI, new TokenRefreshRequest(signedRefreshToken)),
          bodyArgument,
          errorArgument);
    });
    assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());

    Optional<Map> mapOptional = e.getResponse().getBody(Map.class);
    assertTrue(mapOptional.isPresent());

    Map m = mapOptional.get();
    assertEquals("invalid_grant", m.get("error"));
    assertEquals("refresh token revoked", m.get("error_description"));
  }

  @Test
  public void testAuthLogoutSuccess(RequestSpecification spec) {
    String accessToken = getAccessToken();
    authRepository.deleteAll().block();
    Long beforeCount = 0L;

    spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON)
        .auth().oauth2(accessToken)
        .when().delete("/clinic/auth/logout")
        .then().statusCode(HttpStatus.NO_CONTENT.getCode());

    Long afterCount = authRepository.count().block();
    assertEquals(beforeCount, afterCount);
  }
}
