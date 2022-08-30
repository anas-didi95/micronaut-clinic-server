package com.anasdidi.clinic.domain.user;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anasdidi.clinic.common.SearchDTO;

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

  public DataFetcher<CompletableFuture<SearchDTO<UserDTO>>> getUserList() {
    return (env) -> {
      logger.debug("[{}:userList] START", env.getExecutionId());
      Pageable pageable = Pageable.from(0, 200);
      return userRepository.findAll(pageable).map(result -> {
        Map<String, Object> pagination = Map.of("totalPages", result.getTotalPages());
        logger.debug("pagination={}", pagination);
        logger.debug("getNumberOfElements={}", result.getNumberOfElements());
        logger.debug("totalPages={}", result.getTotalPages());
        logger.debug("pageNumber={}", result.getPageNumber());
        logger.debug("getSize={}", result.getSize());
        logger.debug("getPageable().getNumber={}", result.getPageable().getNumber());
        logger.debug("getPageable().getSize={}", result.getPageable().getSize());
        GraphQLContext context = env.getContext();
        System.out.println("eontext.test=" + context.get("test"));
        context.put("test", "hello world 1");
        context.put("pagination", pagination);
        List<UserDTO> resultList = result.getContent().stream().map(UserUtils::copy).collect(Collectors.toList());
        return SearchDTO.<UserDTO>builder()
            .resultList(resultList)
            .pagination(SearchDTO.PaginationDTO.builder()
                .totalPages(result.getTotalPages())
                .build())
            .build();
      }).toFuture();
    };
  }
}
