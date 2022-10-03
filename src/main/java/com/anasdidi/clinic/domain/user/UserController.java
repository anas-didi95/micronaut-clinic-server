package com.anasdidi.clinic.domain.user;

import com.anasdidi.clinic.exception.ErrorResponseDTO;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Put;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import reactor.core.publisher.Mono;

@Secured(SecurityRule.IS_AUTHENTICATED)
interface UserController {

  @Post(value = "/", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  @Tag(name = "user")
  @Operation(description = "Create User")
  @ApiResponse(responseCode = "201", description = "Created", content = @Content(schema = @Schema(type = "object", allOf = UserDTO.class), examples = {
      @ExampleObject("""
          {
             "id": "anas",
             "password": "password",
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
            "fullName": "Anas Juwaidi",
            "password": "p@ssw0rd"
          }
          """) })) @Body UserDTO requestBody);

  @Put(value = "/{id}", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  @Tag(name = "user")
  @Operation(description = "Update User")
  @ApiResponse(responseCode = "200", description = "Ok", content = @Content(schema = @Schema(type = "object", allOf = UserDTO.class), examples = {
      @ExampleObject("""
          {
            "id": "anas",
            "updatedBy": "SYSTEM",
            "updatedDate": 1661084378.897929437,
            "version": 1,
            "fullName": "Anas Juwaidi Mohd Jeffry"
          }
          """) }))
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "object", allOf = ErrorResponseDTO.class), examples = {
      @ExampleObject("""
          {
            "code": "E003",
            "message": "Record [anas] not found!",
            "traceId": "220821122013X0nu6YXL"
          }
          """) }))
  Mono<HttpResponse<UserDTO>> updateUser(@PathVariable String id, @RequestBody(content = @Content(examples = {
      @ExampleObject("""
          {
            "id": "anas",
            "fullName": "Anas Juwaidi Mohd Jeffry",
            "version": 0
          }
          """) })) @Body UserDTO requestBody);

  @Delete(value = "/{id}", consumes = { MediaType.APPLICATION_JSON }, produces = { MediaType.APPLICATION_JSON })
  @Tag(name = "user")
  @Operation(description = "Delete User")
  @ApiResponse(responseCode = "204", description = "No Content")
  @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(type = "object", allOf = ErrorResponseDTO.class), examples = {
      @ExampleObject("""
          {
            "code": "E003",
            "message": "Record [anas] not found!",
            "traceId": "220821122013X0nu6YXL"
          }
          """) }))
  Mono<HttpResponse<Void>> deleteUser(@PathVariable String id, @RequestBody(content = @Content(examples = {
      @ExampleObject("""
          {
            "id": "anas",
            "version": 1
          }
          """) })) @Body UserDTO requestBody);
}
