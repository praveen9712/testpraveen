
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.model.time.CalendarPrecision;
import com.qhrtech.emr.accuro.utils.time.TimeUtil;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AccuroCalendarDeserializerTest {

  private ObjectMapper mapper;

  @Before
  public void setup() {
    mapper = new ObjectMapper();
    mapper.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
    TimeUtil.setThreadTimeZone(TimeZone.getTimeZone("Canada/Pacific"));

    SimpleModule module = new SimpleModule();
    module.addDeserializer(AccuroCalendar.class, new AccuroCalendarDeserializer());
    mapper.registerModule(module);
  }

  @After
  public void tearDown() {
    TimeUtil.clearThreadTimeZone();
  }

  @Test
  public void testSerializeEmpty() throws IOException {
    AccuroCalendar cal = mapper.readValue("null", AccuroCalendar.class);
    Assert.assertNull(cal);
  }

  @Test
  public void testSerializeMillisecond1WithSameTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.6-0800\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(600, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMillisecond2WithSameTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.60-0800\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(600, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMillisecond3WithSameTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.060-0800\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(60, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeSecondWithSameTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10-0800\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Second, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMinuteWithSameTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38-0800\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Minute, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
  }

  @Test
  public void testSerializeHourWithSameTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09-0800\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Hour, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(0, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMillisecond1WithDifferentTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.6-0700\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(8, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(600, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMillisecond2WithDifferentTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.60-0700\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(8, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(600, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMillisecond3WithDifferentTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.060-0700\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(8, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(60, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeSecondWithDifferentTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10-0700\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Second, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(8, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMinuteWithDifferentTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38-0700\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Minute, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(8, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeHourWithDifferentTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09-0700\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Hour, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(8, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(0, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test(expected = JsonParseException.class)
  public void testSerializeDateWithTZ() throws IOException {
    mapper.readValue("\"2015-12-25-0800\"", AccuroCalendar.class);
  }

  @Test(expected = JsonParseException.class)
  public void testSerializeMonthWithTZ() throws IOException {
    mapper.readValue("\"2015-12-0800\"", AccuroCalendar.class);
  }

  @Test(expected = JsonParseException.class)
  public void testSerializeYearWithTZ() throws IOException {
    mapper.readValue("\"2015-0800\"", AccuroCalendar.class);
  }

  @Test
  public void testSerializeMillisecond1WithUtc() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.6Z\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(1, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(600, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMillisecond2WithUtc() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.60Z\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(1, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(600, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMillisecond3WithUtc() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.060Z\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(1, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(60, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeSecondWithUtc() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10Z\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Second, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(1, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMinuteWithUtc() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38Z\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Minute, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(1, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeHourWithUtc() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09Z\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Hour, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(1, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(0, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test(expected = JsonParseException.class)
  public void testSerializeDateWithUtc() throws IOException {
    mapper.readValue("\"2015-12-25Z\"", AccuroCalendar.class);
  }

  @Test(expected = JsonParseException.class)
  public void testSerializeMonthWithUtc() throws IOException {
    mapper.readValue("\"2015-12Z\"", AccuroCalendar.class);
  }

  @Test(expected = JsonParseException.class)
  public void testSerializeYearWithUtc() throws IOException {
    mapper.readValue("\"2015Z\"", AccuroCalendar.class);
  }

  @Test
  public void testSerializeMillisecond1WithoutTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.6\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(600, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMillisecond2WithoutTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.60\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(600, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMillisecond3WithoutTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10.060\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Millisecond, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(60, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeSecondWithoutTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38:10\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Second, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMinuteWithoutTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09:38\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Minute, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeHourWithoutTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25T09\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Hour, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(9, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(0, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeDateWithoutTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12-25\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Day, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(0, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeMonthWithoutTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015-12\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Month, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(1, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(0, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test
  public void testSerializeYearWithoutTZ() throws IOException {
    AccuroCalendar cal = mapper.readValue("\"2015\"", AccuroCalendar.class);
    Assert.assertNotNull(cal);
    Assert.assertEquals(CalendarPrecision.Year, cal.getPrecision());
    Calendar c = AccuroCalendar.unwrap(cal);
    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.JANUARY, c.get(Calendar.MONTH));
    Assert.assertEquals(1, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(0, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }
}
