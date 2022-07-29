package com.anasdidi.clinic.domain.user;

import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.specification.RequestSpecification;

@MicronautTest
class UserControllerTests {

  private static final String baseURI = "/v1/user";

  @Test
  void testHello(RequestSpecification spec) {
    spec
        .given().when().get(baseURI)
        .then().statusCode(HttpStatus.OK.getCode()).body(is("Hello world"));
  }
}
