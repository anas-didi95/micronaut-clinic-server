package com.anasdidi.clinic.domain.user;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.anasdidi.clinic.common.BaseDataFetcher;
import com.anasdidi.clinic.common.SearchDTO;

import graphql.schema.DataFetcher;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class UserDataFetcher extends BaseDataFetcher {

  private final UserRepository userRepository;

  @Inject
  public UserDataFetcher(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public DataFetcher<CompletableFuture<SearchDTO<UserDTO>>> getUserSearch() {
    return (env) -> {
      Pageable pageable = getPageable(env);
      return userRepository.findAll(pageable).map(result -> {
        List<UserDTO> resultList = result.getContent().stream().map(UserUtils::copy).collect(Collectors.toList());
        return SearchDTO.<UserDTO>builder()
            .resultList(resultList)
            .pagination(new SearchDTO.PaginationDTO(result))
            .build();
      }).toFuture();
    };
  }

  public DataFetcher<CompletableFuture<UserDTO>> getUser() {
    return (env) -> {
      String id = env.getArgument("id");
      return userRepository.findById(id).map(UserUtils::copy).toFuture();
    };
  }
}
