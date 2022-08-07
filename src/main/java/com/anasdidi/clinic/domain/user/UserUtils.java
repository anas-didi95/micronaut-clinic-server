package com.anasdidi.clinic.domain.user;

final class UserUtils {

  static UserDAO copy(UserDTO dto) {
    return UserDAO.builder()
        .id(dto.getId())
        .fullName(dto.getFullName())
        .build();
  }
}
