package com.anasdidi.clinic.common;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class CommonUtils {

  private static BCryptPasswordEncoder passwordEncoder;

  public static IRecordMetadata copy(IRecordMetadata in, IRecordMetadata out) {
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

  public static BCryptPasswordEncoder getPasswordEncoder() {
    if (passwordEncoder == null) {
      passwordEncoder = new BCryptPasswordEncoder(14);
    }
    return passwordEncoder;
  }

  public static String generateTraceId() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
    String part1 = sdf.format(new Date());

    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 8;
    Random random = new Random(System.currentTimeMillis());
    String part2 = random.ints(leftLimit, rightLimit + 1)
        .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
        .limit(targetStringLength)
        .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
        .toString();

    return "%s%s".formatted(part1, part2);
  }
}
