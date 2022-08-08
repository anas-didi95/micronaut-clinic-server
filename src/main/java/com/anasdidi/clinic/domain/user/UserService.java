package com.anasdidi.clinic.domain.user;

import reactor.core.publisher.Mono;

public interface UserService {

  Mono<UserDTO> createUser(UserDAO dao);

  Mono<UserDTO> updataUser(String id, UserDAO dao);
}
