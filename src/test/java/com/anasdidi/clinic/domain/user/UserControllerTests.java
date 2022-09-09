package com.anasdidi.clinic.domain.user;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.anasdidi.clinic.client.AppClient;
import com.anasdidi.clinic.common.TestUtils;

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
  private final AppClient appClient;

  @Inject
  UserControllerTests(UserRepository userRepository, AppClient appClient) {
    this.userRepository = userRepository;
    this.appClient = appClient;
  }

  private UserDTO getRequestBody() {
    String value = "" + System.currentTimeMillis();
    UserDTO requestBody = UserDTO.builder()
        .password("password")
        .fullName("fullName" + value)
        .build();
    requestBody.setId("id" + value);
    return requestBody;
  }

  public String getAccessToken() {
    return TestUtils.getAccessToken(appClient);
  }

  private void assertRecord(boolean isUpdate, UserDTO dto, UserDAO dao, Long expectedVersion) {
    Assertions.assertEquals(dto.getId(), dao.getId());
    Assertions.assertEquals(dto.getFullName(), dao.getFullName());
    Assertions.assertNotNull(dao.getIsDeleted());
    Assertions.assertNotNull(dao.getCreatedDate());
    Assertions.assertNotNull(dao.getCreatedBy());
    Assertions.assertEquals(expectedVersion, dao.getVersion());
    if (isUpdate) {
      Assertions.assertNotNull(dao.getUpdatedBy());
      Assertions.assertNotNull(dao.getUpdatedDate());
    }
  }

  @Test
  void testUserCreateSuccess(RequestSpecification spec) {
    UserDTO requestBody = getRequestBody();
    Long beforeCount = Mono.from(userRepository.count()).block();

    UserDTO responseBody = spec
        .given().body(requestBody).accept(ContentType.JSON).contentType(ContentType.JSON)
        .auth().oauth2(getAccessToken())
        .when().post(baseURI)
        .then().statusCode(HttpStatus.CREATED.getCode())
        .body("id", Matchers.notNullValue())
        .extract().response().body().as(UserDTO.class);

    Long afterCount = Mono.from(userRepository.count()).block();
    Assertions.assertEquals(beforeCount + 1, afterCount);

    UserDAO dao = Mono.from(userRepository.findById(responseBody.getId())).block();
    assertRecord(false, requestBody, dao, 0L);
  }

  @Test
  void testUserCreateValidationError(RequestSpecification spec) {
    UserDTO requestBody = UserDTO.builder().build();

    spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .auth().oauth2(getAccessToken())
        .when().post(baseURI)
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("traceId", Matchers.notNullValue())
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
        .auth().oauth2(getAccessToken())
        .when().post(baseURI)
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("traceId", Matchers.notNullValue())
        .body("code", Matchers.is("E002"))
        .body("message", Matchers.notNullValue());
  }

  @Test
  void testUserUpdateSuccess(RequestSpecification spec) {
    UserDTO dto = getRequestBody();
    UserDAO domain = userRepository.save(UserUtils.copy(dto)).block();
    UserDTO requestBody = UserDTO.builder().fullName("update" + System.currentTimeMillis()).build();
    requestBody.setId(domain.getId());
    requestBody.setVersion(domain.getVersion());

    UserDTO responseBody = spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .auth().oauth2(getAccessToken())
        .when().put("%s/%s".formatted(baseURI, domain.getId()))
        .then().statusCode(HttpStatus.OK.getCode())
        .body("id", Matchers.notNullValue())
        .extract().response().as(UserDTO.class);

    UserDAO result = userRepository.findById(responseBody.getId()).block();
    assertRecord(true, requestBody, result, requestBody.getVersion() + 1);
  }

  @Test
  public void testUserUpdateValidationError(RequestSpecification spec) {
    UserDTO dto = getRequestBody();
    UserDAO domain = userRepository.save(UserUtils.copy(dto)).block();
    UserDTO requestBody = UserDTO.builder().build();
    requestBody.setVersion(domain.getVersion());

    spec
        .accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .auth().oauth2(getAccessToken())
        .when().put("%s/%s".formatted(baseURI, domain.getId()))
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("traceId", Matchers.notNullValue())
        .body("code", Matchers.is("E001"))
        .body("message", Matchers.notNullValue())
        .body("errorList", Matchers.notNullValue());
  }

  @Test
  public void testUserUpdateRecordNotFoundError(RequestSpecification spec) {
    UserDTO dto = getRequestBody();
    UserDAO domain = userRepository.save(UserUtils.copy(dto)).block();
    UserDTO requestBody = UserDTO.builder().fullName("update" + System.currentTimeMillis()).build();
    requestBody.setId(domain.getId());
    requestBody.setVersion(domain.getVersion());

    spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .auth().oauth2(getAccessToken())
        .when().put("%s/%s".formatted(baseURI, System.currentTimeMillis()))
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("traceId", Matchers.notNullValue())
        .body("code", Matchers.is("E003"))
        .body("message", Matchers.notNullValue());
  }

  @Test
  public void testUserRecordMetadataNotMatchedError(RequestSpecification spec) {
    UserDTO dto = getRequestBody();
    UserDAO domain = userRepository.save(UserUtils.copy(dto)).block();
    UserDTO requestBody = UserDTO.builder().fullName("update" + System.currentTimeMillis()).build();
    requestBody.setId(domain.getId());
    requestBody.setVersion(-1L);

    spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .auth().oauth2(getAccessToken())
        .when().put("%s/%s".formatted(baseURI, domain.getId()))
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("traceId", Matchers.notNullValue())
        .body("code", Matchers.is("E004"))
        .body("message", Matchers.notNullValue());
  }

  @Test
  public void testUserDeleteSuccess(RequestSpecification spec) {
    UserDTO dto = getRequestBody();
    UserDAO domain = userRepository.save(UserUtils.copy(dto)).block();
    UserDTO requestBody = UserDTO.builder().build();
    requestBody.setId(domain.getId());
    requestBody.setVersion(domain.getVersion());
    Long beforeCount = userRepository.count().block();

    spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .auth().oauth2(getAccessToken())
        .when().delete("%s/%s".formatted(baseURI, domain.getId()))
        .then().statusCode(HttpStatus.NO_CONTENT.getCode());

    Long afterCount = userRepository.count().block();
    Assertions.assertEquals(beforeCount - 1, afterCount);
  }

  @Test
  public void testUserDeleteRecordMetadataNotMatchedError(RequestSpecification spec) {
    UserDTO dto = getRequestBody();
    UserDAO domain = userRepository.save(UserUtils.copy(dto)).block();
    UserDTO requestBody = UserDTO.builder().build();
    requestBody.setId(domain.getId());
    requestBody.setVersion(-1L);

    spec
        .given().accept(ContentType.JSON).contentType(ContentType.JSON).body(requestBody)
        .auth().oauth2(getAccessToken())
        .when().delete("%s/%s".formatted(baseURI, domain.getId()))
        .then().statusCode(HttpStatus.BAD_REQUEST.getCode())
        .body("traceId", Matchers.notNullValue())
        .body("code", Matchers.is("E004"))
        .body("message", Matchers.notNullValue());
  }
}
