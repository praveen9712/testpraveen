
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Factory class that produces Jackson ObjectMapper instances that are configured with the
 * serializers and deserializers for use with the Accuro data model.
 */
public class AccuroObjectMapperFactory {

  private AccuroObjectMapperFactory() {
  }

  /**
   * Creates a Jackson ObjectMapper JSON instance that is configured with the serializers and
   * deserializers for use with the Accuro data model.
   *
   * @return The JSON ObjectMapper object.
   */
  public static ObjectMapper newJsonObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mmZZZ"));

    SimpleModule module = new SimpleModule();
    module.addSerializer(AccuroCalendar.class, new AccuroCalendarSerializer());
    module.addDeserializer(AccuroCalendar.class, new AccuroCalendarDeserializer());
    module.addSerializer(Calendar.class, new CalendarSerializer());
    module.addDeserializer(Calendar.class, new CalendarDeserializer());
    module.addSerializer(Color.class, new ColorSerializer());
    module.addDeserializer(Color.class, new ColorDeserializer());

    mapper.registerModule(module);
    mapper.registerModule(new JodaModule());

    return mapper;
  }
}
