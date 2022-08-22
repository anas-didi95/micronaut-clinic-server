package com.anasdidi.clinic.domain.user;

import com.anasdidi.clinic.exception.ErrorResponseDTO;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

interface UserController {

  @Post(value = "/", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  @Tag(name = "user")
  @Operation(description = "Create User")
  @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(type = "object", allOf = UserDTO.class), examples = {
      @ExampleObject("""
          {
             "id": "anas",
             "createdDate": 1661084378.897929437,
             "createdBy": "SYSTEM",
             "updatedDate": 1661084378.897929437,
             "version": 0,
             "fullName": "Anas Juwaidi"
          }
          """) }))
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "object", allOf = ErrorResponseDTO.class), examples = {
      @ExampleObject("""
          {
            "code": "E002",
            "message": "Record [anas] already exists!",
            "traceId": "220821122013X0nu6YXL"
          }
          """) }))
  Mono<HttpResponse<UserDTO>> createUser(@RequestBody(content = @Content(examples = {
      @ExampleObject("""
          {
            "id": "anas",
            "fullName": "Anas Juwaidi"
          }
          """) })) @Body UserDTO requestBody);

  @Put(value = "/{id}", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  @Operation(description = "Update User")
  @Tag(name = "user")
  @ApiResponse(responseCode = "200", description = "Ok")
  Mono<HttpResponse<UserDTO>> updateUser(@PathVariable String id, @Body UserDTO requestBody);

  @Delete(value = "/{id}", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  @Operation(description = "Delete User")
  @Tag(name = "user")
  @ApiResponse(responseCode = "204", description = "No Content")
  Mono<HttpResponse<Void>> deleteUser(@PathVariable String id, @Body UserDTO requestBody);
}
