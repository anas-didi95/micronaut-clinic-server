package com.anasdidi.clinic.config;

import io.micronaut.context.annotation.Factory;
import io.micronaut.security.authentication.AuthenticationProvider;
import io.micronaut.security.authentication.AuthenticationResponse;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

@Factory
public class AuthFactory {

  @Singleton
  public AuthenticationProvider authenticationProvider() {
    return (httpRequest, authenticationRequest) -> {
      return Flux.create(emitter -> {
        if (authenticationRequest.getIdentity().equals("sherlock") &&
            authenticationRequest.getSecret().equals("password")) {
          emitter.next(AuthenticationResponse
              .success((String) authenticationRequest.getIdentity()));
          emitter.complete();
        } else {
          emitter.error(AuthenticationResponse.exception());
        }
      }, FluxSink.OverflowStrategy.ERROR);
    };
  }
}
