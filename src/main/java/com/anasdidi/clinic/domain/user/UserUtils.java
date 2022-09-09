package com.anasdidi.clinic.domain.user;

import org.apache.commons.lang3.StringUtils;

import com.anasdidi.clinic.common.CommonUtils;

final class UserUtils {

  static UserDAO copy(UserDTO in) {
    UserDAO out = UserDAO.builder()
        .id(in.getId())
        .password(in.getPassword())
        .fullName(in.getFullName())
        .build();
    return (UserDAO) CommonUtils.copy(in, out);
  }

  static UserDTO copy(UserDAO in) {
    UserDTO out = UserDTO.builder()
        .id(in.getId())
        .password(in.getPassword())
        .fullName(in.getFullName())
        .build();
    return (UserDTO) CommonUtils.copy(in, out);
  }

  static UserDAO merge(UserDAO db, UserDAO req) {
    String id = req.getId();
    String password = StringUtils.isBlank(req.getPassword()) ? db.getPassword() : req.getPassword();
    String fullName = req.getFullName();
    UserDAO result = new UserDAO(id, password, fullName);
    return (UserDAO) CommonUtils.copy(db, result);
  }
}
