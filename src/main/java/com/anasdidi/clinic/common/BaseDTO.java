package com.anasdidi.clinic.common;

import java.time.Instant;

import org.apache.commons.lang3.StringUtils;

import graphql.schema.DataFetchingEnvironment;
import io.micronaut.core.annotation.Introspected;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Introspected
@Getter
@Setter
@ToString
public abstract class BaseDTO implements IRecordMetadata {

  private Boolean isDeleted;
  private Instant createdDate;
  private String createdBy;
  private Instant updatedDate;
  private String updatedBy;
  private Long version;

  public String getCreatedDate(DataFetchingEnvironment env) {
    String format = env.getArgument("format");
    if (StringUtils.isBlank(format)) {
      return createdDate.toString();
    }
    return CommonUtils.getFormattedInstant(createdDate, format);
  }

  public String getUpdatedDate(DataFetchingEnvironment env) {
    String format = env.getArgument("format");
    if (StringUtils.isBlank(format)) {
      return updatedDate.toString();
    }
    return CommonUtils.getFormattedInstant(updatedDate, format);
  }
}
