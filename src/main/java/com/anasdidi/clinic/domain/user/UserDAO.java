package com.anasdidi.clinic.domain.user;

import java.time.Instant;

import com.anasdidi.clinic.common.BaseDAO;

import io.micronaut.data.annotation.DateCreated;
import io.micronaut.data.annotation.DateUpdated;
import io.micronaut.data.annotation.Id;
import io.micronaut.data.annotation.MappedEntity;
import io.micronaut.data.annotation.MappedProperty;
import io.micronaut.data.annotation.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = false)
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@MappedEntity(value = "t_user")
class UserDAO extends BaseDAO {

  @Id
  @MappedProperty(value = "id")
  private String id;

  @MappedProperty(value = "full_name")
  private String fullName;

  @MappedProperty(value = "is_del")
  private Boolean isDeleted;

  @DateCreated
  @MappedProperty(value = "created_dt")
  private Instant createdDate;

  @MappedProperty(value = "created_by")
  private String createdBy;

  @DateUpdated
  @MappedProperty(value = "updated_dt")
  private Instant updatedDate;

  @MappedProperty(value = "updated_by")
  private String updatedBy;

  @Version
  @MappedProperty(value = "version")
  private Long version;
}
