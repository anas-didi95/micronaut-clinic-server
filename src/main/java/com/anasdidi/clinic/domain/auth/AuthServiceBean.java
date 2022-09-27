package com.anasdidi.clinic.domain.auth;

import java.security.Principal;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anasdidi.clinic.common.CommonUtils;
import com.anasdidi.clinic.exception.RecordNotFoundException;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

@Singleton
public class AuthServiceBean implements AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthServiceBean.class);
  private final AuthRepository authRepository;

  @Inject
  AuthServiceBean(AuthRepository authRepository) {
    this.authRepository = authRepository;
  }

  @Override
  public void persistToken(RefreshTokenGeneratedEvent event) {
    if (event == null || event.getAuthentication() == null || event.getRefreshToken() == null) {
      logger.info("No refresh token generated event");
      return;
    }
    authRepository.save(AuthDAO.builder()
        .userId(event.getAuthentication().getName())
        .refreshToken(event.getRefreshToken()).build()).subscribe();
  }

  @Override
  public Publisher<Authentication> getAuthentication(String refreshToken) {
    return Flux.create(emitter -> {
      authRepository.findByRefreshToken(refreshToken)
          .switchIfEmpty(Mono.error(new RecordNotFoundException(CommonUtils.generateTraceId(), refreshToken)))
          .subscribe(auth -> {
            if (auth.getIsDeleted()) {
              emitter.error(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT,
                  "refresh token revoked", null));
            } else {
              emitter.next(Authentication.build(auth.getUserId()));
              emitter.complete();
            }
          }, error -> emitter.error(new OauthErrorResponseException(IssuingAnAccessTokenErrorCode.INVALID_GRANT,
              "refresh token not found", null)));
    }, FluxSink.OverflowStrategy.ERROR);
  }

  @Override
  public Mono<Long> logout(Principal principal, String traceId) {
    logger.debug("[{}:logout] principal.name={}", traceId, principal.getName());

    return Mono.just(principal)
        .map(p -> p.getName())
        .flatMap(authRepository::deleteByUserId)
        .doOnError(error -> logger.error("[{}:logout] principal.name={}", traceId, principal.getName()));
  }
}
