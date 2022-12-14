package com.anasdidi.clinic.domain.auth;

import java.util.UUID;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface AuthRepository extends ReactorPageableRepository<AuthDAO, UUID> {

  Mono<AuthDAO> findByRefreshToken(String refreshToken);

  Mono<Page<AuthDAO>> findAllByIsDeleted(Boolean isDeleted, Pageable pageable);

  Mono<Long> deleteByUserId(String userId);
}
