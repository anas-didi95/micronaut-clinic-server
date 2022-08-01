package com.anasdidi.clinic.common;

import java.time.Instant;

import io.micronaut.core.annotation.Introspected;

@Introspected
public abstract class BaseDTO {

  protected Boolean isDeleted;
  protected Instant createdDate;
  protected String createdBy;
  protected Instant updatedDate;
  protected String updatedBy;
  protected Long version;

  public Boolean getIsDeleted() {
    return isDeleted;
  }

  public void setIsDeleted(Boolean isDeleted) {
    this.isDeleted = isDeleted;
  }

  public Instant getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Instant createdDate) {
    this.createdDate = createdDate;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public Instant getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Instant updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getUpdatedBy() {
    return updatedBy;
  }

  public void setUpdatedBy(String updatedBy) {
    this.updatedBy = updatedBy;
  }

  public Long getVersion() {
    return version;
  }

  public void setVersion(Long version) {
    this.version = version;
  }

}
