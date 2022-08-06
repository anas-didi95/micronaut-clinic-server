package com.anasdidi.clinic.config;

import com.anasdidi.clinic.common.CommonConstants;
import com.anasdidi.clinic.common.ResponseDTO;
import com.anasdidi.clinic.exception.RecordAlreadyExistsException;
import com.anasdidi.clinic.exception.ValidationException;

import io.micronaut.context.LocalizedMessageSource;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Factory
public class ExceptionFactory {

  private final LocalizedMessageSource messageSource;

  @Inject
  public ExceptionFactory(LocalizedMessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Singleton
  @Requires(classes = { ValidationException.class })
  public ExceptionHandler<ValidationException, HttpResponse<ResponseDTO>> validationErrorHandler() {
    CommonConstants.Error error = CommonConstants.Error.VALIDATION_ERROR;
    return (request, exception) -> {
      return HttpResponse.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
          .code(error.code)
          .message(getErrorMessage(error))
          .errorList(exception.getErrorList())
          .build());
    };
  }

  @Singleton
  @Requires(classes = { RecordAlreadyExistsException.class })
  public ExceptionHandler<RecordAlreadyExistsException, HttpResponse<ResponseDTO>> recordAlreadyExistsHandler() {
    CommonConstants.Error error = CommonConstants.Error.RECORD_ALREADY_EXISTS;
    return (request, exception) -> {
      return HttpResponse.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
          .code(error.code)
          .message(getErrorMessage(error, exception.getId()))
          .build());
    };
  }

  private String getErrorMessage(CommonConstants.Error error, Object... variables) {
    return messageSource
        .getMessage("message.error.%s".formatted(error.code), variables)
        .orElse("Error Code [%s]!".formatted(error.code));
  }
}
