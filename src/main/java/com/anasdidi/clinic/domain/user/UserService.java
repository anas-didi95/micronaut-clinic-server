package com.anasdidi.clinic.domain.user;

import reactor.core.publisher.Mono;

public interface UserService {

  Mono<UserDTO> createUser(UserDAO dao, String traceId);

  Mono<UserDTO> updataUser(String id, UserDAO dao, String traceId);

  Mono<Long> deleteUser(String id, UserDAO dao, String traceId);

  Mono<UserDTO> getUserById(String id, String traceId);
}
