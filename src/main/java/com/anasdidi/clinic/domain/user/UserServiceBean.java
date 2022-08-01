package com.anasdidi.clinic.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
class UserServiceBean implements UserService {

  private static final Logger logger = LoggerFactory.getLogger(UserServiceBean.class);
  private final UserRepository userRepository;

  @Inject
  UserServiceBean(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Mono<UserDTO> create(UserDAO domain) {
    domain.setIsDeleted(false);
    domain.setCreatedBy("SYSTEM");

    logger.debug("[create] domain={}", domain);

    return Mono.from(userRepository.save(domain))
        .map(result -> UserDTO.builder().id(result.getId()).build());
  }
}
