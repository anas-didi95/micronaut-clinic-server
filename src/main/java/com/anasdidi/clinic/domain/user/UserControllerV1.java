package com.anasdidi.clinic.domain.user;

import com.anasdidi.clinic.common.ResponseDTO;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@Controller(value = "/v1/user")
class UserControllerV1 {

  private final UserService userService;
  private final UserValidator userValidator;

  @Inject
  UserControllerV1(UserService userService, UserValidator userValidator) {
    this.userService = userService;
    this.userValidator = userValidator;
  }

  @Post(value = "/", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  Mono<HttpResponse<ResponseDTO>> createUser(@Body UserDTO requestBody) {
    return Mono.just(requestBody)
        .map(dto -> UserDAO.builder().id(dto.getId()).fullName(dto.getFullName()).build())
        .flatMap(domain -> userValidator.test(domain))
        .flatMap(domain -> userService.create(domain))
        .map(result -> ResponseDTO.builder().id(result.getId()).build())
        .map(responseBody -> HttpResponse.status(HttpStatus.CREATED).body(responseBody));
  }
}
