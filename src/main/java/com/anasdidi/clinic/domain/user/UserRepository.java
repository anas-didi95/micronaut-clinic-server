package com.anasdidi.clinic.domain.user;

import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface UserRepository extends ReactiveStreamsCrudRepository<UserDAO, String> {

  Mono<Boolean> existsById(String id);
}
