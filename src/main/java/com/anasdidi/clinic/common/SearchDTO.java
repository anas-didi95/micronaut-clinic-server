package com.anasdidi.clinic.common;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class SearchDTO<T> {

  private List<T> resultList;
  private PaginationDTO pagination;

  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  @Getter
  @Setter
  @ToString
  public static class PaginationDTO {

    private int totalPages;
    private int pageNumber;
    private int size;
  }
}
