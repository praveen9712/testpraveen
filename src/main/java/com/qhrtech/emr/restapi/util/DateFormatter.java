
package com.qhrtech.emr.restapi.util;

import java.util.Objects;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.DateTimeFormatterBuilder;

public class DateFormatter {


  public static LocalDateTime toDay(LocalDateTime localDateTime) {
    if (localDateTime == null) {
      return null;
    }
    int year = localDateTime.getYear();
    int month = localDateTime.getMonthOfYear();
    int day = localDateTime.getDayOfMonth();
    return new LocalDateTime(year, month, day, 0, 0, 00, 0);
  }

  public static LocalDateTime toLocalDatetimeOptionalTime(String s) {
    try {
      if (Objects.isNull(s)) {
        return null;
      }

      DateTimeFormatter formatter =
          new DateTimeFormatterBuilder().appendYear(4, 4)
              .append(DateTimeFormat.forPattern("-MM-dd"))
              .appendOptional(new DateTimeFormatterBuilder().appendLiteral('T')
                  .append(DateTimeFormat.forPattern("HH:mm:ss")).toParser())
              .appendOptional(
                  new DateTimeFormatterBuilder().append(DateTimeFormat.forPattern(".SSSS"))
                      .toParser())
              .toFormatter();
      LocalDateTime localDateTime = formatter.parseLocalDateTime(s);
      return localDateTime;

    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid DateTime:" + s, e);
    }
  }

  public static LocalDate toLocalDate(String s) {
    try {

      DateTimeFormatter formatter =
          new DateTimeFormatterBuilder().appendYear(4, 4)
              .append(DateTimeFormat.forPattern("-MM-dd"))
              .toFormatter();
      LocalDate localDate = formatter.parseLocalDate(s);
      return localDate;

    } catch (Exception e) {
      throw new IllegalArgumentException("Invalid Date:" + s, e);
    }
  }


}
