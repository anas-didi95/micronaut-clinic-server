package com.anasdidi.clinic.domain.auth;

import com.anasdidi.clinic.common.CommonUtils;

final class AuthUtils {

  static AuthDAO copy(AuthDTO in) {
    AuthDAO out = AuthDAO.builder()
        .id(in.getId())
        .userId(in.getUserId())
        .build();
    return (AuthDAO) CommonUtils.copy(in, out);
  }

  static AuthDTO copy(AuthDAO in) {
    AuthDTO out = AuthDTO.builder()
        .id(in.getId())
        .userId(in.getUserId())
        .build();
    return (AuthDTO) CommonUtils.copy(in, out);
  }
}
