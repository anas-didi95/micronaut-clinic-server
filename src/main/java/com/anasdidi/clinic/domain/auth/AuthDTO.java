package com.anasdidi.clinic.domain.auth;

import java.util.UUID;

import com.anasdidi.clinic.common.BaseDTO;

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
@ToString(callSuper = true)
public class AuthDTO extends BaseDTO {

  private UUID id;
  private String userId;
}
