package com.anasdidi.clinic.domain.auth;

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
public class AuthDataFetcher extends BaseDataFetcher {

  private final AuthRepository authRepository;

  @Inject
  public AuthDataFetcher(AuthRepository authRepository) {
    this.authRepository = authRepository;
  }

  public DataFetcher<CompletableFuture<SearchDTO<AuthDTO>>> getAuthSearch() {
    return (env) -> {
      Pageable pageable = getPageable(env);
      return authRepository.findAll(pageable).map(result -> {
        List<AuthDTO> resultList = result.getContent().stream().map(AuthUtils::copy).collect(Collectors.toList());
        return SearchDTO.<AuthDTO>builder()
            .resultList(resultList)
            .pagination(new SearchDTO.PaginationDTO(result))
            .build();
      }).toFuture();
    };
  }
}
