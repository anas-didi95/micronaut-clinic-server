package com.anasdidi.clinic.common;

import com.anasdidi.clinic.common.CommonConstants.GQLContext;

import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import io.micronaut.data.model.Pageable;

public abstract class BaseDataFetcher {

  protected Pageable getPageable(DataFetchingEnvironment env) {
    int page = getPage(env);
    int size = getSize(env);
    return Pageable.from(page - 1, size);
  }

  protected Object getContext(DataFetchingEnvironment env, GQLContext context) {
    return ((GraphQLContext) env.getContext()).get(context.key);
  }

  private int getPage(DataFetchingEnvironment env) {
    int page = env.getArgument("page");
    return Math.max(page, 1);
  }

  private int getSize(DataFetchingEnvironment env) {
    int size = env.getArgument("size");
    return Math.max(size, 1);
  }
}
