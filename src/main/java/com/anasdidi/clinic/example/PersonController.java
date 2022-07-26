package com.anasdidi.clinic.example;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller("/person")
public class PersonController {

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
    return personRepository.findById(id);
  }
}
