package com.anasdidi.clinic.config;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import static io.micronaut.http.HttpStatus.OK;
import io.micronaut.http.client.BlockingHttpClient;
import io.micronaut.http.client.HttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
public class LiquibaseEndpointTests {

  @Inject
  @Client("/")
  HttpClient httpClient;

  @Test
  void migrationsAreExposedViaAndEndpoint() {
    BlockingHttpClient client = httpClient.toBlocking();

    HttpResponse<LiquibaseReport> response = client.exchange(
        HttpRequest.GET("/liquibase"),
        LiquibaseReport.class);
    assertEquals(OK, response.status());

    LiquibaseReport liquibaseReport = response.body();
    assertNotNull(liquibaseReport);
    assertNotNull(liquibaseReport.getChangeSets());
    assertEquals(4, liquibaseReport.getChangeSets().size());
  }

  static class LiquibaseReport {

    private List<ChangeSet> changeSets;

    public void setChangeSets(List<ChangeSet> changeSets) {
      this.changeSets = changeSets;
    }

    public List<ChangeSet> getChangeSets() {
      return changeSets;
    }
  }

  static class ChangeSet {

    private String id;

    public void setId(String id) {
      this.id = id;
    }

    public String getId() {
      return id;
    }
  }
}
