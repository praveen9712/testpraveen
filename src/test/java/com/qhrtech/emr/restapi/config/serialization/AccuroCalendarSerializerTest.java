
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import com.qhrtech.emr.accuro.model.time.CalendarPrecision;
import com.qhrtech.emr.accuro.utils.time.TimeUtil;
import java.util.Calendar;
import java.util.TimeZone;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AccuroCalendarSerializerTest {

  private ObjectMapper mapper;
  private Calendar testDate;

  @Before
  public void setup() {
    mapper = new ObjectMapper();
    mapper.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
    TimeUtil.setThreadTimeZone(TimeZone.getTimeZone("Canada/Pacific"));

    SimpleModule module = new SimpleModule();
    module.addSerializer(AccuroCalendar.class, new AccuroCalendarSerializer());
    mapper.registerModule(module);

    testDate = Calendar.getInstance();
    testDate.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
    testDate.set(2015, Calendar.DECEMBER, 25, 9, 38, 10);
    testDate.set(Calendar.MILLISECOND, 60);
  }

  @After
  public void tearDown() {
    TimeUtil.clearThreadTimeZone();
  }

  @Test
  public void testSerializeEmpty() throws JsonProcessingException {
    AccuroCalendar cal = new AccuroCalendar(null, CalendarPrecision.Millisecond);
    String jsonString = mapper.writeValueAsString(cal);
    Assert.assertEquals("null", jsonString);
  }

  @Test
  public void testSerializeMillisecond() throws JsonProcessingException {
    AccuroCalendar cal = new AccuroCalendar(testDate, CalendarPrecision.Millisecond);
    String jsonString = mapper.writeValueAsString(cal);
    Assert.assertEquals("\"2015-12-25T09:38:10.060-0800\"", jsonString);
  }

  @Test
  public void testSerializeSecond() throws JsonProcessingException {
    AccuroCalendar cal = new AccuroCalendar(testDate, CalendarPrecision.Second);
    String jsonString = mapper.writeValueAsString(cal);
    Assert.assertEquals("\"2015-12-25T09:38:10-0800\"", jsonString);
  }

  @Test
  public void testSerializeMin() throws JsonProcessingException {
    AccuroCalendar cal = new AccuroCalendar(testDate, CalendarPrecision.Minute);
    String jsonString = mapper.writeValueAsString(cal);
    Assert.assertEquals("\"2015-12-25T09:38-0800\"", jsonString);
  }

  @Test
  public void testSerializeHour() throws JsonProcessingException {
    AccuroCalendar cal = new AccuroCalendar(testDate, CalendarPrecision.Hour);
    String jsonString = mapper.writeValueAsString(cal);
    Assert.assertEquals("\"2015-12-25T09-0800\"", jsonString);
  }

  @Test
  public void testSerializeDay() throws JsonProcessingException {
    AccuroCalendar cal = new AccuroCalendar(testDate, CalendarPrecision.Day);
    String jsonString = mapper.writeValueAsString(cal);
    Assert.assertEquals("\"2015-12-25\"", jsonString);
  }

  @Test
  public void testSerializeMonth() throws JsonProcessingException {
    AccuroCalendar cal = new AccuroCalendar(testDate, CalendarPrecision.Month);
    String jsonString = mapper.writeValueAsString(cal);
    Assert.assertEquals("\"2015-12\"", jsonString);
  }

  @Test
  public void testSerializeYear() throws JsonProcessingException {
    AccuroCalendar cal = new AccuroCalendar(testDate, CalendarPrecision.Year);
    String jsonString = mapper.writeValueAsString(cal);
    Assert.assertEquals("\"2015\"", jsonString);
  }

  @Test
  public void testAllPrecisionsSupported() throws JsonProcessingException {
    for (CalendarPrecision p : CalendarPrecision.values()) {
      AccuroCalendar cal = new AccuroCalendar(testDate, p);
      String result = mapper.writeValueAsString(cal);
      Assert.assertNotNull(result);
    }
  }
}
