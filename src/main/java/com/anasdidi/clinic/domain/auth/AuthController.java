package com.anasdidi.clinic.domain.auth;

import java.security.Principal;

import com.anasdidi.clinic.common.CommonUtils;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import jakarta.inject.Inject;
import reactor.core.publisher.Mono;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(value = "/auth")
class AuthController {

  private final AuthService authService;

  @Inject
  AuthController(AuthService authService) {
    this.authService = authService;
  }

  @Produces(MediaType.TEXT_PLAIN)
  @Get("/username")
  public String getAuthenticatedUsername(Principal principal) {
    return principal.getName();
  }

  @Delete(value = "/logout", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  Mono<HttpResponse<Void>> logout(Principal principal) {
    String traceId = CommonUtils.generateTraceId();

    return Mono.just(principal)
        .flatMap(p -> authService.logout(p, traceId))
        .map(result -> HttpResponse.status(HttpStatus.NO_CONTENT));
  }
}
