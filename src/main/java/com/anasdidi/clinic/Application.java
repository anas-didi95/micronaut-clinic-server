package com.anasdidi.clinic;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(info = @Info(//
    title = "Micronaut Clinic Microservice", //
    version = "0.2.0", //
    description = "Micronaut Clinic Microservice", //
    license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses"), //
    contact = @Contact(url = "https://anasdidi.dev", name = "Anas Juwaidi", email = "anas.didi95@gmail.com")))
public class Application {

  static {
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
  }

  public static void main(String[] args) {
    Micronaut.run(Application.class, args);
  }
}
