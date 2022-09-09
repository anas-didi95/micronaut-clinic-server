package com.anasdidi.clinic.domain.user;

import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactorPageableRepository;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface UserRepository extends ReactorPageableRepository<UserDAO, String> {

  Mono<Boolean> existsById(String id);

  Mono<Page<UserDAO>> findAll(Pageable pageable);

  Mono<UserDAO> findById(String id);
}
