package com.anasdidi.clinic.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anasdidi.clinic.exception.RecordAlreadyExistsException;

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
  public Mono<UserDTO> createUser(UserDAO domain) {
    domain.setIsDeleted(false);
    domain.setCreatedBy("SYSTEM");

    logger.debug("[createUser] domain={}", domain);

    Mono<Void> check = userRepository.existsById(domain.getId()).flatMap(result -> {
      if (result) {
        logger.error("[createUser] domain={}", domain);
        return Mono.error(new RecordAlreadyExistsException(domain.getId()));
      }
      return Mono.empty();
    });
    Mono<UserDTO> save = userRepository.save(domain)
        .map(result -> UserDTO.builder().id(result.getId()).build());

    return check.then(save);
  }

  @Override
  public Mono<UserDTO> updataUser(String id, UserDAO domain) {
    domain.setUpdatedBy("SYSTEM");

    logger.debug("[updateUser] domain={}", domain);

    return userRepository.update(domain)
        .map(result -> UserDTO.builder().id(result.getId()).build());
  }
}
