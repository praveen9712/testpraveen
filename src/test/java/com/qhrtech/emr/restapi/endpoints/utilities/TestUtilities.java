
package com.qhrtech.emr.restapi.endpoints.utilities;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.lang.RandomStringUtils;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class TestUtilities {

  private static final Random random;
  private static final AtomicInteger counter;

  static {
    random = new SecureRandom();
    counter = new AtomicInteger();
  }

  private TestUtilities() {
    // prevents instantiation
  }

  public static int nextId() {
    return counter.incrementAndGet();
  }

  public static boolean nextBoolean() {
    return random.nextBoolean();
  }

  public static String nextString(int limit) {
    return RandomStringUtils.randomAlphanumeric(limit);
  }

  public static long nextLong() {
    return random.nextLong();
  }

  public static int nextInt() {
    return random.nextInt();
  }

  public static int nextInt(int bound) {
    return random.nextInt(bound);
  }

  /**
   * Generates random number between the two given numbers inclusive.
   */
  public static int nextInt(int lowerBound, int upperBound) {
    return random.nextInt(upperBound - lowerBound + 1) + lowerBound;
  }

  public static LocalDate nextLocalDate() {
    long ms = -946771200000L; // January 1, 1940
    long date = ms + (Math.abs(random.nextLong()) % (100L * TimeUnit.DAYS.toMillis(365)));
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    calendar.setTime(new Date(date));
    return new LocalDate(calendar);
  }

  public static LocalDateTime nextLocalDateTime() {
    long ms = -946771200000L; // January 1, 1940
    long date = ms + (Math.abs(random.nextLong()) % (100L * TimeUnit.DAYS.toMillis(365)));
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    calendar.setTime(new Date(date));
    return new LocalDateTime(calendar);
  }

  public static LocalDateTime nextLocalDateTime(boolean includeSeconds) {
    LocalDate date = new LocalDate(getRandomTimeBetweenTwoDates());
    int day = nextInt(1, date.dayOfMonth().getMaximumValue());
    int hour = nextInt(0, 23);
    int minute = nextInt(0, 59);
    int second = 0;
    if (includeSeconds) {
      second = nextInt(0, 59);
    }
    LocalTime time = new LocalTime(hour, minute, second);
    LocalDateTime dateTime = date.withDayOfMonth(day).toLocalDateTime(time);
    return dateTime;
  }

  private static long getRandomTimeBetweenTwoDates() {
    long beginTime = Timestamp.valueOf("1971-01-01 00:00:00").getTime();
    long endTime = Timestamp.valueOf("9999-12-31 00:58:00").getTime();
    long diff = endTime - beginTime + 1L;
    return beginTime + (long) (Math.random() * (double) diff);
  }



  /**
   * Generates a random, valid date between January 1 1940 and 2040, in Utc.
   *
   * @return
   */
  public static Calendar nextUtcCalendar() {
    long ms = -946771200000L; // January 1, 1940
    long date = ms + (Math.abs(random.nextLong()) % (100L * TimeUnit.DAYS.toMillis(365)));
    Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
    calendar.setTime(new Date(date));
    return calendar;
  }

  public static <E extends Enum<E>> E nextValue(Class<E> enumType) {
    E[] values = enumType.getEnumConstants();
    if (values == null || values.length < 1) {
      throw new IllegalStateException("Must be a non-empty Enum type");
    }
    return values[nextInt(values.length)];
  }

  public static <E> E nextElement(Collection<E> e) {
    return e.stream().skip((int) (e.size() * random.nextDouble())).findFirst()
        .orElseThrow(() -> new IllegalStateException("Collection must contain elements."));
  }
}
