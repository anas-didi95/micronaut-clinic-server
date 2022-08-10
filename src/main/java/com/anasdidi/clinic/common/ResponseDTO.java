package com.anasdidi.clinic.common;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@ToString
public class ResponseDTO extends BaseDTO {

  private String id;
  private String status;
  private String code;
  private String message;
  private Collection<?> errorList;
  private String traceId;
}
