package com.anasdidi.clinic.config;

import com.anasdidi.clinic.common.CommonUtils;
import com.anasdidi.clinic.domain.user.UserService;

import io.micronaut.context.annotation.Factory;
import io.micronaut.security.authentication.AuthenticationFailureReason;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Factory
public class AuthFactory {

  @Singleton
  public AuthenticationProvider authenticationProvider(UserService userService) {
    return (httpRequest, authenticationRequest) -> {
      return Flux.create(emitter -> {
        userService
            .getUserById((String) authenticationRequest.getIdentity(), CommonUtils.generateTraceId())
            .subscribe(user -> {
              if (CommonUtils.getPasswordEncoder()
                  .matches((String) authenticationRequest.getSecret(), user.getPassword())) {
                emitter.next(AuthenticationResponse
                    .success((String) authenticationRequest.getIdentity()));
                emitter.complete();
              } else {
                emitter.error(AuthenticationResponse.exception(AuthenticationFailureReason.CREDENTIALS_DO_NOT_MATCH));
              }
            }, error -> emitter.error(error));
      }, FluxSink.OverflowStrategy.ERROR);
    };
  }
}
