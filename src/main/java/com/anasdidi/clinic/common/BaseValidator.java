package com.anasdidi.clinic.common;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import reactor.core.publisher.Mono;

public abstract class BaseValidator<T extends BaseDAO> {

  private static final Logger logger = LoggerFactory.getLogger(BaseValidator.class);
  private final Validator validator;

  protected BaseValidator(Validator validator) {
    this.validator = validator;
  }

  public Mono<T> validate(T dao) {
    return Mono.defer(() -> {
      Set<ConstraintViolation<T>> errorSet = validator.validate(dao);

      if (!errorSet.isEmpty()) {
        ConstraintViolationException ex = new ConstraintViolationException(errorSet);
        logger.error("[validate] dao={}", dao, ex);
        return Mono.error(ex);
      }

      return Mono.just(dao);
    });
  }
}
