package com.anasdidi.clinic.common;

public final class CommonUtils {

  public static IRecordMetadata copy(IRecordMetadata in, IRecordMetadata out) {
    out.setId(in.getId());
    out.setCreatedBy(in.getCreatedBy());
    out.setCreatedDate(in.getCreatedDate());
    out.setUpdatedBy(in.getUpdatedBy());
    out.setUpdatedDate(in.getUpdatedDate());
    out.setVersion(in.getVersion());
    return out;
  }
}
