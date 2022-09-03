package com.anasdidi.clinic.domain.user;

import com.anasdidi.clinic.common.BaseValidator;

import io.micronaut.validation.validator.Validator;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
class UserValidator extends BaseValidator<UserDAO> {

  @Inject
  UserValidator(Validator validator) {
    super(validator);
  }
}
