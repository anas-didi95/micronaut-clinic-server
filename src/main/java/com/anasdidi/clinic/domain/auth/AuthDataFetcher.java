package com.anasdidi.clinic.domain.auth;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.anasdidi.clinic.common.SearchDTO;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class AuthDataFetcher {

  private final AuthRepository authRepository;

  @Inject
  public AuthDataFetcher(AuthRepository authRepository) {
    this.authRepository = authRepository;
  }

  public DataFetcher<CompletableFuture<SearchDTO<AuthDTO>>> getAuthSearch() {
    return (env) -> {
      int page = getSearchPage(env);
      int size = getSearchSize(env);
      Pageable pageable = Pageable.from(page - 1, size);
      return authRepository.findAll(pageable).map(result -> {
        List<AuthDTO> resultList = result.getContent().stream().map(AuthUtils::copy).collect(Collectors.toList());
        return SearchDTO.<AuthDTO>builder()
            .resultList(resultList)
            .pagination(new SearchDTO.PaginationDTO(result))
            .build();
      }).toFuture();
    };
  }

  private int getSearchPage(DataFetchingEnvironment env) {
    int page = env.getArgument("page");
    return Math.max(page, 1);
  }

  private int getSearchSize(DataFetchingEnvironment env) {
    int size = env.getArgument("size");
    return Math.max(size, 1);
  }
}
