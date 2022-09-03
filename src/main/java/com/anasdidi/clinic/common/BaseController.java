package com.anasdidi.clinic.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public abstract class BaseController {

  protected String generateTraceId() {
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
