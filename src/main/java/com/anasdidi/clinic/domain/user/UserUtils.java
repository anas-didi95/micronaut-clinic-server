package com.anasdidi.clinic.domain.user;

final class UserUtils {

  static UserDAO copy(UserDTO in) {
    UserDAO out = UserDAO.builder()
        .id(in.getId())
        .fullName(in.getFullName())
        .build();
    out.setCreatedBy(in.getCreatedBy());
    out.setCreatedDate(in.getCreatedDate());
    out.setUpdatedBy(in.getUpdatedBy());
    out.setUpdatedDate(in.getUpdatedDate());
    out.setVersion(in.getVersion());
    return out;
  }

  static UserDTO copy(UserDAO in) {
    UserDTO out = UserDTO.builder()
        .id(in.getId())
        .fullName(in.getFullName())
        .build();
    out.setCreatedBy(in.getCreatedBy());
    out.setCreatedDate(in.getCreatedDate());
    out.setUpdatedBy(in.getUpdatedBy());
    out.setUpdatedDate(in.getUpdatedDate());
    out.setVersion(in.getVersion());
    return out;
  }
}
