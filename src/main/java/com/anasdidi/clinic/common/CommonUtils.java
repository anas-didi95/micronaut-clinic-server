package com.anasdidi.clinic.common;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class CommonUtils {

  public static IRecordMetadata copy(IRecordMetadata in, IRecordMetadata out) {
    out.setId(in.getId());
    out.setIsDeleted(in.getIsDeleted());
    out.setCreatedBy(in.getCreatedBy());
    out.setCreatedDate(in.getCreatedDate());
    out.setUpdatedBy(in.getUpdatedBy());
    out.setUpdatedDate(in.getUpdatedDate());
    out.setVersion(in.getVersion());
    return out;
  }

  public static String getFormattedInstant(Instant instant, String format) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(ZoneId.systemDefault());
    return formatter.format(instant);
  }
}
