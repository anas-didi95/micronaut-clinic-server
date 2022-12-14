package com.anasdidi.clinic;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.servers.ServerVariable;
import io.swagger.v3.oas.annotations.servers.Servers;

@OpenAPIDefinition(info = @Info(//
    title = "Micronaut Clinic Microservice", //
    version = "0.2.0", //
    description = "Micronaut Clinic Microservice", //
    license = @License(name = "Apache 2.0", url = "http://www.apache.org/licenses"), //
    contact = @Contact(url = "https://anasdidi.dev", name = "Anas Juwaidi", email = "anas.didi95@gmail.com")), security = @SecurityRequirement(name = "jwt"))
@Servers({
    @Server(url = "http://localhost:{port}", description = "Localhost", variables = {
        @ServerVariable(name = "port", allowableValues = { "8080", "8081", "8082" }, defaultValue = "8080")
    })
})
@SecuritySchemes({
    @SecurityScheme(name = "jwt", type = SecuritySchemeType.HTTP, scheme = "bearer", paramName = "Authorization")
})
public class Application {

  static {
    System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
  }

  public static void main(String[] args) {
    Micronaut.run(Application.class, args);
  }
}
