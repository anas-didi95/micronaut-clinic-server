package com.anasdidi.clinic.domain.user;

import com.anasdidi.clinic.common.BaseDTO;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
public class UserDTO extends BaseDTO {

  private final String id;
  private final String fullName;
}
