package com.anasdidi.clinic.domain.auth;

import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.errors.IssuingAnAccessTokenErrorCode;
import io.micronaut.security.errors.OauthErrorResponseException;
import io.micronaut.security.token.event.RefreshTokenGeneratedEvent;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

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
      authRepository.findByRefreshToken(refreshToken).subscribe(auth -> {
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
}
