package com.anasdidi.clinic;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.micronaut.runtime.EmbeddedApplication;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;

@MicronautTest
class ApplicationTests {

  private static final Logger logger = LoggerFactory.getLogger(ApplicationTests.class);

  @Inject
  EmbeddedApplication<?> application;

  @Test
  void testItWorks() {
    logger.info("[testItWorks] Testing logger");
    Assertions.assertTrue(application.isRunning());
  }

  @Test
  void testItThrows() throws Exception {
    Exception e = new Exception("TEST");
    logger.error("[testItThrows] Testing logger", e);
    logger.info("[testItThrows] Print error done");
    Assertions.assertTrue(application.isRunning());
  }
}
