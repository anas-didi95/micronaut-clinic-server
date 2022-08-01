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

  @Inject
  UserControllerV1(UserService userService) {
    this.userService = userService;
  }

  @Post(value = "/", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  Mono<HttpResponse<ResponseDTO>> createUser(@Body UserDTO requestBody) {
    return Mono.just(requestBody)
        .map(dto -> UserDAO.builder().name(dto.getName()).build())
        .flatMap(domain -> userService.create(domain))
        .map(result -> ResponseDTO.builder().id(result.getId()).build())
        .map(responseBody -> HttpResponse.status(HttpStatus.CREATED).body(responseBody));
  }
}
