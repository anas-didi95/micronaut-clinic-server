package com.anasdidi.clinic.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/person")
public class PersonController {

  private static final Logger logger = LoggerFactory.getLogger(PersonController.class);
  private final PersonRepository personRepository;

  public PersonController(PersonRepository personRepository) {
    this.personRepository = personRepository;
  }

  @Get
  Flux<Person> all() {
    return personRepository.findAll();
  }

  @Get("/{id}")
  Mono<Person> get(@PathVariable Long id) {
    return personRepository.findById(id).doOnSuccess(responseBody -> {
      logger.debug("[get] responseBody: {}", responseBody);
    });
  }
}
