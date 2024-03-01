
package com.qhrtech.emr.restapi.config.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.qhrtech.emr.accuro.model.time.AccuroCalendar;
import java.io.IOException;

/**
 * A JSON deserializer that parses AccuroCalendar values.
 */
public class AccuroCalendarDeserializer extends AccuroCalendarBasedDeserializer<AccuroCalendar> {

  public AccuroCalendarDeserializer() {
    super(AccuroCalendar.class);
  }

  @Override
  public AccuroCalendar deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {
    return deserializeImpl(jp);
  }
}
