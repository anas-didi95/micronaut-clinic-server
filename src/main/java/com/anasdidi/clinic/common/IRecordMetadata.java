package com.anasdidi.clinic.common;

import java.time.Instant;

public interface IRecordMetadata {

  String getId();

  void setId(String id);

  Boolean getIsDeleted();

  void setIsDeleted(Boolean isDeleted);

  Instant getCreatedDate();

  void setCreatedDate(Instant createdDate);

  String getCreatedBy();

  void setCreatedBy(String createdBy);

  Instant getUpdatedDate();

  void setUpdatedDate(Instant updatedDate);

  String getUpdatedBy();

  void setUpdatedBy(String updatedBy);

  Long getVersion();

  void setVersion(Long version);
}
