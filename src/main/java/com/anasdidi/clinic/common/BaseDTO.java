package com.anasdidi.clinic.common;

import java.time.Instant;

import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Introspected
@Getter
@Setter
@ToString
public abstract class BaseDTO implements IRecordMetadata {

  private String id;
  private Boolean isDeleted;
  private Instant createdDate;
  private String createdBy;
  private Instant updatedDate;
  private String updatedBy;
  private Long version;
}
