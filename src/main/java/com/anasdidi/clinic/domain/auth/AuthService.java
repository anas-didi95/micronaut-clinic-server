package com.anasdidi.clinic.domain.auth;

import java.security.Principal;

import io.micronaut.security.token.refresh.RefreshTokenPersistence;
import reactor.core.publisher.Mono;

public interface AuthService extends RefreshTokenPersistence {

  Mono<Long> logout(Principal principal, String traceId);
}
