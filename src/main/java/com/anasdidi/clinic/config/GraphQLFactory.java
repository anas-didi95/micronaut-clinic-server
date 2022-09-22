package com.anasdidi.clinic.config;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.dataloader.DataLoader;
import org.dataloader.DataLoaderRegistry;

import com.anasdidi.clinic.common.CommonConstants.GQLContext;
import com.anasdidi.clinic.common.CommonUtils;
import com.anasdidi.clinic.domain.auth.AuthDTO;
import com.anasdidi.clinic.domain.auth.AuthDataFetcher;
import com.anasdidi.clinic.domain.user.UserDTO;
import com.anasdidi.clinic.domain.user.UserDataFetcher;
import com.anasdidi.clinic.domain.user.UserService;

import graphql.GraphQL;
import graphql.GraphQLContext;
import graphql.TypeResolutionEnvironment;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolver;
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
  public GraphQL graphQL(ResourceResolver resourceResolver, UserDataFetcher userDataFetcher,
      AuthDataFetcher authDataFetcher) {
    SchemaParser schemaParser = new SchemaParser();
    TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    typeRegistry
        .merge(getTypeDefinitionRegistry(resourceResolver, schemaParser, "classpath:graphql/schema.graphqls"))
        .merge(getTypeDefinitionRegistry(resourceResolver, schemaParser, "classpath:graphql/user.graphqls"))
        .merge(getTypeDefinitionRegistry(resourceResolver, schemaParser, "classpath:graphql/auth.graphqls"));

    RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
        .type("Query", typeWiring -> typeWiring
            .dataFetcher("userSearch", userDataFetcher.getUserSearch())
            .dataFetcher("user", userDataFetcher.getUser())
            .dataFetcher("authSearch", authDataFetcher.getAuthSearch()))
        .type("Auth", typeWiring -> typeWiring.dataFetcher("user", authDataFetcher.getUserId()))
        .type("RecordMetadata", typeWiring -> typeWiring.typeResolver(new TypeResolver() {
          @Override
          public GraphQLObjectType getType(TypeResolutionEnvironment env) {
            Object object = env.getObject();
            if (object instanceof UserDTO) {
              return env.getSchema().getObjectType("User");
            } else if (object instanceof AuthDTO) {
              return env.getSchema().getObjectType("Auth");
            }
            return null;
          }
        }))
        .build();

    SchemaGenerator schemaGenerator = new SchemaGenerator();
    GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    return GraphQL.newGraphQL(graphQLSchema).build();
  }

  @Singleton
  public GraphQLExecutionInputCustomizer graphQLExecutionInputCustomizer() {
    return (executionInput, httpRequest, httpResponse) -> {
      return Publishers
          .just(executionInput.transform((builder) -> builder
              .context(GraphQLContext.newContext()
                  .of(GQLContext.TRACE_ID.key, CommonUtils.generateTraceId())
                  .build())));
    };
  }

  @Singleton
  public DataLoaderRegistry dataLoaderRegistry(UserService userService) {
    DataLoaderRegistry registry = new DataLoaderRegistry();
    registry.register("user", DataLoader.<String, UserDTO>newMappedDataLoader((keys, env) -> {
      return userService.getUsersByIdIn(keys, CommonUtils.generateTraceId()).collectMap(o -> o.getId(), o -> o)
          .toFuture();
    }));
    return registry;
  }

  private TypeDefinitionRegistry getTypeDefinitionRegistry(ResourceResolver resourceResolver, SchemaParser schemaParser,
      String path) {
    return schemaParser
        .parse(new BufferedReader(new InputStreamReader(resourceResolver.getResourceAsStream(path).get())));
  }
}
