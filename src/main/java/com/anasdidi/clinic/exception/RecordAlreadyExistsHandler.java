package com.anasdidi.clinic.exception;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@Requires(classes = { RecordAlreadyExistsException.class, ExceptionHandler.class })
public class RecordAlreadyExistsHandler implements ExceptionHandler<RecordAlreadyExistsException, HttpResponse<?>> {

  private final ErrorResponseProcessor<?> processor;

  @Inject
  public RecordAlreadyExistsHandler(ErrorResponseProcessor<?> processor) {
    this.processor = processor;
  }

  @Override
  @SuppressWarnings("rawtypes")
  public HttpResponse<?> handle(HttpRequest request, RecordAlreadyExistsException exception) {
    String errorMessage = "Record [%s] not found!".formatted(exception.getId());
    return processor.processResponse(
        ErrorContext.builder(request).errorMessage(errorMessage).build(),
        HttpResponse.badRequest());
  }
}
