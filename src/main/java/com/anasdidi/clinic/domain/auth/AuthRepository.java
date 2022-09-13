package com.anasdidi.clinic.domain.auth;

import java.util.UUID;

import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorCrudRepository;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface AuthRepository extends ReactorCrudRepository<AuthDAO, UUID> {

  Mono<AuthDAO> findByRefreshToken(String refreshToken);
}
