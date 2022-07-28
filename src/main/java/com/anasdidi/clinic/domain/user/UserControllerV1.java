package com.anasdidi.clinic.domain.user;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import reactor.core.publisher.Mono;

@Controller(value = "/v1/user")
class UserControllerV1 {

  @Get(value = "/")
  Mono<HttpResponse<String>> hello() {
    return Mono.just("Hello world").map(responseBody -> HttpResponse.ok(responseBody));
  }
}
