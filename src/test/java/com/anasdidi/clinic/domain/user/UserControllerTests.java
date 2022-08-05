package com.anasdidi.clinic.domain.user;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.anasdidi.clinic.common.ResponseDTO;

import io.micronaut.http.HttpStatus;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@MicronautTest
class UserControllerTests {

  private static final String baseURI = "/clinic/v1/user";
  private final UserRepository userRepository;

  @Inject
  UserControllerTests(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Test
  void testUserCreateSuccess(RequestSpecification spec) {
    String value = "" + System.currentTimeMillis();
    UserDTO requestBody = UserDTO.builder().id("id" + value).fullName("name" + value).build();
    Long beforeCount = Mono.from(userRepository.count()).block();

    ResponseDTO responseBody = spec
        .given().body(requestBody).accept(ContentType.JSON).contentType(ContentType.JSON)
        .when().post(baseURI)
        .then().statusCode(HttpStatus.CREATED.getCode())
        .body("id", Matchers.notNullValue())
        .extract().response().body().as(ResponseDTO.class);

    Long afterCount = Mono.from(userRepository.count()).block();
    Assertions.assertEquals(beforeCount + 1, afterCount);

    UserDAO dao = Mono.from(userRepository.findById(responseBody.getId())).block();
    Assertions.assertNotNull(dao);
    Assertions.assertEquals(requestBody.getId(), dao.getId());
    Assertions.assertEquals(requestBody.getFullName(), dao.getFullName());
    Assertions.assertNotNull(dao.getCreatedDate());
    Assertions.assertNotNull(dao.getCreatedBy());
    Assertions.assertEquals(0, dao.getVersion());
  }

  @Test
  void testUserCreateValidationError(RequestSpecification spec) {
    String value = "";
    UserDTO requestBody = UserDTO.builder().id(value).fullName(value).build();

    spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .when().post(baseURI)
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("code", Matchers.is("E001"))
        .body("message", Matchers.is("Validation error!"))
        .body("errorList", Matchers.hasSize(2));
  }

  @Test
  void testUserCreateAlreadyExistsError(RequestSpecification spec) {
    String value = "" + System.currentTimeMillis();
    UserDTO requestBody = UserDTO.builder().id("id" + value).fullName("fullName" + value).build();
    Mono.from(userRepository.save(UserDAO.builder().id("id" + value).fullName("fullName" + value).build())).block();

    spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .when().post(baseURI)
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("code", Matchers.is("E002"))
        .body("message", Matchers.is("Record [%s] already exists!".formatted(requestBody.getId())));
  }
}
