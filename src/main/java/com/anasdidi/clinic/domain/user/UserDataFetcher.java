package com.anasdidi.clinic.domain.user;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import graphql.GraphQLContext;
import graphql.schema.DataFetcher;
import io.micronaut.data.model.Pageable;
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

  public DataFetcher<CompletableFuture<List<UserDTO>>> getUserList() {
    return (env) -> {
      logger.debug("[{}:userList] START", env.getExecutionId());
      Pageable pageable = Pageable.from(1, 2);
      return userRepository.findAll(pageable).map(result -> {
        Map<String, Object> pagination = Map.of("totalPages", result.getTotalPages());
        logger.debug("pagination={}", pagination);
        logger.debug("totalPages={}", result.getTotalPages());
        logger.debug("pageNumber={}", result.getPageNumber());
        logger.debug("getSize={}", result.getSize());
        logger.debug("getPageable().getNumber={}", result.getPageable().getNumber());
        logger.debug("getPageable().getSize={}", result.getPageable().getSize());
        GraphQLContext context = env.getContext();
        System.out.println("eontext.test=" + context.get("test"));
        context.put("test", "hello world 1");
        context.put("pagination", pagination);
        return result.getContent().stream().map(UserUtils::copy).collect(Collectors.toList());
      }).toFuture().whenComplete((resultList, error) -> {
        logger.debug("[{}:userList] END: resultList.size={}", env.getExecutionId(), resultList.size());
      });
    };
  }
}
