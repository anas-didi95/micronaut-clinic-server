package com.anasdidi.clinic.domain.auth;

import java.security.Principal;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;

@Secured(SecurityRule.IS_AUTHENTICATED)
@Controller
public class AuthController {

  @Produces(MediaType.TEXT_PLAIN)
  @Get
  public String index(Principal principal) {
    return principal.getName();
  }
}
