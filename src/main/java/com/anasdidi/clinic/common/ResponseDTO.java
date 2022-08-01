package com.anasdidi.clinic.common;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class ResponseDTO extends BaseDTO {

  private final String id;
  private final String status;
}
