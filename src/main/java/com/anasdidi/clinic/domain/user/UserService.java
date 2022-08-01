package com.anasdidi.clinic.domain.user;

import reactor.core.publisher.Mono;

public interface UserService {

  Mono<UserDTO> create(UserDAO domain);
}
