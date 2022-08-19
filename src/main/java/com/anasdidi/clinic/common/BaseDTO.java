package com.anasdidi.clinic.common;

import java.time.Instant;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;

@Introspected
@Getter
@Setter
public abstract class BaseDTO implements IRecordMetadata {

  protected String id;
  protected Boolean isDeleted;
  protected Instant createdDate;
  protected String createdBy;
  protected Instant updatedDate;
  protected String updatedBy;
  protected Long version;
}
