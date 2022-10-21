package com.anasdidi.clinic.domain.auth;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.anasdidi.clinic.common.BaseDataFetcher;
import com.anasdidi.clinic.common.CommonConstants;
import com.anasdidi.clinic.common.SearchDTO;
import com.anasdidi.clinic.domain.user.UserDTO;

import graphql.schema.DataFetcher;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

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
      Boolean isDeleted = env.getArgument("isDeleted");
      Mono<Page<AuthDAO>> search = isDeleted != null
          ? authRepository.findAllByIsDeleted(isDeleted, pageable)
          : authRepository.findAll(pageable);

      return search.map(result -> {
        List<AuthDTO> resultList = result.getContent().stream().map(AuthUtils::copy).collect(Collectors.toList());
        return SearchDTO.<AuthDTO>builder()
            .resultList(resultList)
            .pagination(new SearchDTO.PaginationDTO(result))
            .build();
      }).toFuture();
    };
  }

  public DataFetcher<CompletableFuture<UserDTO>> getUserId() {
    return (env) -> {
      AuthDTO source = env.getSource();
      return env.<String, UserDTO>getDataLoader(CommonConstants.GraphQL.DataLoader.User.key)
          .load(source.getUserId(), env.getContext());
    };
  }
}
