package com.anasdidi.clinic.domain.user;

import com.anasdidi.clinic.common.CommonUtils;

final class UserUtils {

  static UserDAO copy(UserDTO in) {
    UserDAO out = UserDAO.builder()
        .fullName(in.getFullName())
        .build();
    return (UserDAO) CommonUtils.copy(in, out);
  }

  static UserDTO copy(UserDAO in) {
    UserDTO out = UserDTO.builder()
        .fullName(in.getFullName())
        .build();
    return (UserDTO) CommonUtils.copy(in, out);
  }

  static UserDAO merge(UserDAO db, UserDAO req) {
    String fullName = req.getFullName();
    UserDAO result = new UserDAO(fullName);
    return (UserDAO) CommonUtils.copy(db, result);
  }
}