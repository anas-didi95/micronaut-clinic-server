package com.anasdidi.clinic.domain.user;

import com.anasdidi.clinic.common.BaseDAO;

import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ToString
@MappedEntity(value = "t_user")
class UserDAO extends BaseDAO {

  @Id
  @MappedProperty(value = "id")
  private String id;

  @MappedProperty(value = "name")
  private String name;
}
