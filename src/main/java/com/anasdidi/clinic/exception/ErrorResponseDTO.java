package com.anasdidi.clinic.exception;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class ErrorResponseDTO {

  private String code;
  private String message;
  private Collection<?> errorList;
  private String traceId;
}
