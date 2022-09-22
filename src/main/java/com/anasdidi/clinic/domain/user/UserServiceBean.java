package com.anasdidi.clinic.domain.user;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anasdidi.clinic.common.CommonUtils;
import com.anasdidi.clinic.exception.RecordAlreadyExistsException;
import com.anasdidi.clinic.exception.RecordMetadataNotMatchedException;
import com.anasdidi.clinic.exception.RecordNotFoundException;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Flux;
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
    dao.setPassword(CommonUtils.getPasswordEncoder().encode(dao.getPassword()));

    logger.debug("[{}:createUser] dao={}", traceId, dao);

    Mono<Void> check = userRepository.existsById(dao.getId()).flatMap(result -> {
      if (result) {
        return Mono.error(new RecordAlreadyExistsException(traceId, dao.getId()));
      }
      return Mono.empty();
    });
    Mono<UserDTO> save = userRepository.save(dao)
        .map(UserUtils::copy);

    return check.then(save).doOnError(error -> logger.error("[{}:createUser] dao={}", traceId, dao));
  }

  @Override
  public Mono<UserDTO> updataUser(String id, UserDAO dao, String traceId) {
    logger.debug("[{}:updateUser] id={}, dao={}", traceId, id, dao);

    Mono<UserDAO> check = userRepository.findById(id)
        .switchIfEmpty(Mono.defer(() -> {
          return Mono.error(new RecordNotFoundException(traceId, id));
        }))
        .flatMap(db -> {
          if (!(db.getId().equals(dao.getId()) && db.getVersion() == dao.getVersion())) {
            return Mono.error(new RecordMetadataNotMatchedException(traceId, db.getId(), db.getVersion(), dao.getId(),
                dao.getVersion()));
          }
          return Mono.just(db);
        })
        .map(db -> UserUtils.merge(db, dao));
    Mono<UserDTO> update = check.flatMap(userRepository::update).map(UserUtils::copy);

    return update.doOnError(error -> logger.error("[{}:updateUser] id={}, dao={}", traceId, id, dao));
  }

  @Override
  public Mono<Long> deleteUser(String id, UserDAO dao, String traceId) {
    logger.debug("[{}:deleteUser] id={}, dao={}", traceId, id, dao);

    Mono<Void> check = userRepository.findById(id)
        .switchIfEmpty(Mono.defer(() -> Mono.error(new RecordNotFoundException(traceId, id))))
        .flatMap(db -> {
          if (!(db.getId().equals(dao.getId()) && db.getVersion() == dao.getVersion())) {
            return Mono.error(new RecordMetadataNotMatchedException(traceId, db.getId(), db.getVersion(), dao.getId(),
                dao.getVersion()));
          }
          return Mono.empty();
        });
    Mono<Long> delete = userRepository.deleteById(id);

    return check.then(delete).doOnError(error -> logger.error("[{}:deleteUser] id={}, dao={}", traceId, id, dao));
  }

  @Override
  public Mono<UserDTO> getUserById(String id, String traceId) {
    logger.debug("[{}:getUserById] id={} ", traceId, id);
    return userRepository.findById(id)
        .map(UserUtils::copy)
        .doOnError((error) -> logger.error("[{}:getUserById] id={} ", traceId, id));
  }

  @Override
  public Flux<UserDTO> getUsersByIdIn(Collection<String> ids, String traceId) {
    logger.debug("[{}:getUsersByIdIn] ids.size={}", traceId, ids.size());
    return userRepository.findByIdIn(ids)
        .map(UserUtils::copy)
        .doOnError((error) -> logger.error("[{}:getUsersByIdIn] ids.size={}", traceId, ids.size()));
  }
}
