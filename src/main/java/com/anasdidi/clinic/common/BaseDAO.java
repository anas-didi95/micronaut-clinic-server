package com.anasdidi.clinic.common;

import java.time.Instant;

import javax.validation.constraints.NotBlank;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.annotation.Version;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Introspected
@Getter
@Setter
@ToString
public abstract class BaseDAO implements IRecordMetadata {

  @Id
  @MappedProperty(value = "id")
  @NotBlank
  private String id;

  @MappedProperty(value = "is_del")
  private Boolean isDeleted;

  @DateCreated
  @MappedProperty(value = "created_dt")
  private Instant createdDate;

  @MappedProperty(value = "created_by")
  private String createdBy;

  @DateUpdated
  @MappedProperty(value = "updated_dt")
  private Instant updatedDate;

  @MappedProperty(value = "updated_by")
  private String updatedBy;

  @Version
  @MappedProperty(value = "version")
  private Long version;
}
