package com.anasdidi.clinic.domain.user;

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
@ToString(callSuper = true, exclude = "password")
public class UserDTO extends BaseDTO {

  private String id;
  private String password;
  private String fullName;
}
