package com.anasdidi.clinic.domain.user;

import com.anasdidi.clinic.common.BaseController;
import com.anasdidi.clinic.common.ResponseDTO;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@Controller(value = "/v1/user")
class UserControllerV1 extends BaseController {

  private final UserService userService;
  private final UserValidator userValidator;

  @Inject
  UserControllerV1(UserService userService, UserValidator userValidator) {
    this.userService = userService;
    this.userValidator = userValidator;
  }

  @Post(value = "/", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  Mono<HttpResponse<ResponseDTO>> createUser(@Body UserDTO requestBody) {
    String traceId = generateTraceId();

    return Mono.just(requestBody)
        .map(dto -> UserUtils.copy(dto))
        .flatMap(dao -> userValidator.validate(dao, traceId))
        .flatMap(dao -> userService.createUser(dao, traceId))
        .map(result -> ResponseDTO.builder().id(result.getId()).build())
        .map(responseBody -> HttpResponse.status(HttpStatus.CREATED).body(responseBody));
  }

  @Put(value = "/{id}", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  Mono<HttpResponse<ResponseDTO>> updateUser(@PathVariable String id, @Body UserDTO requestBody) {
    String traceId = generateTraceId();

    return Mono.just(requestBody)
        .map(dto -> UserUtils.copy(dto))
        .flatMap(dao -> userValidator.validate(dao, traceId))
        .flatMap(dao -> userService.updataUser(id, dao, traceId))
        .map(result -> ResponseDTO.builder().id(result.getId()).build())
        .map(responseBody -> HttpResponse.status(HttpStatus.OK).body(responseBody));
  }

  @Delete(value = "/{id}", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  Mono<HttpResponse<Void>> deleteUser(@PathVariable String id, @Body UserDTO requestBody) {
    String traceId = generateTraceId();

    return Mono.just(requestBody)
        .map(dto -> UserUtils.copy(dto))
        .flatMap(dao -> userService.deleteUser(id, dao, traceId))
        .map(result -> HttpResponse.status(HttpStatus.NO_CONTENT));
  }
}
