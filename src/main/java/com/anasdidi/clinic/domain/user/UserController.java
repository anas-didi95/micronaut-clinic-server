package com.anasdidi.clinic.domain.user;

import com.anasdidi.clinic.common.ResponseDTO;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import reactor.core.publisher.Mono;

interface UserController {

  @Post(value = "/", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  Mono<HttpResponse<ResponseDTO>> createUser(@Body UserDTO requestBody);

  @Put(value = "/{id}", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  Mono<HttpResponse<ResponseDTO>> updateUser(@PathVariable String id, @Body UserDTO requestBody);

  @Delete(value = "/{id}", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  Mono<HttpResponse<Void>> deleteUser(@PathVariable String id, @Body UserDTO requestBody);
}
