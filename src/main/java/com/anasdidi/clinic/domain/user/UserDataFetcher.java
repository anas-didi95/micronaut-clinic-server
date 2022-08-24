package com.anasdidi.clinic.domain.user;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.schema.DataFetcher;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class UserDataFetcher {

  private final Logger logger = LoggerFactory.getLogger(UserDataFetcher.class);
  private final UserRepository userRepository;

  @Inject
  public UserDataFetcher(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public DataFetcher<CompletableFuture<List<UserDTO>>> userList() {
    return (env) -> {
      logger.debug("[{}:userList] START", env.getExecutionId());
      return userRepository.findAll().map(UserUtils::copy).collectList().toFuture()
          .whenComplete((resultList, error) -> {
            logger.debug("[{}:userList] END: resultList.size={}", env.getExecutionId(), resultList.size());
          });
    };
  }
}
