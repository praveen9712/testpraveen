
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.qhrtech.emr.accuro.utils.time.TimeUtil;
import java.util.Calendar;
import java.util.TimeZone;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CalendarSerializerTest {

  private ObjectMapper mapper;
  private Calendar testDate;

  @Before
  public void setup() {
    mapper = new ObjectMapper();
    mapper.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
    TimeUtil.setThreadTimeZone(TimeZone.getTimeZone("Canada/Pacific"));

    SimpleModule module = new SimpleModule();
    module.addSerializer(Calendar.class, new CalendarSerializer());
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
  public void testSerializeFull() throws JsonProcessingException {
    String jsonString = mapper.writeValueAsString(testDate);
    Assert.assertEquals("\"2015-12-25T09:38:10.060-0800\"", jsonString);
  }

  @Test
  public void testSerializeDateOnly() throws JsonProcessingException {
    String jsonString = mapper.writeValueAsString(new TestClassDateOnly(testDate));
    Assert.assertEquals("{\"value\":\"2015-12-25\"}", jsonString);
  }

  @Test
  public void testSerializeTimeZone() throws JsonProcessingException {
    String jsonString = mapper.writeValueAsString(new TestClassTimeZone(testDate));
    Assert.assertEquals("{\"value\":\"2015-12-25T17:38:10.060+0000\"}", jsonString);
  }
}
