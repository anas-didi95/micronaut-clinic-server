package com.anasdidi.clinic.config;

import com.anasdidi.clinic.common.IRecordMetadata;

import io.micronaut.data.annotation.event.PrePersist;
import io.micronaut.data.annotation.event.PreUpdate;
import jakarta.inject.Singleton;

@Singleton
public class DatabaseConfig {

  @PrePersist
  void prePresist(IRecordMetadata record) {
    record.setCreatedBy("SYSTEM");
  }

  @PreUpdate
  void preUpdate(IRecordMetadata record) {
    record.setUpdatedBy("SYSTEM");
  }
}
