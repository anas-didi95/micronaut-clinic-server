package com.anasdidi.clinic.domain.user;

import com.anasdidi.clinic.common.BaseController;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.annotation.Controller;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@Controller(value = "/v1/user")
class UserControllerV1 extends BaseController implements UserController {

  private final UserService userService;
  private final UserValidator userValidator;

  @Inject
  UserControllerV1(UserService userService, UserValidator userValidator) {
    this.userService = userService;
    this.userValidator = userValidator;
  }

  @Override
  public Mono<HttpResponse<UserDTO>> createUser(UserDTO requestBody) {
    String traceId = generateTraceId();

    return Mono.just(requestBody)
        .map(dto -> UserUtils.copy(dto))
        .flatMap(dao -> userValidator.validate(dao, traceId))
        .flatMap(dao -> userService.createUser(dao, traceId))
        .map(responseBody -> HttpResponse.status(HttpStatus.CREATED).body(responseBody));
  }

  @Override
  public Mono<HttpResponse<UserDTO>> updateUser(String id, UserDTO requestBody) {
    String traceId = generateTraceId();

    return Mono.just(requestBody)
        .map(dto -> UserUtils.copy(dto))
        .flatMap(dao -> userValidator.validate(dao, traceId))
        .flatMap(dao -> userService.updataUser(id, dao, traceId))
        .map(responseBody -> HttpResponse.status(HttpStatus.OK).body(responseBody));
  }

  @Override
  public Mono<HttpResponse<Void>> deleteUser(String id, UserDTO requestBody) {
    String traceId = generateTraceId();

    return Mono.just(requestBody)
        .map(dto -> UserUtils.copy(dto))
        .flatMap(dao -> userService.deleteUser(id, dao, traceId))
        .map(result -> HttpResponse.status(HttpStatus.NO_CONTENT));
  }
}
