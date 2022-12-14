package com.anasdidi.clinic.domain.user;

import javax.validation.constraints.NotBlank;

import com.anasdidi.clinic.common.BaseDAO;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
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
@MappedEntity(value = "t_user")
class UserDAO extends BaseDAO {

  @Id
  @MappedProperty(value = "id")
  private String id;

  @MappedProperty(value = "password")
  private String password;

  @MappedProperty(value = "full_name")
  @NotBlank
  private String fullName;
}
