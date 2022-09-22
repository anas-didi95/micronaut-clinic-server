package com.anasdidi.clinic.domain.auth;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import com.anasdidi.clinic.common.BaseDataFetcher;
import com.anasdidi.clinic.common.CommonConstants.GQLContext;
import com.anasdidi.clinic.common.SearchDTO;
import com.anasdidi.clinic.domain.user.UserDTO;
import com.anasdidi.clinic.domain.user.UserService;

import graphql.schema.DataFetcher;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Singleton
public class AuthDataFetcher extends BaseDataFetcher {

  private final AuthRepository authRepository;
  private final UserService userService;

  @Inject
  public AuthDataFetcher(AuthRepository authRepository, UserService userService) {
    this.authRepository = authRepository;
    this.userService = userService;
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
      String traceId = (String) getContext(env, GQLContext.TRACE_ID);
      AuthDTO source = env.getSource();
      return userService.getUserById(source.getUserId(), traceId).toFuture();
    };
  }
}
