package com.anasdidi.clinic.common;

import java.util.Iterator;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import javax.validation.Validator;

import com.anasdidi.clinic.exception.ValidationException;

import reactor.core.publisher.Mono;

public abstract class BaseValidator<T extends BaseDAO> {

  private final Validator validator;

  protected BaseValidator(Validator validator) {
    this.validator = validator;
  }

  public Mono<T> validate(T dao) {
    return Mono.defer(() -> {
      Set<ConstraintViolation<T>> errorSet = validator.validate(dao);

      if (!errorSet.isEmpty()) {
        return Mono.error(
            new ValidationException(errorSet.stream()
                .map(this::buildMessage)
                .collect(Collectors.toList())));
      }

      return Mono.just(dao);
    });
  }

  protected String buildMessage(ConstraintViolation<T> violation) {
    Path propertyPath = violation.getPropertyPath();
    StringBuilder message = new StringBuilder();
    Iterator<Path.Node> i = propertyPath.iterator();

    while (i.hasNext()) {
      Path.Node node = i.next();

      if (node.getKind() == ElementKind.METHOD || node.getKind() == ElementKind.CONSTRUCTOR) {
        continue;
      }

      message.append(node.getName());

      if (node.getIndex() != null) {
        message.append(String.format("[%d]", node.getIndex()));
      }

      if (i.hasNext()) {
        message.append('.');
      }
    }

    message.append(": ").append(violation.getMessage());

    return message.toString();
  }
}
