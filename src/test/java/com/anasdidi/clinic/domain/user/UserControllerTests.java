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

  private UserDTO getRequestBody() {
    String value = "" + System.currentTimeMillis();
    return UserDTO.builder()
        .id("id" + value)
        .fullName("fullName" + value)
        .build();
  }

  @Test
  void testUserCreateSuccess(RequestSpecification spec) {
    UserDTO requestBody = getRequestBody();
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
    UserDTO requestBody = UserDTO.builder().build();

    spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .when().post(baseURI)
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("code", Matchers.is("E001"))
        .body("message", Matchers.notNullValue())
        .body("errorList", Matchers.notNullValue());
  }

  @Test
  void testUserCreateRecordAlreadyExistsError(RequestSpecification spec) {
    UserDTO requestBody = getRequestBody();
    Mono.from(userRepository.save(UserUtils.copy(requestBody))).block();

    spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .when().post(baseURI)
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("code", Matchers.is("E002"))
        .body("message", Matchers.notNullValue());
  }

  @Test
  void testUserUpdateSuccess(RequestSpecification spec) {
    UserDTO dto = getRequestBody();
    UserDAO domain = userRepository.save(UserUtils.copy(dto)).block();
    UserDTO requestBody = UserDTO.builder().id(domain.getId()).fullName("update" + System.currentTimeMillis()).build();
    requestBody.setVersion(domain.getVersion());

    ResponseDTO responseBody = spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .when().put("%s/%s".formatted(baseURI, domain.getId()))
        .then().statusCode(HttpStatus.OK.getCode())
        .body("id", Matchers.notNullValue())
        .extract().response().as(ResponseDTO.class);

    UserDAO result = userRepository.findById(responseBody.getId()).block();
    Assertions.assertNotNull(result);
    Assertions.assertEquals(requestBody.getFullName(), result.getFullName());
    Assertions.assertNotNull(result.getUpdatedBy());
    Assertions.assertNotNull(result.getUpdatedDate());
    Assertions.assertEquals(domain.getVersion() + 1, result.getVersion());
  }

  @Test
  public void testUserUpdateValidationError(RequestSpecification spec) {
    UserDTO dto = getRequestBody();
    UserDAO domain = userRepository.save(UserUtils.copy(dto)).block();
    UserDTO requestBody = UserDTO.builder().build();
    requestBody.setVersion(domain.getVersion());

    spec
        .accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .when().put("%s/%s".formatted(baseURI, domain.getId()))
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("code", Matchers.is("E001"))
        .body("message", Matchers.notNullValue())
        .body("errorList", Matchers.notNullValue());
  }
}
