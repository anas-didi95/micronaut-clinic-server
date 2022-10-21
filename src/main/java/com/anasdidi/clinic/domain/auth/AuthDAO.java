package com.anasdidi.clinic.domain.auth;

import java.util.UUID;

import com.anasdidi.clinic.common.BaseDAO;

import io.micronaut.data.annotation.AutoPopulated;
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
@ToString(callSuper = true)
@MappedEntity(value = "t_auth")
public class AuthDAO extends BaseDAO {

  @Id
  @MappedProperty(value = "id")
  @AutoPopulated(updateable = false)
  private UUID id;

  @MappedProperty(value = "user_id")
  private String userId;

  @MappedProperty(value = "refresh_token")
  private String refreshToken;
}
