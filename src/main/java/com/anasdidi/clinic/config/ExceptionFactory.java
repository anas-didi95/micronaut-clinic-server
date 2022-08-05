package com.anasdidi.clinic.config;

import com.anasdidi.clinic.common.CommonConstants;
import com.anasdidi.clinic.common.ResponseDTO;
import com.anasdidi.clinic.exception.RecordAlreadyExistsException;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import jakarta.inject.Singleton;

@Factory
public class ExceptionFactory {

  @Singleton
  @Requires(classes = { RecordAlreadyExistsException.class })
  public ExceptionHandler<RecordAlreadyExistsException, HttpResponse<ResponseDTO>> recordAlreadyExistsHandler() {
    CommonConstants.Error error = CommonConstants.Error.RECORD_ALREADY_EXISTS;
    return (request, exception) -> {
      return HttpResponse.status(HttpStatus.BAD_REQUEST)
          .body(ResponseDTO.builder()
              .code(error.code)
              .message(error.message.formatted(exception.getId())).build());
    };
  }
}
