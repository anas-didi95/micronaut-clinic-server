package com.anasdidi.clinic.example;

import io.micronaut.data.model.query.builder.sql.Dialect;
import io.micronaut.data.r2dbc.annotation.R2dbcRepository;
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@R2dbcRepository(dialect = Dialect.POSTGRES)
public interface PersonRepository extends ReactiveStreamsCrudRepository<Person, Long> {

  Mono<Person> findById(Long id);

  Flux<Person> findAll();
}
