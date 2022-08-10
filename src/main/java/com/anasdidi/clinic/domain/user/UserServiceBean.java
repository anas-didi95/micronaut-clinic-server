package com.anasdidi.clinic.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anasdidi.clinic.exception.RecordAlreadyExistsException;
import com.anasdidi.clinic.exception.RecordNotFoundException;

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
  public Mono<UserDTO> createUser(UserDAO dao, String traceId) {
    dao.setIsDeleted(false);
    dao.setCreatedBy("SYSTEM");

    logger.debug("[{}:createUser] dao={}", traceId, dao);

    Mono<Void> check = userRepository.existsById(dao.getId()).flatMap(result -> {
      if (result) {
        logger.error("[{}:createUser] dao={}", traceId, dao);
        return Mono.error(new RecordAlreadyExistsException(traceId, dao.getId()));
      }
      return Mono.empty();
    });
    Mono<UserDTO> save = userRepository.save(dao)
        .map(result -> UserDTO.builder().id(result.getId()).build());

    return check.then(save);
  }

  @Override
  public Mono<UserDTO> updataUser(String id, UserDAO dao, String traceId) {
    dao.setUpdatedBy("SYSTEM");

    logger.debug("[{}:updateUser] dao={}", traceId, dao);

    Mono<UserDAO> check = userRepository.findById(id)
        .switchIfEmpty(Mono.defer(() -> {
          logger.error("[{}:updateUser] dao={}", traceId, dao);
          return Mono.error(new RecordNotFoundException(traceId, id));
        }));
    Mono<UserDTO> update = userRepository.update(dao)
        .map(result -> UserDTO.builder().id(result.getId()).build());

    return check.then(update);
  }
}
