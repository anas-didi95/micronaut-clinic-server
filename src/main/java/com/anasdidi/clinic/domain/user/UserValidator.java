package com.anasdidi.clinic.domain.user;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
class UserValidator {

  private static final Logger logger = LoggerFactory.getLogger(UserValidator.class);
  private final Validator validator;

  @Inject
  UserValidator(Validator validator) {
    this.validator = validator;
  }

  Mono<UserDAO> test(UserDAO dao) {
    return Mono.defer(() -> {
      Set<ConstraintViolation<UserDAO>> errorSet = validator.validate(dao);

      logger.debug("[test] errorSet.size={}", errorSet.size());

      if (!errorSet.isEmpty()) {
        return Mono.error(new ConstraintViolationException(errorSet));
      }

      return Mono.just(dao);
    });
  }
}
