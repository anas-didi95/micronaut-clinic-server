package com.anasdidi.clinic.domain.user;

import reactor.core.publisher.Mono;

public interface UserService {

  Mono<UserDTO> createUser(UserDAO domain);

  Mono<UserDTO> updataUser(String id, UserDAO domain);
}
