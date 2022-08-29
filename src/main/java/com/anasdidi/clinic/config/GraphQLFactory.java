package com.anasdidi.clinic.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.anasdidi.clinic.domain.user.UserDataFetcher;

import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.GraphQLContext;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.micronaut.configuration.graphql.GraphQLExecutionInputCustomizer;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.core.io.ResourceResolver;
import jakarta.inject.Singleton;
import reactor.core.publisher.Mono;

@Factory
public class GraphQLFactory {

  private static final Logger logger = LoggerFactory.getLogger(GraphQLFactory.class);

  @Singleton
  public GraphQL graphQL(ResourceResolver resourceResolver, UserDataFetcher userDataFetcher) {
    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    typeRegistry
        .merge(getTypeDefinitionRegistry(resourceResolver, schemaParser, "classpath:graphql/schema.graphqls"))
        .merge(getTypeDefinitionRegistry(resourceResolver, schemaParser, "classpath:graphql/user.graphqls"));

    RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
        .type("Query", typeWiring -> typeWiring
            .dataFetcher("userList", userDataFetcher.getUserList())
            .dataFetcher("pagination", (env) -> {
              return Mono.defer(() -> {
                GraphQLContext context = env.getContext();
                Map<String, Object> result = context.get("pagination");
                String v = context.get("test");
                String v2 = context.get("test2");
                System.out.println("result=" + result);
                System.out.println("v=" + v);
                System.out.println("v2=" + v2);
                logger.debug("result={}", result);
                return Mono.just(result);
              }).delaySubscription(Duration.ofSeconds(1)).toFuture();
            }))
        .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    return GraphQL.newGraphQL(graphQLSchema).build();
  }

  @Singleton
  @Bean
  public GraphQLExecutionInputCustomizer graphQLExecutionInputCustomizer() {
    return (executionInput, httpRequest, httpResponse) -> {
      ExecutionInput.Builder executionInputBuilder = ExecutionInput.newExecutionInput()
          .query(executionInput.getQuery())
          .operationName(executionInput.getOperationName())
          .variables(executionInput.getVariables())
          .context(GraphQLContext.newContext().of("test", "hello world").build());
      ExecutionInput a = executionInputBuilder.build();
      GraphQLContext context = (GraphQLContext) a.getContext();
      context.put("test2", "bye");
      return Publishers.just(a);
    };
  }

  private TypeDefinitionRegistry getTypeDefinitionRegistry(ResourceResolver resourceResolver, SchemaParser schemaParser,
      String path) {
    return schemaParser
        .parse(new BufferedReader(new InputStreamReader(resourceResolver.getResourceAsStream(path).get())));
  }
}
