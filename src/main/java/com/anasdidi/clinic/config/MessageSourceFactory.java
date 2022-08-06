package com.anasdidi.clinic.config;

import io.micronaut.context.MessageSource;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.i18n.ResourceBundleMessageSource;
import jakarta.inject.Singleton;

@Factory
public class MessageSourceFactory {

  @Singleton
  public MessageSource messageSource() {
    return new ResourceBundleMessageSource("i18n.messages");
  }
}
