
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.model.time.CalendarPrecision;
import com.qhrtech.emr.accuro.utils.time.TimeUtil;
import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A generic serializer that will parse a text JSON value into AccuroCalendar objects. Can be used
 * as a basis for other data-based deserializers.
 *
 * @param <T> The class that the deserializer produces.
 */
public abstract class AccuroCalendarBasedDeserializer<T> extends StdScalarDeserializer<T> {

  // ACCAPI-974 fix. days cannot be more than 31 and months cannot be more than 12
  private static final Pattern SEARCH_PATTERN = Pattern.compile(
      "(\\d{4})(?:-(0[1-9]|1[012]|[1-9])(?:-(0[1-9]|[12][0-9]|3[01]|[1-9]))?(?:T(\\d{1,2})"
          + "(?::(\\d{1,2})(?::(\\d{1,2})(?:\\.(\\d{1,3}))?)?)?(Z|[+-]\\d{1,4})?)?)?");

  private TimeZone serializationTimeZone;

  public AccuroCalendarBasedDeserializer(Class<T> clazz) {
    super(clazz);
  }

  protected AccuroCalendarBasedDeserializer(Class<T> clazz, TimeZone serializationTimeZone) {
    super(clazz);
    this.serializationTimeZone = serializationTimeZone;
  }

  /**
   * Parse a String into an AccuroCalendar.
   *
   * @param date String Date to parse.
   * @param serializationTimeZone TimeZone to use in conversion.
   * @return AccuroCalendar object of parsed dated.
   */
  public static AccuroCalendar parse(String date, TimeZone serializationTimeZone) {
    Matcher m = SEARCH_PATTERN.matcher(date);
    if (!m.matches()) {
      return null;
    }

    String milliStr = m.group(7);
    int milli;
    if (milliStr == null) {
      milli = 0;
    } else {
      milli = Integer.parseInt(milliStr) * Math
          .round((float) Math.pow(10, ((double) 3 - milliStr.length())));
    }

    String monthStr = m.group(2);
    String dayStr = m.group(3);
    String hourStr = m.group(4);
    String minStr = m.group(5);
    String secondStr = m.group(6);
    CalendarPrecision precision;
    if (monthStr == null) {
      precision = CalendarPrecision.Year;
    } else if (dayStr == null) {
      precision = CalendarPrecision.Month;
    } else if (hourStr == null) {
      precision = CalendarPrecision.Day;
    } else if (minStr == null) {
      precision = CalendarPrecision.Hour;
    } else if (secondStr == null) {
      precision = CalendarPrecision.Minute;
    } else if (milliStr == null) {
      precision = CalendarPrecision.Second;
    } else {
      precision = CalendarPrecision.Millisecond;
    }

    TimeZone targetTz = TimeUtil.getTimeZone();

    String tzStr = m.group(8);
    TimeZone creationZone;
    if (tzStr == null) {
      creationZone = (serializationTimeZone != null ? serializationTimeZone : targetTz);
    } else if (tzStr.equals("Z")) {
      creationZone = TimeZone.getTimeZone("UTC");
    } else {
      creationZone = TimeZone.getTimeZone("GMT" + tzStr);
    }

    int year = Integer.parseInt(m.group(1));
    int month = monthStr == null ? Calendar.JANUARY : Integer.parseInt(monthStr) - 1;
    int day = dayStr == null ? 1 : Integer.parseInt(dayStr);

    // check for the leap year and max. date for any month
    if (!isValidDay(year, month, day)) {
      return null;
    }
    int hour = hourStr == null ? 0 : Integer.parseInt(hourStr);
    int min = minStr == null ? 0 : Integer.parseInt(minStr);
    int sec = secondStr == null ? 0 : Integer.parseInt(secondStr);
    Calendar cal = Calendar.getInstance(creationZone);
    cal.set(year, month, day, hour, min, sec);
    cal.set(Calendar.MILLISECOND, milli);

    if (!creationZone.equals(targetTz)) {
      Calendar c2 = Calendar.getInstance(targetTz);
      c2.setTimeInMillis(cal.getTimeInMillis());
      cal = c2;
    }

    return new AccuroCalendar(cal, precision);
  }

  // ACCAPI-974 fix. Leap year validation and days in shorter and longer months
  private static boolean isValidDay(int year, int month, int day) {
    Set<Integer> longerMonths = new HashSet<>(Arrays.asList(0, 2, 4, 6, 7, 9, 11));
    Set<Integer> shorterMonths = new HashSet<>(Arrays.asList(1, 3, 5, 8, 10));

    if (longerMonths.contains(month) && day <= 31) {
      return true;
    } else if (shorterMonths.contains(month) && day <= 30) {
      if (month == 1) {
        if (!java.time.Year.isLeap(year) && day >= 29) {
          return false;
        } else if (java.time.Year.isLeap(year) && day > 29) {
          return false;
        } else {
          return true;
        }
      } else {
        return true;
      }

    } else {
      return false;
    }


  }

  protected AccuroCalendar deserializeImpl(JsonParser jp) throws IOException {
    String textValue = jp.getText();
    AccuroCalendar calendar = parse(textValue, serializationTimeZone);
    if (calendar == null) {
      throw new JsonParseException(jp, "Unsupported date format: " + textValue,
          jp.getCurrentLocation());
    }

    return calendar;
  }
}
