package com.anasdidi.clinic.client;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.security.authentication.UsernamePasswordCredentials;
import io.micronaut.security.token.jwt.render.BearerAccessRefreshToken;

@Client("/clinic")
public interface AppClient {

  @Post("/login")
  BearerAccessRefreshToken login(@Body UsernamePasswordCredentials credentials);
}
