package com.anasdidi.clinic.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;

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
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.core.io.ResourceResolver;
import jakarta.inject.Singleton;

@Factory
public class GraphQLFactory {

  @Singleton
  public GraphQL graphQL(ResourceResolver resourceResolver, UserDataFetcher userDataFetcher) {
    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    typeRegistry
        .merge(getTypeDefinitionRegistry(resourceResolver, schemaParser, "classpath:graphql/schema.graphqls"))
        .merge(getTypeDefinitionRegistry(resourceResolver, schemaParser, "classpath:graphql/user.graphqls"));

    RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
        .type("Query", typeWiring -> typeWiring
            .dataFetcher("userSearch", userDataFetcher.getUserSearch())
            .dataFetcher("user", userDataFetcher.getUser()))
        .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    return GraphQL.newGraphQL(graphQLSchema).build();
  }

  @Singleton
  public GraphQLExecutionInputCustomizer graphQLExecutionInputCustomizer() {
    return (executionInput, httpRequest, httpResponse) -> {
      ExecutionInput.Builder executionInputBuilder = ExecutionInput.newExecutionInput()
          .query(executionInput.getQuery())
          .operationName(executionInput.getOperationName())
          .variables(executionInput.getVariables())
          .context(GraphQLContext.newContext().build());
      return Publishers.just(executionInputBuilder.build());
    };
  }

  private TypeDefinitionRegistry getTypeDefinitionRegistry(ResourceResolver resourceResolver, SchemaParser schemaParser,
      String path) {
    return schemaParser
        .parse(new BufferedReader(new InputStreamReader(resourceResolver.getResourceAsStream(path).get())));
  }
}
