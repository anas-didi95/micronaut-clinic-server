package com.anasdidi.clinic.domain.user;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.HttpClient;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@MicronautTest
class UserControllerTests {

  private final HttpClient httpClient;

  @Inject
  UserControllerTests(HttpClient httpClient) {
    this.httpClient = httpClient;
  }

  @Test
  void testHello() {
    HttpRequest<String> request = HttpRequest.GET("/v1/user");
    Mono.from(httpClient.exchange(request)).subscribe(responseBody -> {
      Assertions.assertNotNull(responseBody);
      Assertions.assertEquals("Hello world", responseBody);
    });
  }
}
