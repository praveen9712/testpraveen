
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.qhrtech.emr.accuro.utils.time.TimeUtil;
import java.io.IOException;
import java.util.TimeZone;
import javax.ws.rs.WebApplicationException;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JodaLocalDateDeserializerTest {

  private ObjectMapper mapper;

  @Before
  public void setup() {
    mapper = new ObjectMapper();
    mapper.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
    TimeUtil.setThreadTimeZone(TimeZone.getTimeZone("Canada/Pacific"));

    SimpleModule module = new SimpleModule();
    module.addDeserializer(LocalDate.class, new JodaLocalDateDeserializer());
    mapper.registerModule(module);
  }

  @After
  public void tearDown() {
    TimeUtil.clearThreadTimeZone();
  }

  @Test
  public void testDeserializeNull() throws IOException {
    LocalDate result = mapper.readValue("null", LocalDate.class);
    Assert.assertNull(result);
  }

  @Test
  public void testDeserializeFull() throws IOException {
    LocalDate result = mapper.readValue("\"2015-12-25\"", LocalDate.class);
    Assert.assertNotNull(result);
  }

  @Test(expected = WebApplicationException.class)
  public void testDeserializeWholeNumber() throws IOException {
    mapper.readValue("2015", LocalDate.class);
  }


}
