package com.anasdidi.clinic.config;

import com.anasdidi.clinic.exception.RecordAlreadyExistsException;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Factory
public class ExceptionFactory {

  private final ErrorResponseProcessor<?> processor;

  @Inject
  public ExceptionFactory(ErrorResponseProcessor<?> processor) {
    this.processor = processor;
  }

  @Singleton
  @Requires(classes = { RecordAlreadyExistsException.class })
  public ExceptionHandler<RecordAlreadyExistsException, HttpResponse<?>> recordAlreadyExistsHandler() {
    return (request, exception) -> {
      String message = "Record [%s] not found!".formatted(exception.getId());
      return processor.processResponse(
          ErrorContext.builder(request).cause(exception).errorMessage(message).build(),
          HttpResponse.badRequest());
    };
  }
}
