
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.qhrtech.emr.accuro.utils.time.TimeUtil;
import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalendarDeserializerTest {

  private ObjectMapper mapper;

  @Before
  public void setup() {
    mapper = new ObjectMapper();
    mapper.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
    TimeUtil.setThreadTimeZone(TimeZone.getTimeZone("Canada/Pacific"));

    SimpleModule module = new SimpleModule();
    module.addDeserializer(Calendar.class, new CalendarDeserializer());
    mapper.registerModule(module);
  }

  @After
  public void tearDown() {
    TimeUtil.clearThreadTimeZone();
  }

  @Test
  public void testDeserializeNull() throws IOException {
    Calendar result = mapper.readValue("null", Calendar.class);
    Assert.assertNull(result);
  }

  @Test
  public void testDeserializeFull() throws IOException {
    Calendar c = mapper.readValue("\"2015-12-25T09:38:10.060-0800\"", Calendar.class);
    Assert.assertNotNull(c);

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
  public void testDeserializeDateOnly() throws IOException {
    TestClassDateOnly result =
        mapper.readValue("{\"value\":\"2015-12-25\"}", TestClassDateOnly.class);
    Assert.assertNotNull(result);
    Calendar c = result.getValue();
    Assert.assertNotNull(c);

    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(0, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(0, c.get(Calendar.MINUTE));
    Assert.assertEquals(0, c.get(Calendar.SECOND));
    Assert.assertEquals(0, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }

  @Test(expected = JsonProcessingException.class)
  public void testDeserializeDateOnlyWithTimeSent() throws IOException {
    mapper.readValue("{\"value\":\"2015-12-25T09:38:10.060-0800\"}", TestClassDateOnly.class);
  }

  @Test
  public void testDeserializeTimeZone() throws IOException {
    TestClassTimeZone result =
        mapper.readValue("{\"value\":\"2015-12-25T17:38:10.060\"}", TestClassTimeZone.class);
    Assert.assertNotNull(result);
    Calendar c = result.getValue();
    Assert.assertNotNull(c);

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
  public void testDeserializeTimeZoneWithSentSameTimeZone() throws IOException {
    TestClassTimeZone result =
        mapper.readValue("{\"value\":\"2015-12-25T17:38:10.060+0000\"}", TestClassTimeZone.class);
    Assert.assertNotNull(result);
    Calendar c = result.getValue();
    Assert.assertNotNull(c);

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
  public void testDeserializeTimeZoneWithSentDifferentTimeZone() throws IOException {
    TestClassTimeZone result =
        mapper.readValue("{\"value\":\"2015-12-25T17:38:10.060-0700\"}", TestClassTimeZone.class);
    Assert.assertNotNull(result);
    Calendar c = result.getValue();
    Assert.assertNotNull(c);

    Assert.assertEquals(2015, c.get(Calendar.YEAR));
    Assert.assertEquals(Calendar.DECEMBER, c.get(Calendar.MONTH));
    Assert.assertEquals(25, c.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals(16, c.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals(38, c.get(Calendar.MINUTE));
    Assert.assertEquals(10, c.get(Calendar.SECOND));
    Assert.assertEquals(60, c.get(Calendar.MILLISECOND));
    Assert.assertEquals(TimeUtil.getThreadTimeZone(), c.getTimeZone());
  }
}
