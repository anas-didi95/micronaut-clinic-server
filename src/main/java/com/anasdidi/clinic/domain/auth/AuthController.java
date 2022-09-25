package com.anasdidi.clinic.domain.auth;

import java.security.Principal;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import reactor.core.publisher.Mono;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller(value = "/auth")
public class AuthController {

  @Produces(MediaType.TEXT_PLAIN)
  @Get("/username")
  public String getAuthenticatedUsername(Principal principal) {
    return principal.getName();
  }

  @Delete(value = "/logout", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  Mono<HttpResponse<Void>> logout() {
    return Mono.just(HttpResponse.noContent());
  }
}
